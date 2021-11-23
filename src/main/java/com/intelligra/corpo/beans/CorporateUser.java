package com.intelligra.corpo.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CorporateUser implements Serializable {

   int  corporateuserId;

    String name;
    String pwd;
    String surname;

    String username;
    String msisdn;
    String userstatus; //active, inactive,blocked
    String email;
    int corporateId;
     private Corporates corporate;


}
