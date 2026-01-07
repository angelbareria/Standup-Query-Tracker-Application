package com.querytracker.standup_query_tracker.contoller;

import com.querytracker.standup_query_tracker.dto.LoginRequest;
import com.querytracker.standup_query_tracker.dto.SignupRequest;
import com.querytracker.standup_query_tracker.model.User;
import com.querytracker.standup_query_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author abareria
 **/

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    //post json body for login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                userService.authenticate(request.getEmail(), request.getPassword())
        );

    }

    //Post body for create user
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return ResponseEntity.ok(
                userService.createUser(user)
        );
    }

    //Getting list of users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Getting List of users by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
