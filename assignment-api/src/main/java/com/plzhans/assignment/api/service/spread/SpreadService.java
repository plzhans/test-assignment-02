package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.service.spread.datatype.*;

public interface SpreadService {
    DistributeResult distribute(AuthRoomRequester requester, DistributeParam param) throws Exception;

    DistributeStatusResult getDistributeStatusByToken(AuthRoomRequester param, String token) throws Exception;

    DistributeReceiveResult receiveByToken(AuthRoomRequester param, String token) throws Exception;
}
