package com.intelligra.corpo.beans;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class DeviceType implements Serializable {

    Integer deviceTypeId;

    String type_name;//= models.CharField(max_length=100)

    String description;//= models.CharField(max_length=400)


    String dimension; //= models.CharField(max_length=50, blank=True, null=True)

    String chipset; // = models.CharField(max_length=50, blank=True, null=True)

    String os_ui; // = models.CharField(max_length=50, choices=OS_CHOICES)

    String network;// = models.CharField(max_length=50, blank=True, null=True)

    String display;//= models.CharField(max_length=50, blank=True, null=True)

    String screen_size; //  = models.CharField(max_length=20, blank=True, null=True)
    String memory; //= models.CharField(max_length=20, blank=True, null=True)

    String internal_storage; // = models.CharField(max_length=20, blank=True, null=True)

    String camera;// = models.CharField(max_length=20, blank=True, null=True)

    String battery;// = models.CharField(max_length=50, blank=True, null=True)

    String specs;// = models.CharField(max_length=500, blank=True, null=True)

    double full_price;//= models.CharField(max_length=50, blank=True, null=True)

    Date created_on;//= models.DateTimeField(default=datetime.now)

    Date updated_on;// = models.DateTimeField(default=datetime.now)
    Integer devicetypecategory;

    //  @OneToMany(fetch =FetchType.LAZY,mappedBy = "deviceType")

    List<DeviceTypePhotos> photos;

}
