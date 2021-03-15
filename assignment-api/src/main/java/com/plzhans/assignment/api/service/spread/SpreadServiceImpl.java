package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.service.spread.datatype.*;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SpreadServiceImpl implements SpreadService {


    static final int DEFAULT_DISTRIBUTE_EXPIRED_SECONDS = 60 * 10;
    static final int DEFAULT_FIND_INACTIVE_DAYS = 7;

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
    public DistributeResult distribute(AuthRoomRequester requester, DistributeRequest request) throws Exception {

        // entity
        val entity = SpreadEventEntity.builder()
                .state(SpreadState.Active)
                .token(spreadTokenGenerator.nextToken())
                .userId(requester.getUserId())
                .roomId(requester.getRoomId())
                .totalAmount(request.getAmount())
                .receiverCount(request.getReceiverCount())
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

    @Override
    public DistributeStatusResult getDistributeStatusByToken(AuthRoomRequester requester, String token) throws Exception {

        val entity = this.spreadRepository.findByRoomIdAndToken(requester.getRoomId(), token);
        if (entity == null) {
            throw new Exception("spreadRepository.findByRoomIdAndToken");
        } else if (entity.getUserId() != requester.getUserId()) {
            throw new Exception("invalid requester");
        } else if (entity.getCreatedAt().plusDays(DEFAULT_FIND_INACTIVE_DAYS).isBefore(LocalDateTime.now())) {
            throw new Exception("7 day");
        }

        // result
        return DistributeStatusResult.builder()
                .data(new DistributeStatusDto(entity))
                .build();
    }

    @Override
    public DistributeReceiveResult receiveByToken(AuthRoomRequester requester, String token) throws Exception {

        val entity = this.spreadRepository.findByRoomIdAndToken(requester.getRoomId(), token);
        val amounts = entity.getAmounts();
        if (amounts == null) {
            throw new Exception("invalid amounts");
        }

        //
        if(entity.getExpiredDate().isBefore(LocalDateTime.now())){
            throw new Exception("expired date");
        }

        // find amount
        if (amounts.stream().anyMatch(x -> x.getReceiverId() == requester.getUserId())) {
            throw new Exception("already receiver");
        }

        val amountEntity = amounts.stream()
                .filter(x -> x.isReady())
                .findFirst().orElseThrow(() -> new Exception(""));

        // set received
        amountEntity.setReceived(requester.getUserId());

        // repository save
        this.spreadRepository.save(entity);

        return DistributeReceiveResult.builder()
                .amount(amountEntity.getAmount())
                .build();
    }
}
