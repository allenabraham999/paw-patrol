package com.simplogics.base.service;

import com.simplogics.base.exception.PawException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordService implements IPasswordService{

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private static final String REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";

    @Override
    public String validateAndGenerateEncodedPassword(String password) throws PawException {
        if(!isValidPassword(password)){
            throw new PawException("validation.password.invalid", HttpStatus.BAD_REQUEST);
        }
        return passwordEncoder.encode(password);
    }

    public boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
