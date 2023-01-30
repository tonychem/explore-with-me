package ru.yandex.tonychem.ewmmainservice.user.model.dto;


import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;

public class UserMapper {
    public static User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
