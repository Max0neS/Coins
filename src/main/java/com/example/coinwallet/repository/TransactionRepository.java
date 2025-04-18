package com.example.coinwallet.repository;

import com.example.coinwallet.model.Transaction;
import com.example.coinwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.user LEFT JOIN FETCH t.categories")

    List<Transaction> findAllWithUserAndCategories();

    // JPQL-запрос: фильтрация транзакций по userId и типу
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    List<Transaction> findByUserIdAndTypeJPQL(@Param("userId") Long userId, @Param("type") boolean type);

    // Native SQL-запрос: фильтрация транзакций по userId и типу
    @Query(value = "SELECT t.* FROM transactions t JOIN users u ON t.user_id = u.id WHERE u.id = :userId AND t.type = :type", nativeQuery = true)
    List<Transaction> findByUserIdAndTypeNative(@Param("userId") Long userId, @Param("type") boolean type);
}