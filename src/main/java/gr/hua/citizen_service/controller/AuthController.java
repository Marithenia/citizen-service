package gr.hua.citizen_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.citizen_service.model.Role;
import gr.hua.citizen_service.model.User;
import gr.hua.citizen_service.repository.UserRepository;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CITIZEN);
        userRepository.save(user);
        return "Ο χρήστης " + user.getUsername() + " καταχωρήθηκε επιτυχώς.";
    }
}