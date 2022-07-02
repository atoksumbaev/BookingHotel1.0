package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.microservices.FileServiceFeign;
import com.example.bookinghotel.microservices.json.FileServiceResponse;
import com.example.bookinghotel.models.dtos.HotelDto;
import com.example.bookinghotel.models.dtos.ImageDto;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.services.FileService;
import com.example.bookinghotel.services.HotelService;
import com.example.bookinghotel.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ImageService imageService;

    @Override
    public ResponseEntity<?> uploadImageToHotel(MultipartFile file, Long hotelId, int orderNum) {
        HotelDto hotelDto = hotelService.findById(hotelId);
        if(hotelDto != null){
            ImageDto imageDto = new ImageDto();
            imageDto.setHotel(hotelDto);
            imageDto.setOrderNum(orderNum);
            try {
                FileServiceResponse response = fileServiceFeign.upload(file);
                imageDto.setLink(response.getDownloadUri());
                ImageDto saved = imageService.save(imageDto);


                return ResponseEntity.ok(saved);
            }catch (Exception e){
                System.out.println(e);
                return new ResponseEntity<>(Message.of("Не удалось сохранить фото"), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return new ResponseEntity<>(Message.of("Не удалось найти объект отеля"), HttpStatus.NOT_ACCEPTABLE);
    }
}
