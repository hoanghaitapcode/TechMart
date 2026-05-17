package com.springboot.techmart.entity;

public enum Status {
    PENDING, PAID, SHIPPING, DELIVERED, CANCELLED;
    public boolean isCancellable(){
        if(this== SHIPPING|| this==CANCELLED){
            return false;
        }
        return true;
    }
}
