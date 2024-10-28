package com.workers.ws_order.bussines.photofiles.service;

import com.workers.ws_order.bussines.photofiles.interfaces.OrderPhotoService;
import com.workers.ws_order.bussines.photofiles.mapper.OrderPhotoMapper;
import com.workers.ws_order.persistance.repository.OrderPhotoRepository;
import com.workers.ws_order.rest.inbound.dto.common.model.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPhotoServiceImpl implements OrderPhotoService {

    private final OrderPhotoMapper orderPhotoMapper;
    private final OrderPhotoRepository orderPhotoRepository;

    /**
     * Сохранение фотографий заказа
     * @param orderId
     * @param files
     */
    @Override
    @Transactional
    public void uploadFiles(Long orderId, List<MultipartFile> files) {
        if (CollectionUtils.isNotEmpty(files)) {
            saveOrderPhotos(files, orderId);
        }
    }

    /**
     * Загрузка фотографий заказа
     * @param orderId
     */
    @Override
    @Transactional(readOnly = true)
    public List<FileDto> loadFiles(Long orderId) {
        return Optional.ofNullable(orderPhotoRepository.findByOrderId(orderId))
                .map(photoList -> photoList.stream().map(orderPhotoMapper::toDomain).toList())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Photo not found with ID: " + orderId));
    }

    /**
     * Замена списка фотографий на те, что пришли из update.
     * @param photoData
     */
    @Override
    @Transactional
    public void updateOrderPhotos(Long orderId, List<MultipartFile> photoData) {
        orderPhotoRepository.deleteAllByOrderId(orderId);
        saveOrderPhotos(photoData, orderId);
    }

    private void saveOrderPhotos(List<MultipartFile> photoList, Long orderId) {
        photoList.forEach(photoFile -> {
            var photoEntity = orderPhotoMapper.toEntity(photoFile, orderId);
            orderPhotoRepository.save(photoEntity);
        });
    }
}
