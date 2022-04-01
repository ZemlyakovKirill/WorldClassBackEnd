package ru.itmo.worldclassbackend.controllers;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.worldclassbackend.entities.User;
import ru.itmo.worldclassbackend.utils.AbstractController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Api(tags = "Авторизационные эндпоинты")
@RestController
@RequestMapping(produces = "application/json")
@Validated
public class AuthController extends AbstractController {
    @PostMapping("/auth/registrate")
    public ResponseEntity<String> registerUser(@Valid @RequestParam("password") String password,
                                               @Valid @RequestParam("email") String email,
                                               @Valid @RequestParam("name") String name) {
        if (userService.findByEmail(email) == null) {
            userService.saveUser(email,password, name);
            return responseCreated(null);
        } else {
            return responseBad("Пользователь уже существует");
        }
    }
    @PostMapping(value = "/auth/login", produces = "application/json")
    public ResponseEntity<String> auth(@Valid @RequestParam("email") String username,
                                       @Valid @RequestParam("password") String password) {
        if (userService.findByEmailAndPassword(username, password) != null) {
            String token = jwtProvider.generateToken(username);
            return responseSuccess(token);
        }
        return responseBad("Пользователь не существует");
    }

    @GetMapping(value = "/user/profile",produces = "application/json")
    public ResponseEntity<String> getUserProfile(Principal principal){
        User user = userService.findByEmail(principal.getName());
        return responseSuccess(user);
    }

    @PutMapping(value="/user/profile/edit",produces = "application/json")
    public ResponseEntity<String> editProfileData(Principal principal,
                                                  @Valid @RequestParam("email") String email,
                                                  @Valid @RequestParam("password") String password){
        User user=userService.findByEmail(principal.getName());
        if (userService.findByEmail(email) == null) {
            userService.editUser(user.getEmail(),email,password);
            return responseCreated(null);
        } else {
            return responseBad("Пользователь уже существует");
        }
    }
}
