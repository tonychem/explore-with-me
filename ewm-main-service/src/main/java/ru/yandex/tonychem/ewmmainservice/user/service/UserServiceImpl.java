package ru.yandex.tonychem.ewmmainservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.NewUserRequest;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserDto;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserMapper;
import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;
import ru.yandex.tonychem.ewmmainservice.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> users(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.Direction.ASC, "id");
        List<UserDto> users;

        if (ids != null) {
            users = userRepository.findAllByIdIn(ids, pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        users = userRepository.findAllBy(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> createUser(NewUserRequest newUserRequest) {
        User userFromRequest = UserMapper.toUser(newUserRequest);
        User savedUser = userRepository.save(userFromRequest);
        return new ResponseEntity<>(UserMapper.toUserDto(savedUser), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
