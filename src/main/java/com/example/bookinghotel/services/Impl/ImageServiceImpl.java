package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.dao.ImageDao;
import com.example.bookinghotel.mappers.ImageMapper;
import com.example.bookinghotel.models.dtos.ImageDto;
import com.example.bookinghotel.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;
    private ImageMapper imageMapper = ImageMapper.INSTANCE;

    @Override
    public ImageDto save(ImageDto imageDto) {
        return imageMapper.toDto(imageDao.save(imageMapper.toEntity(imageDto)));
    }
}
