package com.ttree.ttree.token;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TokenService {
    private TokenRepository tokenRepository;
    private List<Token> tokenList;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void saveToken(TokenDto tokenDto) {
        tokenRepository.save(tokenDto.toEntity());
    }

    @Transactional
    public boolean checkToken(String token) {
        boolean status = false;
        tokenList = (List<Token>) tokenRepository.findAll();
        for(Token tokenFromDB : tokenList) {
            if(token.equals(tokenFromDB.getToken())) {
                status = true;
                deletePost(tokenFromDB.getEmail());
                break;
            }
        }
        return status;
    }

    @Transactional
    public void deletePost(String id) {
        tokenRepository.deleteById(id);
    }
}
