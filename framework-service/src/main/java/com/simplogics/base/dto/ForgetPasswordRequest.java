package com.simplogics.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequest {

    @NotBlank(message = "validation.email.empty")
    private String email;

}
