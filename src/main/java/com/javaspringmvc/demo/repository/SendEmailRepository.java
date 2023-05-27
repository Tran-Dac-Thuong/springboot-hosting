package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.SendEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendEmailRepository extends JpaRepository<SendEmail, Long> {
}
