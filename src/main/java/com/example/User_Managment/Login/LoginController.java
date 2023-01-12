package com.example.User_Managment.Login;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.response_handler.ErrorRespone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    private final Token token = new Token();

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login.LoginRequest request) {
        Login account = loginService.findAccount(request);

        if (account == null) {
            ErrorRespone respone = new ErrorRespone("Username or Password is incorrect", 401);
            return ResponseEntity.status(respone.getStatusCode()).body(respone);
        } else {
            Token newToken = new Token(account.getUser());
            String authToken = token.generateAuthToken(newToken);
            return ResponseEntity.status(200).body(new Login.LoginRespone("Login success", authToken));
        }
    }
}
