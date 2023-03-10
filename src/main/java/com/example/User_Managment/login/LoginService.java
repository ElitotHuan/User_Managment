package com.example.User_Managment.login;

import com.example.User_Managment.exceptions_handler.customs_exception.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginRepository loginRepository;

    public Login validateAccount(Login.LoginRequest request) {
        logger.info("Find and validate account...");
        Login account = loginRepository.findAccount(request.getUsername(), request.getPassword());

        if (account == null) {
            throw new LoginException("Invalid username or password");
        }

        if (!account.getExp_date().before(new Date())) {
            return account;
        } else {
            throw new LoginException(account.getUsername() + " has expired at " + account.getExp_date());
        }

    }
}
