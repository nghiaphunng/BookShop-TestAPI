package com.bookshop.demo.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "user_name", nullable = false, unique = true, length = 50)
    String userName;

    @Column(name = "user_email", nullable = false, unique = true, length = 100)
   	String userEmail;

    @Column(name = "user_password", nullable = false, length = 150)
	String userPassword;

    @Column(name = "created_at")
	LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "user")
    private Set<Order> orders;
    
    @OneToOne(mappedBy = "user")
    private Cart cart;
}
