package com.ryanmhub.fitnessapp.token;

import com.ryanmhub.fitnessapp.user.model.AppUser;
import org.springframework.stereotype.Service;

import static com.ryanmhub.fitnessapp.token.TokenType.BEARER;

@Service
public class TokenService {

    public final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void revokeAllUserTokens(AppUser user) {
        var validTokens = tokenRepository.findAllValidTokenByAppUser(user.getId());
        if (validTokens.isEmpty()) return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    //Todo: Should this be somewhere else
    public void saveUserToken(AppUser user, String jwtToken){
        var token = new Token.Builder()
                .user(user)
                .token(jwtToken)
                .tokenType(BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
