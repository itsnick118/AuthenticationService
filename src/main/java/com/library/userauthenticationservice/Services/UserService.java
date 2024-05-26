package com.library.userauthenticationservice.Services;

import com.library.userauthenticationservice.Exceptions.UserNotFoundException;
import com.library.userauthenticationservice.Models.Token;
import com.library.userauthenticationservice.Models.User;
import com.library.userauthenticationservice.Repository.TokenRepository;
import com.library.userauthenticationservice.Repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String email,
                       String name,
                       String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        return userRepository.save(user);
    }

    public Token login(String email,
                       String password) {
        Optional<User> optionalUser= userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with email "+ email + " doesn't exist");
        }
        User user =optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            //throw some exception.
            return null;
        }
        //login successful, generate a token.
        Token token= generateToken(user);
        Token savedToken= tokenRepository.save(token);
        return savedToken;
    }

    private Token generateToken(User user){
        LocalDate currentDate= LocalDate.now();
        LocalDate thirtyDaysLater= currentDate.plusDays(30);
        Date expiryDate= Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Token token= new Token();
        token.setExpiryAt(expiryDate);
        //128 character alphanumeric string.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    public void logout(String token) {
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeleted(token, false);
        if(optionalToken.isEmpty()){
            //throw new exception
            return;
        }
        Token tokenValue= optionalToken.get();
        tokenValue.setDeleted(true);
        tokenRepository.save(tokenValue);

    }

    public User validateToken(String token) {
        Optional<Token> optionalToken= tokenRepository.findByValueAAndDeletedAndExpiryAtGreaterThan(token, false, new Date());
        if(optionalToken.isEmpty()){
            //throw new exception

        }
        return optionalToken.get().getUser();
    }

}
