package com.aws_practice.services.user;

import com.aws_practice.models.User;

public interface IUserService {

    User createUser(User user);
    User findUserById(long id);
}
