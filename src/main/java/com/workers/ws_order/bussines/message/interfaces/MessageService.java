package com.workers.ws_order.bussines.message.interfaces;

import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getmsg.MessageSummaryDto;

import java.util.List;

public interface MessageService {

    MessageCreateResponseDto sendMessage(Long bidId, MessageCreateRequestDto requestDto);

    List<MessageSummaryDto> getMessagesByBidId(Long bidId);
}
