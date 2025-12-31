package com.in.mzncvmc.common.auth.controller;

import com.in.mzncvmc.content.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Users, Long> {

    @Modifying
    @Query("""
        UPDATE Users u
           SET u.password = :password
               , u.pwNotifyDuration = '999'
         WHERE u.username = :username
    """)
    int resetPassword(
            @Param("username") String username,
            @Param("password") String password
    );
}
