package com.eagle_bank_api.repository;


import com.eagle_bank_api.model.entity.BankAccount;
import com.eagle_bank_api.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findByAccount(BankAccount account, Pageable pageable);


}
