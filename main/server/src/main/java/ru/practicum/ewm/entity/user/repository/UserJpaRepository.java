package ru.practicum.ewm.entity.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.exception.UserNotFoundException;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    default void checkUserExistsById(@NonNull Long userId) {
        if (!existsById(userId)) {
            throw UserNotFoundException.fromUserId(userId);
        }
    }
}
