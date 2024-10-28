package com.workers.ws_order.bussines.photofiles.interfaces;

import com.workers.ws_order.rest.inbound.dto.common.model.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderPhotoService {

    void uploadFiles(Long orderId, List<MultipartFile> files);

    void updateOrderPhotos(Long orderId, List<MultipartFile> photoData);

    List<FileDto> loadFiles(Long orderId);
}
