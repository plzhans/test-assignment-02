package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.service.spread.datatype.*;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import com.plzhans.assignment.common.error.ClientError;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SpreadServiceImpl implements SpreadService {


    public static final int DEFAULT_DISTRIBUTE_EXPIRED_SECONDS = 60 * 10;
    public static final int DEFAULT_FIND_INACTIVE_DAYS = 7;

    SpreadTokenGenerator spreadTokenGenerator;
    SpreadAmountGenerator spreadAmountGenerator;
    SpreadRepository spreadRepository;

    public SpreadServiceImpl(SpreadRepository spreadRepository) {
        this.spreadTokenGenerator = new SpreadTokenGenerator();
        this.spreadAmountGenerator = new DefaultSpreadAmountGenerator();
        this.spreadRepository = spreadRepository;
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

        // result
        return DistributeResult.builder()
                .token(entity.getToken())
                .createdDate(entity.getCreatedAt())
                .expiredDate(entity.getExpiredDate())
                .build();
    }

    @Transactional
    @Override
    public DistributeReceiveResult receiveByToken(AuthRoomRequester session, String token) throws Exception {

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
                .filter(x -> x.isReady())
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
    }

    @Override
    public DistributeStatusResult getDistributeStatusByToken(AuthRoomRequester session, String token){

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
