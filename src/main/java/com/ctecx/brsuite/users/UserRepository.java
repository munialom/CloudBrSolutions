package com.ctecx.brsuite.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends CrudRepository<User , Integer> {
    @Query("SELECT u FROM User u WHERE u.email=:email")
    User getUserByEmail(@Param("email") String email);

    @Query("SELECT u.firstLogin FROM User u WHERE u.email = :email")
    Boolean isFirstLogin(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);
    @Query("UPDATE User u SET u.enabled = true WHERE u.id = ?1")
    @Modifying
    void enable(Integer id);

    User findFirstByFirstLogin(boolean firstLogin);




    User findFirstByEmailAndFirstLogin(String email, boolean b);

    User getUserByPin(String pin);
}
