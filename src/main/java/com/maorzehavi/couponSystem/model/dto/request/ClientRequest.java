package com.maorzehavi.couponSystem.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequest <T>{
    @NotNull
    private T data;
    @NotNull
    private UserRequest user;


}
