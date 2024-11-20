package com.simplogics.base.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String name;
    private String password;
    private String phoneNo;
    //TODO: try writing a annotation to catch if it's from a different role
    private String role;
}
