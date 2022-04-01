package ru.itmo.worldclassbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itmo.worldclassbackend.entities.User;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userService.findByEmail(email);
        return userEntity!=null?MyUserDetails.fromUserEntityToUserDetails(userEntity):null;
    }
}
