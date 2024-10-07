// src/main/java/com/library/repositories/BorrowHistoryRepository.java
package com.library.repositories;

import com.library.models.BorrowHistory;
import com.library.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
    List<BorrowHistory> findByUser(User user);
    List<BorrowHistory> findAll();
}
