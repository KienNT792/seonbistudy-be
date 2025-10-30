package com.seonbistudy.seonbistudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seonbistudy.seonbistudy.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
