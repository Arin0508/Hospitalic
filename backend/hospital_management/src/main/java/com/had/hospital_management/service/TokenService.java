package com.had.hospital_management.service;

import com.had.hospital_management.model.Token;
import com.had.hospital_management.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public void logout(String tokenval) {
        Optional<Token> tokenOptional = tokenRepository.findById(tokenval);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            tokenRepository.delete(token);
        } else {
            System.out.println("error in changing token val / token does not exist");
        }
    }

    public Boolean isValid(String tokenval) {
        Optional<Token> tokenOptional = tokenRepository.findById(tokenval);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();

            System.out.println("valid token");
            return true;
        } else {
            System.out.println("invalid token");
            return false;
        }
    }
}
