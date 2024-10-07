// src/main/java/com/library/controllers/BookController.java
package com.library.controllers;

import com.library.models.Book;
import com.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookService bookService;

    // Get all books (both AVAILABLE and BORROWED)
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.findAll());
    }

    // Add a new book (Librarian only)
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book savedBook = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // Update an existing book (Librarian only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book bookDetails){
        return bookService.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setStatus(bookDetails.getStatus());
            bookService.save(book);
            return ResponseEntity.ok("Book updated successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found"));
    }

    // Delete a book (Librarian only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        return bookService.findById(id).map(book -> {
            bookService.delete(id);
            return ResponseEntity.ok("Book deleted successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found"));
    }

    // Get available books (Members can view available books)
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks(){
        return ResponseEntity.ok(bookService.findAvailableBooks());
    }
}
