package com.plzhans.assignment.api.service.spread.datatype;

import lombok.Builder;
import lombok.Getter;

/**
 * The type Distribute status result.
 */
@Builder
@Getter
public class DistributeStatusResult {
    /**
     * 상태 정보
     */
    DistributeStatusDto data;

}
