package com.intelligra.corpo.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Entity
//@Table(name = "device_type_photos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeviceTypePhotos implements Serializable {

    String phototype;
    String description;
  String encodedImage;



}