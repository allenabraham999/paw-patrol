package com.simplogics.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "validation.token.empty")
    private String token;

    @NotBlank(message = "validation.password.empty")
    private String newPassword;

}
