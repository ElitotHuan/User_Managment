package com.example.User_Managment.login;

import com.example.User_Managment.authenticate.Token;
import com.example.User_Managment.authenticate.TokenService;
import com.example.User_Managment.response_handler.ErrorRespone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login.LoginRequest request) {
        Login account = loginService.validateAccount(request);
        Token authToken = tokenService.generateJwtToken(new Token(account.getUser()));
        return ResponseEntity.ok(new Login.LoginRespone("Login success", authToken.getAccessToken(), authToken.getRefreshToken()));
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody Token.TokenRefreshRequest request) {
        Token authToken = tokenService.verifyExpiration(request.getRefreshToken());
        return ResponseEntity.ok(new Token.TokenRefreshRespone("Refresh token success", authToken.getAccessToken(), authToken.getRefreshToken()));
    }
}
