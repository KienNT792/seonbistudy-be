package com.seonbistudy.seonbistudy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seonbistudy.seonbistudy.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(Long accountId);
    Optional<User> findByAccount_Email(String email);
    Optional<User> findByAccount_Username(String username);
}
