package com.example.bookinghotel.models.response;

import com.example.bookinghotel.models.enums.EBedType;
import com.example.bookinghotel.models.enums.ETypeOfView;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@Builder
public class RoomFilterResponse {

    private Long id;
    int capacity;
    EBedType bedType;
    float square;
    boolean wifi;
    ETypeOfView typeOfView;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    float totalSum;
}
