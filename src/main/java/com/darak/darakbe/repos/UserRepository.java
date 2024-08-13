package com.darak.darakbe.repos;

import com.darak.darakbe.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByEmail(String email);
    Optional<Object> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.phoneNumber IS NULL AND u.userStatus = 'active'")
    Page<User> findActiveUsersWithPhoneNumber(Pageable pageable);
}

