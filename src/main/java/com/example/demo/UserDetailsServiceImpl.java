package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("Here???");
        User user = userRepository.getUserByUsername(username);
        System.out.println("Or Here???");

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        System.out.println("FOUND!!!!");

        return new MyUserDetails(user);
    }
}