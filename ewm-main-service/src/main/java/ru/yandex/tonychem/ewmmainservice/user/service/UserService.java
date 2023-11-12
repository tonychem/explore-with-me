package ru.yandex.tonychem.ewmmainservice.user.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.NewUserRequest;

import java.util.List;

public interface UserService {

    ResponseEntity<Object> users(List<Long> ids, Integer from, Integer size);

    ResponseEntity<Object> createUser(NewUserRequest newUserRequest);

    ResponseEntity<Void> deleteUser(long id);
}
