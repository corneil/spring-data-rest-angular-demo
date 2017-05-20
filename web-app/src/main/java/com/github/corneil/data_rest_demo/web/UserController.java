package com.github.corneil.data_rest_demo.web;

import com.github.corneil.data_rest_demo.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Corneil on 2017/05/13.
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

}
