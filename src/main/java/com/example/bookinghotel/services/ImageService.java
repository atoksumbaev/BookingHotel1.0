package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.ImageDto;
import com.example.bookinghotel.models.entities.Image;

public interface ImageService {

    ImageDto save(ImageDto imageDto);
}
