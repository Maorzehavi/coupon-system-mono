package com.maorzehavi.couponSystem.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @NotBlank(message = "last name is mandatory")
    private String lastName;

}
