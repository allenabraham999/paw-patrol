package com.simplogics.base.service;

import com.simplogics.base.entity.Role;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.exception.PawException;

public interface IRoleService {

    Role findByRole(UserRole role) throws PawException;

}
