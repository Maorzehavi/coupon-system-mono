package com.maorzehavi.couponSystem.model.dto.request;

import com.maorzehavi.couponSystem.model.ClientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {
    @NotBlank(message = "role is mandatory")
    private String role;
    @NotNull
    private ClientType clientType;
    @NotNull
    private Set<AuthorityRequest> authorities;
}
