package com.example.coinwallet.repository;

import com.example.coinwallet.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"transactions", "transactions.categories"})
    @Query("SELECT DISTINCT u FROM User u")
    List<User> findAllWithTransactionsAndCategories();
}