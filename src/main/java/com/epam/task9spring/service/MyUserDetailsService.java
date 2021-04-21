package com.epam.task9spring.service;

import com.epam.task9spring.model.User;
import com.epam.task9spring.model.UserRole;
import com.epam.task9spring.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public MyUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }


    public boolean saveUser(String login, String name, String password, String role) {
        User userFromDB = userRepository.findByUserName(name).orElseThrow();
        if (userFromDB != null) {
            return false;
        }
        User userForSave = new User();
        userForSave.setPassword(bCryptPasswordEncoder.encode(password));
        if (role.equalsIgnoreCase("admin"))
            userForSave.setUserRole(UserRole.ADMIN);
        if (role.equalsIgnoreCase("user"))
            userForSave.setUserRole(UserRole.USER);
        userRepository.save(userForSave);
        return true;
    }

    public boolean deleteUser(String name) {
        try {
            userRepository.deleteById(userRepository.findByUserName(name).orElseThrow().getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}