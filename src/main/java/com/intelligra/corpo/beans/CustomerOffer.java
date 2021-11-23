package com.intelligra.corpo.beans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOffer implements Serializable {

    DeviceType specs;
    List<DevicePlans> plans;
}
