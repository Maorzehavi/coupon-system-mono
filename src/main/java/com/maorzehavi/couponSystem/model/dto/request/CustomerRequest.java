package com.maorzehavi.couponSystem.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "name is mandatory")
    private String fullName;
    @NonNull
    private UserRequest user;
}
