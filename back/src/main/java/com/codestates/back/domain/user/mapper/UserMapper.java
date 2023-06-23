package com.codestates.back.domain.user.mapper;

import com.codestates.back.domain.user.dto.UserDto;
import com.codestates.back.domain.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userPostToUser(UserDto.Post requestBody);

    User userPatchToUser(UserDto.Update requestBody);

    UserDto.Response userToUserResponse(User user);

    List<UserDto.Response> usersToUserResponses(List<User> users);
}
