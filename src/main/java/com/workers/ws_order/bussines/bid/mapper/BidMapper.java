package com.workers.ws_order.bussines.bid.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getbid.BidSummaryDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface BidMapper {

    BidEntity toEntity(BidCreateRequestDto requestDto);

    BidCreateResponseDto toResponseDto(BidEntity bidEntity);

    BidSummaryDto toSummaryDto(BidEntity bidEntity);
}
