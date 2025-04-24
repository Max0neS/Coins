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

    // MODIFIED: Added JOIN FETCH to load categories eagerly
    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.categories WHERE t.user.id = :userId")
    List<Transaction> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

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