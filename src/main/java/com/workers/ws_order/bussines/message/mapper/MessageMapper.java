package com.workers.ws_order.bussines.message.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.MessageEntity;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getmsg.MessageSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "bidId", ignore = true)
    MessageEntity toEntity(MessageCreateRequestDto requestDto);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "bidId", source = "bidId")
    @Mapping(target = "timestamp", source = "sentAt")
    MessageCreateResponseDto toResponseDto(MessageEntity messageEntity);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "timestamp", source = "sentAt")
    MessageSummaryDto toSummaryDto(MessageEntity messageEntity);

}
