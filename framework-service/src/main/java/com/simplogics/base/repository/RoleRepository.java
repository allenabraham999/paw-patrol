package com.simplogics.base.repository;

import com.simplogics.base.entity.Role;
import com.simplogics.base.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(UserRole role);

}
