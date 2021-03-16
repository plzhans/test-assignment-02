package com.plzhans.assignment.api.controller.spread;

import com.plzhans.assignment.api.auth.AuthRoomIdentity;
import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.controller.common.ControllerBase;
import com.plzhans.assignment.api.service.spread.SpreadService;
import com.plzhans.assignment.api.service.spread.datatype.DistributeParam;
import com.plzhans.assignment.api.service.spread.datatype.DistributeReceiveResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeStatusResult;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 뿌리기 API
 */
@Validated
@RequestMapping(SpreadController.BASE_PATH)
@RestController
public class SpreadController extends ControllerBase {
    /**
     * The constant MAPPING_BASE.
     */
    public static final String BASE_PATH = "/v1";

    /**
     * The Spread service.
     */
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
     * The constant MAPPING_DISTRIBUTE.
     */
    public static final String MAPPING_DISTRIBUTE = "/spray/distribute";

    /**
     * The constant MAPPING_DISTRIBUTE_STATUS.
     */
    public static final String MAPPING_DISTRIBUTE_STATUS = "/spray/distribute/status";

    /**
     * The constant MAPPING_RECEIVE.
     */
    public static final String MAPPING_RECEIVE = "/spray/receive";

    /**
     * 뿌리기
     *
     * @param requester the requester
     * @param request   the request
     * @return the distribute result
     * @throws Exception the exception
     */
    @PostMapping(MAPPING_DISTRIBUTE)
    public DistributeResult distribute(@AuthRoomIdentity AuthRoomRequester requester, @Valid @RequestBody DistributeParam request) throws Exception {
        val result = this.spreadService.distribute(requester, request);
        return result;
    }

    /**
     * 조회
     *
     * @param requester the requester
     * @param token     the token
     * @return the distribute status
     * @throws Exception the exception
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
     * @throws Exception the exception
     */
    @PostMapping(MAPPING_RECEIVE)
    public DistributeReceiveResult receive(@AuthRoomIdentity AuthRoomRequester requester,
                                           @RequestParam("token") String token) throws Exception {
        return this.spreadService.receiveByToken(requester, token);
    }
}
