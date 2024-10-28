package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.photofiles.interfaces.OrderPhotoService;
import com.workers.ws_order.rest.inbound.dto.common.model.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/photo")
public class OrderPhotoController {

    private final OrderPhotoService service;

    @PostMapping("/{orderId}/upload")
    public ResponseEntity<Void> uploadFiles(@PathVariable Long orderId,
                                            @RequestParam("files") List<MultipartFile> files) {
        service.uploadFiles(orderId, files);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/update")
    public ResponseEntity<Void> updateOrderPhotos(@PathVariable Long orderId,
                                                  @RequestParam("files") List<MultipartFile> files) {
        service.updateOrderPhotos(orderId, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/load")
    public ResponseEntity<List<FileDto>> loadFiles(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.loadFiles(orderId));
    }
}
