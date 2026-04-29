package com.springboot.techmart.dto.request;


import com.springboot.techmart.entity.Type;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class WalletActionRequest {

    @NotNull
    @DecimalMin(value = "1000", message = "Số tiền giao dịch tối thiểu là 1000 VND")
    private BigDecimal amount;
    private Type type;
}
