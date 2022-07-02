package com.example.bookinghotel.models.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Table(name = "image")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {

    @Id
    @GeneratedValue
    Long id;
    String link;
    int orderNum;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;
}
