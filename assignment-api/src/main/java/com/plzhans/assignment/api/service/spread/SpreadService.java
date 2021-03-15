package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.service.spread.datatype.*;

public interface SpreadService {
    DistributeResult distribute(AuthRoomRequester requester, DistributeRequest request) throws Exception;

    DistributeStatusResult getDistributeStatusByToken(AuthRoomRequester requester, String token) throws Exception;

    DistributeReceiveResult receiveByToken(AuthRoomRequester requester, String token) throws Exception;
}
