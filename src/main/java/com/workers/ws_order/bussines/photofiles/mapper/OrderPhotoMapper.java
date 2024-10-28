package com.workers.ws_order.bussines.photofiles.mapper;

import com.workers.ws_order.config.mapper.MapperConfiguration;
import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import com.workers.ws_order.rest.inbound.dto.common.model.FileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(config = MapperConfiguration.class)
public interface OrderPhotoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "photoData", source = "source", qualifiedByName = "getPhotoData")
    @Mapping(target = "fileName", source = "source.name")
    @Mapping(target = "originalName", source = "source.originalFilename")
    @Mapping(target = "fileType", source = "source.contentType")
    OrderPhotoEntity toEntity(MultipartFile source, Long orderId);

    @Mapping(target = "bytes", source = "photoData")
    @Mapping(target = "name", source = "fileName")
    @Mapping(target = "originalFilename", source = "originalName")
    @Mapping(target = "contentType", source = "fileType")
    FileDto toDomain(OrderPhotoEntity source);

    @Named("getPhotoData")
    default byte[] getPhotoData(MultipartFile source) {
        try {
            return source.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read bytes from MultipartFile", e);
        }
    }
}
