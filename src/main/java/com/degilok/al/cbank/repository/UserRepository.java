package com.degilok.al.cbank.repository;

import com.degilok.al.cbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    boolean existsByEmail(String email);

    boolean existsUserByName(String name);
}