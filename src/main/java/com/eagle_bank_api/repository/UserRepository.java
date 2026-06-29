package com.eagle_bank_api.repository;

import com.eagle_bank_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}