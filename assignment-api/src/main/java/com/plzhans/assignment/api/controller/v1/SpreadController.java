package com.plzhans.assignment.api.controller.v1;

import com.plzhans.assignment.api.auth.AuthRoomIdentity;
import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.service.spread.SpreadService;
import com.plzhans.assignment.api.service.spread.datatype.DistributeReceiveResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeRequest;
import com.plzhans.assignment.api.service.spread.datatype.DistributeResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeStatusResult;
import com.plzhans.assignment.common.controller.ControllerBase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 뿌리기 API
 */
@Validated
@RequestMapping(SpreadController.MAPPING_BASE)
@RestController
public class SpreadController extends ControllerBase {
    /**
     * The constant MAPPING_BASE.
     */
    public static final String MAPPING_BASE = "/v1/spray";
    /**
     * The constant MAPPING_DISTRIBUTE.
     */
    public static final String MAPPING_DISTRIBUTE = MAPPING_BASE + "/distribute";
    /**
     * The constant MAPPING_DISTRIBUTE_STATUS.
     */
    public static final String MAPPING_DISTRIBUTE_STATUS = MAPPING_BASE + "/distribute/status";
    /**
     * The constant MAPPING_RECEIVE.
     */
    public static final String MAPPING_RECEIVE = MAPPING_BASE + "/receive";

    private SpreadService spreadService;

    /**
     * Instantiates a new Spread controller.
     *
     * @param spreadService the spread service
     */
    public SpreadController(SpreadService spreadService) {
        this.spreadService = spreadService;
    }

    /**
     * 뿌리기
     *
     * @param requester the requester
     * @param request   the request
     * @return the distribute result
     */
    @PostMapping(MAPPING_DISTRIBUTE)
    public DistributeResult distribute(@AuthRoomIdentity AuthRoomRequester requester, DistributeRequest request) throws Exception {
        return this.spreadService.distribute(requester, request);
    }

    /**
     * 조회
     *
     * @param requester the requester
     * @param token     the token
     * @return the distribute status
     */
    @GetMapping(MAPPING_DISTRIBUTE_STATUS)
    public DistributeStatusResult getDistributeStatus(@AuthRoomIdentity AuthRoomRequester requester,
                                                      @RequestParam("token") String token) throws Exception {
        return this.spreadService.getDistributeStatusByToken(requester, token);
    }

    /**
     * 받기
     *
     * @param requester the requester
     * @param token     the token
     * @return the distribute receive result
     */
    @PostMapping(MAPPING_RECEIVE)
    public DistributeReceiveResult receive(@AuthRoomIdentity AuthRoomRequester requester,
                                           @RequestParam("token") String token) throws Exception {
        return this.spreadService.receiveByToken(requester, token);
    }
}
