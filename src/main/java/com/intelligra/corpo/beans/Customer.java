package com.intelligra.corpo.beans;

import lombok.*;


import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Customer implements Serializable {

    int customer_id;//= models.UUIDField(unique=True)

    String surname;//= models.CharField(max_length=50, null=True)

    String name;//= models.CharField(max_length=50, null=True)

    String msisdn;// = models.CharField(max_length=10, blank=False, null=False, unique=True)

    String address;// = models.CharField(max_length=300, blank=True, null=True)

    String gender;// = models.CharField(max_length=20, choices=GENDER_CHOICES)

    String martial_status;//= models.CharField(max_length=20, choices=MARTIAL_STATUS_CHOICES)

    String national_id_number;// = models.CharField(max_length=30)

    String identification_type;// = models.CharField(max_length=20, choices=NATIONAL_IDENTIFICATION_CHOICES)

    String province;//= models.CharField(max_length=40, blank=True, null=True)

    String district;//= models.CharField(max_length=40, blank=True, null=True)

    String sector;//= models.CharField(max_length=40, blank=True, null=True)

    String cell;// = models.CharField(max_length=40, blank=True, null=True)

    String village;// = models.CharField(max_length=40, blank=True, null=True)

    Date created_at;// = models.DateTimeField(auto_now_add=True)

    Date modified_at;// = models.DateTimeField(auto_now=True)

    String email;
    boolean blocked; //active, blocked,   //=models.CharField(max_length=10,choices=USER_STATUSES)
     // Aboarded,Pre-customer,Active
    String customerType;

    Date dob;

    String language;

    String bankId;

}
