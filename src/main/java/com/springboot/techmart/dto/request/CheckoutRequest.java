package com.springboot.techmart.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Update sau khi hoàn thiện mainflow
public class CheckoutRequest {
    private String shippingAdress;
    private String phoneNumber;
    private String paymentMethod;
    private String note;
}
