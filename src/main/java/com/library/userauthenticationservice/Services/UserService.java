package com.library.userauthenticationservice.Services;

import com.library.userauthenticationservice.Models.Token;
import com.library.userauthenticationservice.Models.User;
import com.library.userauthenticationservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
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
        return null;
    }

    public void logout(String token) {
    }

    public User validateToken(String token) {
        return null;
    }

}
