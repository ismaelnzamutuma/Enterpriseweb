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
public class Corporates implements Serializable {

    Integer corporateId;
    String name;
    String tinNumber;
    String description;
    String email;
    String website;
    String msisdn;
    boolean status;


}
