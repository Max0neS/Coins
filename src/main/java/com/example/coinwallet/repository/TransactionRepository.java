package com.example.coinwallet.repository;

import com.example.coinwallet.model.Transaction;
import com.example.coinwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.user LEFT JOIN FETCH t.categories")
    List<Transaction> findAllWithUserAndCategories();

    @Query("""
            SELECT DISTINCT t
            FROM Transaction t
            JOIN t.categories c
            WHERE t.id IN (
            SELECT t2.id FROM Transaction t2 JOIN t2.categories c2
            WHERE c2.id IN :categoryIds
            GROUP BY t2.id
            HAVING COUNT(DISTINCT c2.id) = :categoryCount
            ) AND (
            SELECT COUNT(c3) FROM t.categories c3
            ) = :categoryCount
            ORDER BY t.id
                        """)
    List<Transaction> findByCategoryIds(@org.springframework.data.repository.query.Param("categoryIds") List<Long> categoryIds,
                                        @org.springframework.data.repository.query.Param("categoryCount") Long categoryCount);
}

/*
    @Query(value = "SELECT DISTINCT t.* FROM transactions t " +
                   "JOIN transaction_category tc ON t.id = tc.transaction_id " +
                   "WHERE t.id IN (" +
                   "SELECT t2.id FROM transactions t2 " +
                   "JOIN transaction_category tc2 ON t2.id = tc2.transaction_id " +
                   "WHERE tc2.category_id IN (:categoryIds) " +
                   "GROUP BY t2.id " +
                   "HAVING COUNT(DISTINCT tc2.category_id) = :categoryCount" +
                   ") AND (" +
                   "SELECT COUNT(*) FROM transaction_category tc3 WHERE tc3.transaction_id = t.id" +
                   ") = :categoryCount " +
                   "ORDER BY t.id", nativeQuery = true)
    List<Transaction> findByCategoryIdsNative(@org.springframework.data.repository.query.Param("categoryIds") List<Long> categoryIds,
                                             @org.springframework.data.repository.query.Param("categoryCount") Long categoryCount);
    */