package com.example.bookinghotel.mappers;

import com.example.bookinghotel.models.dtos.ImageDto;
import com.example.bookinghotel.models.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageMapper extends BaseMapper<Image, ImageDto> {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);
}
