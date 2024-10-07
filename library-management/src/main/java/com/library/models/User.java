// src/main/java/com/library/models/User.java
package com.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // LIBRARIAN or MEMBER

    @Column(nullable = false)
    private boolean active = true; // To handle soft deletion

	public void setUsername(String username2) {
		// TODO Auto-generated method stub
		
	}
}
