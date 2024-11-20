package com.simplogics.base.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Utils {

    public static SecretKey getSignInKey(String secret){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //TODO: Add general utils for your application here

}
