package com.maorzehavi.couponSystem.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityRequest {
    @NotBlank(message = "Authority is mandatory")
    private String authority;
}
