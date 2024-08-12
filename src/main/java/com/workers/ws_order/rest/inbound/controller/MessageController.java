package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.message.interfaces.MessageService;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.getmsg.MessageSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/bid/{bidId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long bidId, @RequestBody MessageCreateRequestDto requestDto) {
        return ResponseEntity.ok(messageService.sendMessage(bidId, requestDto));
    }

    @GetMapping("/bid/{bidId}")
    public ResponseEntity<List<MessageSummaryDto>> getMessagesByBidId(@PathVariable Long bidId) {
        return ResponseEntity.ok(messageService.getMessagesByBidId(bidId));
    }
}
