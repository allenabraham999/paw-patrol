package com.simplogics.base.config;

import com.simplogics.base.entity.Role;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.service.IPasswordService;
import com.simplogics.base.service.IRoleService;
import com.simplogics.base.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer implements ApplicationRunner{

    @Autowired
    IUserService userService;

    @Autowired
    IPasswordService passwordService;

    @Autowired
    IRoleService roleService;

    @Value("${default.user.password}")
    private String defaultUserPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role role = roleService.findByRole(UserRole.ROLE_ADMIN);
        String encodedPassword = passwordService.validateAndGenerateEncodedPassword(defaultUserPassword);
        userService.createDefaultUsers(encodedPassword, role);
    }
}
