package com.intelligra.corpo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DevicePlans implements Serializable {

    Integer id;
    double initDeposit;
    double periodicity;
    int bandleMinutes;
    int bandleData;
    int bandleSms;
    double financedAmount;
    double interestRate;
    double dailyPayment;
    double weeklyPayment;
    double monthlyPayment;
    double lowerLimit;
    double upperLimit;
    String productType;
    @JsonIgnore
    String productName; // this is for bandle provisionning and it is provided by MTN
}
