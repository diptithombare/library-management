// src/main/java/com/library/services/BorrowHistoryService.java
package com.library.services;

import com.library.models.BorrowHistory;
import com.library.models.User;
import com.library.repositories.BorrowHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowHistoryService {
    
    @Autowired
    private BorrowHistoryRepository borrowHistoryRepository;

    public BorrowHistory save(BorrowHistory history){
        return borrowHistoryRepository.save(history);
    }

    public List<BorrowHistory> findByUser(User user){
        return borrowHistoryRepository.findByUser(user);
    }

    public List<BorrowHistory> findAll(){
        return borrowHistoryRepository.findAll();
    }

    // Additional methods as needed
}
