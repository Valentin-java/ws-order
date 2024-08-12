package com.workers.ws_order.bussines.message.service;

import com.workers.ws_order.bussines.message.interfaces.MessageService;
import com.workers.ws_order.bussines.message.mapper.MessageMapper;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.persistance.entity.MessageEntity;
import com.workers.ws_order.persistance.repository.BidRepository;
import com.workers.ws_order.persistance.repository.MessageRepository;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getmsg.MessageSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final BidRepository bidRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageCreateResponseDto sendMessage(Long bidId, MessageCreateRequestDto requestDto) {
        BidEntity bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Bid not found with ID: " + bidId));

        MessageEntity messageEntity = messageMapper.toEntity(requestDto);
        messageEntity.setBid(bid);
        messageEntity = messageRepository.save(messageEntity);

        return messageMapper.toResponseDto(messageEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageSummaryDto> getMessagesByBidId(Long bidId) {
        BidEntity bid = findBidById(bidId);

        return messageRepository.findByBid(bid)
                .stream()
                .map(messageMapper::toSummaryDto)
                .toList();
    }

    private BidEntity findBidById(Long bidId) {
        return bidRepository.findById(bidId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Bid not found with ID: " + bidId));
    }
}
