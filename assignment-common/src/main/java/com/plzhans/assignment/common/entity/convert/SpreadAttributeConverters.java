package com.plzhans.assignment.common.entity.convert;

import com.plzhans.assignment.common.config.CodeEnumableAttributeConverter;
import com.plzhans.assignment.common.domain.spread.SpreadAmountState;
import com.plzhans.assignment.common.domain.spread.SpreadState;

public class SpreadAttributeConverters {
    public static class _SpreadState extends CodeEnumableAttributeConverter<SpreadState> {}
    public static class _SpreadAmountState extends CodeEnumableAttributeConverter<SpreadAmountState> {}
}
