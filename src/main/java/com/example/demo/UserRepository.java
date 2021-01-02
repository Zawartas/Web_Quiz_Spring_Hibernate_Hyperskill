package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email = :email")
    User getUserByUsername(@Param("email") String email); //obsolete because of findUserByEmail

    User findUserByEmail(String email); //automatically created

    boolean existsByEmail(String email); //automatically created
}
