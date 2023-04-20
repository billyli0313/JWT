package com.dailycodebuffer.jwt.repository;

import com.dailycodebuffer.jwt.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPATokenRepository extends JpaRepository<Token,String> {
    Token findByEmail(String email);
}
