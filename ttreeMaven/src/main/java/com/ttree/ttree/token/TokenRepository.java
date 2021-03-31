package com.ttree.ttree.token;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
    boolean findTokenByToken(String token);
}
