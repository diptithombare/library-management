// src/main/java/com/library/services/BookService.java
package com.library.services;

import com.library.models.Book;
import com.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id){
        return bookRepository.findById(id);
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }

    public List<Book> findAvailableBooks(){
        return bookRepository.findAll().stream()
                .filter(book -> book.getStatus().equals("AVAILABLE"))
                .collect(Collectors.toList());
    }

    public void updateStatus(Long id, String status){
        bookRepository.findById(id).ifPresent(book -> {
            book.setStatus(status);
            bookRepository.save(book);
        });
    }

    // Additional methods as needed
}
