package com.simplogics.base.controller;

import com.simplogics.base.security.authDetails.UserAuthDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    public static UserAuthDetails getUserDetailsByPrincipal(){
        return (UserAuthDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
