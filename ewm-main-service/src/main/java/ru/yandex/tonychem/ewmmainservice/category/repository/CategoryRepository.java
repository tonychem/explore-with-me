package ru.yandex.tonychem.ewmmainservice.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
