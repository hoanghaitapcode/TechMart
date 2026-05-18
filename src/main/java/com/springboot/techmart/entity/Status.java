package com.springboot.techmart.entity;

public enum Status {
    PENDING, PAID, SHIPPING, DELIVERED, CANCELLED;
    public boolean isCancellable(){
        // Chỉ cho phép hủy đơn hàng khi đang ở trạng thái PENDING.
        // Sau khi đã PAID (đã trừ tiền, chuẩn bị giao), không thể hủy tùy tiện.
        return this == PENDING;
    }
}
