package com.simplogics.base.service;

import com.simplogics.base.exception.PawException;

public interface IPasswordService {

    String validateAndGenerateEncodedPassword(String password) throws PawException;

}
