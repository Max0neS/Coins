package com.example.coinwallet.repository;

import com.example.coinwallet.model.Transaction;
import com.example.coinwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUser_Id(Long userId);
}