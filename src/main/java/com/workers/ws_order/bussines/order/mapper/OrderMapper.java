package com.workers.ws_order.bussines.order.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "bids", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto requestDto);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "photoData", source = "photos", qualifiedByName = "mapPhotosToPhotoData")
    OrderCreateResponseDto toResponseDto(OrderEntity orderEntity);

    @Mapping(source = "id", target = "orderId")
    OrderSummaryDto toSummaryDto(OrderEntity orderEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bids", ignore = true)
    @Mapping(target = "photos", ignore = true)  // Фотографии будут обновляться отдельно
    void updateOrderFromDto(OrderUpdateRequestDto dto, @MappingTarget OrderEntity entity);

    @Named("mapPhotosToPhotoData")
    default List<byte[]> mapPhotosToPhotoData(List<OrderPhotoEntity> photos) {
        return photos.stream()
                .map(OrderPhotoEntity::getPhotoData)
                .toList();
    }
}
