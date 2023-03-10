package ru.practicum.ewm.entity.category.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.exception.CategoryNotFoundException;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    default void checkCategoryExistsById(@NonNull Long catId) {
        if (!existsById(catId)) {
            throw CategoryNotFoundException.fromCategoryId(catId);
        }
    }
}
