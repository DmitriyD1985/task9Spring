package com.epam.task9spring.service;

import com.epam.task9spring.model.Role;
import com.epam.task9spring.model.User;
import com.epam.task9spring.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String request) throws UsernameNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JsonNode jsonNode1 = actualObj.get("name");
        String username = jsonNode1.asText();

        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("49 User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toSet());
    }


    public User saveUser(String request) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JsonNode jsonNode1 = actualObj.get("name");
        String username = null;
        try {
            username = jsonNode1.asText();
            findByUsername(username);
        } catch (UsernameNotFoundException | NoSuchElementException e) {
            logger.log(Level.INFO, "user with this name is not found, he is can be saved");
        }
        jsonNode1 = actualObj.get("role");
        String role = jsonNode1.textValue();
        Set<Role> roleSet = new HashSet<>();
        if (role.equalsIgnoreCase("admin"))
            roleSet.add(Role.ROLE_ADMIN);
        if (role.equalsIgnoreCase("user"))
            roleSet.add(Role.ROLE_USER);


        jsonNode1 = actualObj.get("password");
        String password = jsonNode1.textValue();

        jsonNode1 = actualObj.get("email");
        String email = jsonNode1.textValue();

        User user = new User(username, bCryptPasswordEncoder.encode(password), email);
        user.setRoles(roleSet);
        userRepository.save(user);
        return user;
    }
}
