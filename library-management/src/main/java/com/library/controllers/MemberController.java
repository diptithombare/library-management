// src/main/java/com/library/controllers/MemberController.java
package com.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.models.User;
import com.library.services.UserService;

@RestController
@RequestMapping("/members")
public class MemberController {
    
    @Autowired
    private UserService userService;

    // Add a new member (Librarian only)
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> addMember(@RequestBody User member){
        if(userService.findByUsername(member.getUsername()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if(!member.getRole().equals("MEMBER")){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Role must be MEMBER");
        }

        member.setPassword(new BCryptPasswordEncoder().encode(member.getPassword()));
        member.setActive(true);
        userService.save(member);
        return ResponseEntity.status(HttpStatus.CREATED).body("Member added successfully!");
    }

    // Update a member's information (Librarian only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody User memberDetails){
        return userService.findById(id).map(member -> {
            member.setUsername(memberDetails.getUsername());
            if(memberDetails.getPassword() != null && !memberDetails.getPassword().isEmpty()){
                member.setPassword(new BCryptPasswordEncoder().encode(memberDetails.getPassword()));
            }
            // Only Librarians can update roles
            if(memberDetails.getRole() != null){
                member.setRole(memberDetails.getRole());
            }
            userService.save(member);
            return ResponseEntity.ok("Member updated successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found"));
    }

    // Remove (soft delete) a member (Librarian only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> removeMember(@PathVariable Long id){
        return userService.findById(id).map(member -> {
            userService.deactivateUser(id);
            return ResponseEntity.ok("Member deactivated successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found"));
    }

    // View all active members (Librarian only)
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<User>> getActiveMembers(){
        return ResponseEntity.ok(userService.findAllActiveMembers());
    }

    // View all deleted members (Librarian only)
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<User>> getDeletedMembers(){
        return ResponseEntity.ok(userService.findAllDeletedMembers());
    }

    // Restore a deleted member (Librarian only)
    @PutMapping("/restore/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> restoreMember(@PathVariable Long id){
        return userService.findById(id).map(member -> {
            userService.activateUser(id);
            return ResponseEntity.ok("Member restored successfully!");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found"));
    }
}
