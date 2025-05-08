package com.example.mod3.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Entity
@Table(name = "users_j")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Конструктор без аргументов
    public User() {
    }

    // Параметризованный конструктор
    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "User {id=" + id + ", name='" + name + "', email='" + email + "', age='" + age + "', createdAt='" + createdAt + "'}";
    }

}