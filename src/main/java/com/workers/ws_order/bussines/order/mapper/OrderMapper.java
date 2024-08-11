package com.workers.ws_order.bussines.order.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto requestDto);

    OrderCreateResponseDto toResponseDto(OrderEntity orderEntity);

    @Mapping(source = "id", target = "orderId")
    OrderSummaryDto toSummaryDto(OrderEntity orderEntity);

    @Mapping(target = "photos", ignore = true)  // Фотографии будут обновляться отдельно
    void updateOrderFromDto(OrderUpdateRequestDto dto, @MappingTarget OrderEntity entity);
}
