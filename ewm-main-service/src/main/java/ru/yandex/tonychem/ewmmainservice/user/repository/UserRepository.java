package ru.yandex.tonychem.ewmmainservice.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.tonychem.ewmmainservice.user.model.dto.UserDto;
import ru.yandex.tonychem.ewmmainservice.user.model.entity.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<UserDto> findAllBy(Pageable pageable);

    List<UserDto> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
