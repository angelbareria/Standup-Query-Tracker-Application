package com.querytracker.standup_query_tracker.service;

import com.querytracker.standup_query_tracker.exception.ResourceNotFoundException;
import com.querytracker.standup_query_tracker.model.User;
import com.querytracker.standup_query_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author abareria
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Create new User
    public User createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    //get user by email
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User","email",email));
    }

    //get user by id
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
    }

    //get all users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Authenticate
    public User authenticate(String email, String password){
        User user = getUserByEmail(email);
        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Invalid Credentials");
        }
        return user;
    }

}
