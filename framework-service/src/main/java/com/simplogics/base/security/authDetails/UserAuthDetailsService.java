package com.simplogics.base.security.authDetails;

import com.simplogics.base.entity.Role;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.service.IUserService;
import com.simplogics.base.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserAuthDetailsService implements UserDetailsService {

    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findUserByUsername(username);
        } catch (PawException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        return new UserAuthDetails(user.getId(), user.getEmail(), user.getPassword(),getAuthorities(user.getUserRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .toList();
        return new ArrayList<>(authorities);
    }

}
