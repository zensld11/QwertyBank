package com.degilok.al.cbank.sevice;

import com.degilok.al.cbank.entity.User;
import com.degilok.al.cbank.entity.dto.UserDto;

public interface UserService {
    User createUser(UserDto userDto) throws Exception;

    User updateUser(UserDto userDto);

    User registerUser(User user) throws Exception;
}
