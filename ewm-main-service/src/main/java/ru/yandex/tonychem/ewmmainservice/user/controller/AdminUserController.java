package ru.yandex.tonychem.ewmmainservice.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserDto;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.NewUserRequest;
import ru.yandex.tonychem.ewmmainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService adminUserService;

    @GetMapping
    public ResponseEntity<Object> users(@RequestParam(required = false) List<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return adminUserService.users(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUserService.createUser(newUserRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        return adminUserService.deleteUser(id);
    }
}
