package com.example.User_Managment.User;

import com.example.User_Managment.Login.Token;
import com.example.User_Managment.response_handler.ErrorRespone;
import com.example.User_Managment.response_handler.SuccessRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService services;


    Token token = new Token();

    ErrorRespone errorRespone;


    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(required = false) Long id,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                if (id == null) {
                    try {
                        List<UserDTO> list = services.getAll();
                        if (list.isEmpty()) {
                            return ResponseEntity.ok().body(new SuccessRespone("There are no users"));
                        }
                        return ResponseEntity.ok(list);
                    } catch (NullPointerException e) {
                        errorRespone = new ErrorRespone(e.getMessage(), HttpStatus.BAD_REQUEST.value());
                        return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone);
                    }

                } else {
                    UserDTO employee = services.getUser(id);
                    if (employee == null) {
                        return ResponseEntity.status(200).body(new SuccessRespone("User doesn't exit"));
                    }
                    return ResponseEntity.ok(employee);
                }
            } else {
                errorRespone = new ErrorRespone("Unauthorized Access!", HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
            }
        }
        errorRespone = new ErrorRespone("Access denied", HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {

        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                Object respone = services.addUser(user);
                return ResponseEntity.status(200).body(respone);
            } else {
                errorRespone = new ErrorRespone("Unauthorized Access!", 401);
                return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
            }
        } else {
            errorRespone = new ErrorRespone("Access denied", 403);
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {

        if (token.validateToken(authtoken)) {
            Object respone = services.updateUser(user.getUserId(), user);
            return ResponseEntity.status(200).body(respone);
        }

        errorRespone = new ErrorRespone("Access denied", 403);
        return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
    }

//    @PutMapping("/update/password")
//    public ResponseEntity<?> updatePassword(@Valid @RequestBody User.PasswordUpdate update) {
//        ResponeObject respone = services.updatePassword(update.getId(), update.getNewPassword());
//        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//    }
//

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@Valid @RequestBody User.UserID id,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {

        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                Object respone = services.deleteUser(id.getId());
                return ResponseEntity.ok().body(respone);
            } else {
                errorRespone = new ErrorRespone("Unauthorized Access!", 401);
                return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
            }

        } else {
            errorRespone = new ErrorRespone("Access denied: Token expired", 403);
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
        }

    }

}