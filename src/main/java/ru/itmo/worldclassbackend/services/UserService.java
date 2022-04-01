package ru.itmo.worldclassbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.itmo.worldclassbackend.entities.Authority;
import ru.itmo.worldclassbackend.entities.User;
import ru.itmo.worldclassbackend.repositories.AuthorityRepository;
import ru.itmo.worldclassbackend.repositories.UserRepository;

import java.util.*;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public User save(User user){
        return userRepository.save(user);
    }

    public User saveUser(String email, String password, String name) {
        Set<Authority> authorities=new HashSet<>(Collections.singletonList(authorRepository.findAuthoritiesByAuthority("ROLE_USER")));
        String encodedPassword = passwordEncoder().encode(password);
        User userEntity=new User(
            email,
            encodedPassword,
            true,
            name,
            authorities
            );
        return userRepository.save(userEntity);
    }
    public User editUser(String oldEmail,String newEmail,String password){
        String encodedPassword= passwordEncoder().encode(password);
        User user = findByEmail(oldEmail);
        user.setEmail(newEmail);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<Map<String,Object>> getAllFavouriteNutritions(User user){
        return userRepository.getAllFavouriteNutritions(user);
    }

    public List<Map<String,Object>> getAllFavouriteExercises(User user){
        return userRepository.getAllFavouriteExercises(user);
    }

    public User findByEmailAndPassword(String email, String password) {
        User userEntity = findByEmail(email);
        if (userEntity != null
                && passwordEncoder().matches(password,userEntity.getPassword())) {
            return userEntity;
        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
