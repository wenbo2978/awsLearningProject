package com.aws_practice.mapper;

import com.aws_practice.dto.OrderSummaryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderQueryMapper {
    List<OrderSummaryDto> findOrderSummaryByUserId(@Param("userId") Long userId);
}
