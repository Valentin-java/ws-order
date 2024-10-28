package com.workers.ws_order.bussines.bid.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getbid.BidSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface BidMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BidEntity toEntity(BidCreateRequestDto requestDto);

    @Mapping(target = "bidId", source = "id")
    BidCreateResponseDto toResponseDto(BidEntity bidEntity);

    @Mapping(target = "bidId", source = "id")
    BidSummaryDto toSummaryDto(BidEntity bidEntity);
}
