package com.dailycodebuffer.jwt.service;

import com.dailycodebuffer.jwt.entity.Token;
import com.dailycodebuffer.jwt.repository.JPATokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private JPATokenRepository jpaTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //Logic to get the user form the Database

        return new User("admin","123456",new ArrayList<>());
    }
    public void saveToken(Token token){
        jpaTokenRepository.save(token);
    }
    public Token getToken(String email){
        return jpaTokenRepository.findByEmail(email);
    }
    public void remove(String token){
        jpaTokenRepository.deleteById(token);
    }

}
