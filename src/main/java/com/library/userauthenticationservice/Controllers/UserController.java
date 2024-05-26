package com.library.userauthenticationservice.Controllers;


import com.library.userauthenticationservice.Dto.LogOutRequestDto;
import com.library.userauthenticationservice.Dto.LoginRequestDto;
import com.library.userauthenticationservice.Dto.SignUpRequestDto;
import com.library.userauthenticationservice.Dto.UserDto;
import com.library.userauthenticationservice.Models.Token;
import com.library.userauthenticationservice.Models.User;
import com.library.userauthenticationservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user= userService.signUp(
                signUpRequestDto.getEmail(),
                signUpRequestDto.getName(),
                signUpRequestDto.getPassword());

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto){
        Token token= userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return token;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogOutRequestDto logOutRequestDto){
        userService.logout(logOutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validateToken/{token}")
    public UserDto validateToken(@PathVariable String token){
        User user= userService.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/users/{id}")
    private UserDto getUserById(@PathVariable Long id){
        return null;
    }
}
