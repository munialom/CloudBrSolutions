package com.ctecx.brsuite.userroles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role ,Integer> {

    //List<Role> findByRoleName(String roleName);
    Set<Role> findByRoleName(String roleName);
}
