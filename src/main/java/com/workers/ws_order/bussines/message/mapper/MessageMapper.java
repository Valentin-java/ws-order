package com.workers.ws_order.bussines.message.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.MessageEntity;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getmsg.MessageSummaryDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface MessageMapper {

    MessageEntity toEntity(MessageCreateRequestDto requestDto);

    MessageCreateResponseDto toResponseDto(MessageEntity messageEntity);

    MessageSummaryDto toSummaryDto(MessageEntity messageEntity);

}
