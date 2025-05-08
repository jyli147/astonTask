package com.example.mod3.Dto;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class UserDto {
   private Long id;

   private String name;

   private String email;

   private Integer age;

   private Date createdAt;

   // Конструктор без аргументов
   public UserDto() {
   }

   // Параметризованный конструктор
   public UserDto(Long id, String name, String email, Integer age) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.age = age;
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
      return "UserDto {id=" + id + ", name='" + name + "', email='" + email + "', age='" + age + "', createdAt='" + createdAt + "'}";
   }
}
