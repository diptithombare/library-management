// src/main/java/com/library/models/BorrowHistory.java
package com.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "borrow_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many borrow histories can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Many borrow histories can belong to one book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date borrowedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnedAt;
}
