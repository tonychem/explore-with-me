package ru.yandex.tonychem.ewmmainservice.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
