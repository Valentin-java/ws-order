package com.workers.ws_order.bussines.bid.service;

import com.workers.ws_order.bussines.bid.interfaces.BidService;
import com.workers.ws_order.bussines.bid.mapper.BidMapper;
import com.workers.ws_order.bussines.message.interfaces.MessageService;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.persistance.enums.BidStatus;
import com.workers.ws_order.persistance.repository.BidRepository;
import com.workers.ws_order.rest.inbound.dto.acceptbid.BidChangeStatusRequest;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.createmsg.MessageCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.getbid.BidSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.workers.ws_order.persistance.enums.BidStatus.ACCEPTED;
import static com.workers.ws_order.persistance.enums.BidStatus.NEW;
import static com.workers.ws_order.persistance.enums.BidStatus.PENDING;
import static com.workers.ws_order.persistance.enums.BidStatus.REJECTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final MessageService messageService;
    private final BidMapper bidMapper;

    @Override
    @Transactional
    public BidCreateResponseDto createBid(BidCreateRequestDto requestDto) {
        log.info("Creating bid for order ID: {}", requestDto.orderId());

        if (Boolean.TRUE.equals(
                bidRepository.existsBidEntityBySpecialistIdAndOrderId(
                        requestDto.specialistId(), requestDto.orderId()))) {
            log.error("Bid already exists with order ID: {}", requestDto.orderId());
            throw new ResponseStatusException(BAD_REQUEST, "Bid already exists with order ID: " + requestDto.orderId());
        }


        BidEntity bidEntity = bidMapper.toEntity(requestDto);
        bidEntity.setStatus(NEW);
        bidEntity = bidRepository.save(bidEntity);

        messageService.sendMessage(bidEntity.getId(), new MessageCreateRequestDto(
                bidEntity.getSpecialistId(),
                requestDto.message()
        ));

        return bidMapper.toResponseDto(bidEntity);
    }

    /**
     * Метод для просмотра откликов на заказ для заказчика.
     * @param orderId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BidSummaryDto> getBidsByOrderId(Long orderId) {
        log.info("Fetching bids for order ID: {}", orderId);
        return bidRepository.findByOrderId(orderId)
                .stream()
                .map(bidMapper::toSummaryDto)
                .toList();
    }

    /**
     * Метод для просмотра заказов исполнителя.
     * @param specialistId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BidSummaryDto> getBidsBySpecialistId(Long specialistId) {
        log.info("Fetching bids for specialist ID: {}", specialistId);
        return bidRepository.findBySpecialistId(specialistId)
                .stream()
                .map(bidMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    public void acceptBid(BidChangeStatusRequest request) {
        BidEntity bid = findBid(request.orderId(), request.bidId());

        // Проверяем, есть ли уже принятый отклик на этот заказ
        BidEntity existingAcceptedBid = bidRepository.findFirstByOrderIdAndStatus(bid.getOrderId(), ACCEPTED);
        if (existingAcceptedBid != null) {
            existingAcceptedBid.setStatus(REJECTED);
            bidRepository.save(existingAcceptedBid);
        }

        // Принятие выбранного отклика
        bid.setStatus(ACCEPTED);
        bidRepository.save(bid);
    }

    @Override
    @Transactional
    public void rejectBid(BidChangeStatusRequest request) {
        BidEntity bid = findBid(request.orderId(), request.bidId());
        bid.setStatus(REJECTED);
        bidRepository.save(bid);
    }

    @Override
    @Transactional
    public void cancelBid(BidChangeStatusRequest request) {
        BidEntity bid = findBid(request.orderId(), request.bidId());

        bid.setStatus(BidStatus.CANCELLED);
        bidRepository.save(bid);
        log.info("Bid with ID: {} has been cancelled", request.orderId());
    }

    private BidEntity findBid(Long orderId, Long bidId) {
        return bidRepository.findBidEntityByOrderIdAndId(orderId, bidId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Bid not found with ID: " + bidId));
    }
}
