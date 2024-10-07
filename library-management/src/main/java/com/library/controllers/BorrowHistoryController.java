// src/main/java/com/library/controllers/BorrowHistoryController.java
package com.library.controllers;

import com.library.models.*;
import com.library.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/borrow")
public class BorrowHistoryController {
    
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private BorrowHistoryService borrowHistoryService;

    // Borrow a book (Member only)
    @PostMapping("/borrow/{bookId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, Authentication authentication){
        String username = authentication.getName();
        User member = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Book> bookOpt = bookService.findById(bookId);
        if(bookOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }

        Book book = bookOpt.get();
        if(!book.getStatus().equals("AVAILABLE")){
            return ResponseEntity.badRequest().body("Book is not available for borrowing");
        }

        // Update book status
        book.setStatus("BORROWED");
        bookService.save(book);

        // Create borrow history
        BorrowHistory history = new BorrowHistory();
        history.setUser(member);
        history.setBook(book);
        history.setBorrowedAt(new Date());
        borrowHistoryService.save(history);

        return ResponseEntity.ok("Book borrowed successfully!");
    }

    // Return a book (Member only)
    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId, Authentication authentication){
        String username = authentication.getName();
        User member = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Book> bookOpt = bookService.findById(bookId);
        if(bookOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }

        Book book = bookOpt.get();
        if(!book.getStatus().equals("BORROWED")){
            return ResponseEntity.badRequest().body("Book is not currently borrowed");
        }

        // Find the latest borrow history without a return date
        List<BorrowHistory> histories = borrowHistoryService.findByUser(member);
        Optional<BorrowHistory> historyOpt = histories.stream()
                .filter(h -> h.getBook().getId().equals(bookId) && h.getReturnedAt() == null)
                .findFirst();

        if(historyOpt.isEmpty()){
            return ResponseEntity.badRequest().body("No borrow record found for this book");
        }

        BorrowHistory history = historyOpt.get();
        history.setReturnedAt(new Date());
        borrowHistoryService.save(history);

        // Update book status
        book.setStatus("AVAILABLE");
        bookService.save(book);

        return ResponseEntity.ok("Book returned successfully!");
    }

    // View own borrow history (Member only)
    @GetMapping("/history")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<BorrowHistory>> viewOwnHistory(Authentication authentication){
        String username = authentication.getName();
        User member = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        List<BorrowHistory> histories = borrowHistoryService.findByUser(member);
        return ResponseEntity.ok(histories);
    }

    // View all borrow histories (Librarian only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowHistory>> viewAllHistories(){
        List<BorrowHistory> histories = borrowHistoryService.findAll();
        return ResponseEntity.ok(histories);
    }
}
