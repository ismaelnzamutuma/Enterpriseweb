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

public class CorporateMember implements Serializable {

    int memberId;

    String surname;//= models.CharField(max_length=50, null=True)

    String name;//= models.CharField(max_length=50, null=True)

    String msisdn;// = models.CharField(max_length=10, blank=False, null=False, unique=True)

    String address;// = models.CharField(max_length=300, blank=True, null=True)

    String gender;// = models.CharField(max_length=20, choices=GENDER_CHOICES)

    String martial_status;//= models.CharField(max_length=20, choices=MARTIAL_STATUS_CHOICES)

    String national_id_number;// = models.CharField(max_length=30)
    String memberEmail;
    String identification_type;// = models.CharField(max_length=20, choices=NATIONAL_IDENTIFICATION_CHOICES)
    Integer corporateId;
    String status;
     Corporates corporate;
}
