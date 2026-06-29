package com.eagle_bank_api.repository;


import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByUser(User user);
}