package com.example.bookinghotel.models.request;

import com.example.bookinghotel.models.enums.EBedType;
import com.example.bookinghotel.models.enums.EStatusBooking;
import com.example.bookinghotel.models.enums.ETypeOfView;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSaveWithPrice {

    int capacity;
    EBedType bedType;
    float square;
    boolean wifi;
    Long hotelId;
    ETypeOfView typeOfView;
    EStatusBooking statusBooking;
    float price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDate endDate;
}
