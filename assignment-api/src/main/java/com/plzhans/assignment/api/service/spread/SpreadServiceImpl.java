package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.service.lock.LockInfra;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.repository.cache.CacheRepository;
import com.plzhans.assignment.api.service.spread.datatype.*;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadAmountEntity;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import com.plzhans.assignment.common.error.ClientError;
import com.plzhans.assignment.common.error.ServerError;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SpreadServiceImpl implements SpreadService {

    public static final int DEFAULT_DISTRIBUTE_EXPIRED_SECONDS = 60 * 10;
    public static final int DEFAULT_FIND_INACTIVE_DAYS = 7;

    private static final long LOCK_LEASE_TIME_MS = 3000L;
    private static final long LOCK_WAIT_TIME_MS = 3000L;

    SpreadTokenGenerator spreadTokenGenerator;
    SpreadAmountGenerator spreadAmountGenerator;
    SpreadRepository spreadRepository;
    LockInfra lockInfra;
    CacheRepository cacheRepository;

    public SpreadServiceImpl(SpreadRepository spreadRepository, CacheRepository cacheRepository, LockInfra lockInfra) {
        this.spreadTokenGenerator = new SpreadTokenGenerator();
        this.spreadAmountGenerator = new DefaultSpreadAmountGenerator();
        this.spreadRepository = spreadRepository;
        this.cacheRepository = cacheRepository;
        this.lockInfra = lockInfra;
    }

    @Transactional
    @Override
    public DistributeResult distribute(AuthRoomRequester requester, DistributeParam param) throws Exception {
        // 컨트롤러에서 걸리지만.. 방어코드
        if (param.getAmount() <= 0) {
            // 금액 오류 : 0원 방지
            throw new ClientError.InvalidParam("amount");
        } else if (param.getReceiverCount() <= 0) {
            // 인원수 오류
            throw new ClientError.InvalidParam("receiverCount");
        }

        // entity
        val entity = SpreadEventEntity.builder()
                .state(SpreadState.Active)
                .token(spreadTokenGenerator.nextToken())
                .userId(requester.getUserId())
                .roomId(requester.getRoomId())
                .totalAmount(param.getAmount())
                .receiverCount(param.getReceiverCount())
                .expiredSeconds(DEFAULT_DISTRIBUTE_EXPIRED_SECONDS)
                .build();
        entity.addAmount(spreadAmountGenerator.generateToList(entity.getTotalAmount(), entity.getReceiverCount()));

        // repository save
        this.spreadRepository.save(entity);
        if (entity.getNo() == 0) {
            throw new Exception("spreadRepository.save");
        }

        // cache 등록
        cacheRepository.setValue("spread:" + entity.getToken(), entity.getExpiredDate(), entity.getExpiredSeconds());

        // result
        return DistributeResult.builder()
                .token(entity.getToken())
                .createdDate(entity.getCreatedAt())
                .expiredDate(entity.getExpiredDate())
                .build();
    }

    /**
     * 캐시에서 선행 검사
     * @param  token 토큰
     * @return 결과
     */
    private boolean validatorCacheToken(String token) {

        val expredDate = this.cacheRepository.<LocalDateTime>getValue(token);
        if (expredDate != null) {
            // 캐시가 있는데 만료시간이 지난 경우
            if (expredDate.isBefore(LocalDateTime.now())) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public DistributeReceiveResult receiveByToken(AuthRoomRequester session, String token) throws Exception {

        // 캐시에서 선행 검사
        if (!this.validatorCacheToken(token)) {
            // 만료시간이 지남
            return new DistributeReceiveResult(DistributeReceiveResultCode.Expired);
        }

        // 분산처리를 위한 User Lock 인터페이스
        val lock = lockInfra.getLock(token, session.getUserId());
        if (!lock.tryLock(LOCK_WAIT_TIME_MS, LOCK_LEASE_TIME_MS)) {
            throw new ServerError.ConcurrentModification(String.format("ConcurrentModification. token=%s, userid=%s", token, session.getUserId()));
        }

        try {
            val entity = this.spreadRepository.findByTokenAndRoomId(token, session.getRoomId());
            if (entity == null) {
                throw new ClientError.Notfound("spread");
            }

            // 자신이 받을수 없음
            if (entity.getUserId() == session.getUserId()) {
                throw new ClientError.InvalidParam("user_id");
            }

            // 만료시간이 지난 뿌리기는 받을수 없음
            if (entity.getExpiredDate().isBefore(LocalDateTime.now())) {
                return new DistributeReceiveResult(DistributeReceiveResultCode.Expired);
            }

            val amounts = entity.getAmounts();
            if (amounts == null) {
                throw new ClientError.Notfound("amounts");
            }

            // 중복 지급 받을 수 없음
            val overlap = amounts.stream()
                    .filter(x -> x.getReceiverId() == session.getUserId())
                    .findFirst().orElse(null);
            if (overlap != null) {
                return DistributeReceiveResult.builder()
                        .code(DistributeReceiveResultCode.Received)
                        .amount(overlap.getAmount())
                        .build();
            }

            // 미지급한 금액 찾기
            val amountEntity = amounts.stream()
                    .filter(SpreadAmountEntity::isReady)
                    .findFirst().orElse(null);
            if (amountEntity == null) {
                // 더이상 받을 금액이 없음
                return new DistributeReceiveResult(DistributeReceiveResultCode.Finished);
            }

            // set received
            amountEntity.setReceived(session.getUserId());

            // repository save
            this.spreadRepository.save(entity);

            return DistributeReceiveResult.builder()
                    .code(DistributeReceiveResultCode.Ok)
                    .amount(amountEntity.getAmount())
                    .build();
        } catch (Exception err) {
            lock.release();
            throw err;
        }
    }

    @Override
    public DistributeStatusResult getDistributeStatusByToken(AuthRoomRequester session, String token) {

        val entity = this.spreadRepository.findByTokenAndRoomId(token, session.getRoomId());
        if (entity == null) {
            throw new ClientError.Notfound("spread");
        } else if (entity.getUserId() != session.getUserId()) {
            throw new ClientError.Unauthorized("user_id");
        } else if (entity.getCreatedAt().plusDays(DEFAULT_FIND_INACTIVE_DAYS).isBefore(LocalDateTime.now())) {
            throw new ClientError.Expired(String.format("expired %d days.", DEFAULT_FIND_INACTIVE_DAYS));
        }

        // result
        return DistributeStatusResult.builder()
                .data(new DistributeStatus(entity))
                .build();
    }
}
