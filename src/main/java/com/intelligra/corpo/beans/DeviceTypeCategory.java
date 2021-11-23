package com.intelligra.corpo.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeviceTypeCategory implements Serializable {

    Integer id;
     String categoryName;
  String description;


}
