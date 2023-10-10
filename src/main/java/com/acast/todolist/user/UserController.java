package com.acast.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;


    @PostMapping("/")
    public UserModel createUser(@RequestBody UserModel userModel) {
        var createdUser = this.userRepository.save(userModel);
        System.out.println(userModel.getName());
        return createdUser;

    }
    
}
