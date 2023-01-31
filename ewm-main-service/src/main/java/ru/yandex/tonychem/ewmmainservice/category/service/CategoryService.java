package ru.yandex.tonychem.ewmmainservice.category.service;


import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryDto;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.NewCategoryDto;

public interface CategoryService {
    ResponseEntity<Object> createCategory(NewCategoryDto newCategoryDto);

    ResponseEntity<Object> updateCategory(long id, CategoryDto categoryDto);

    ResponseEntity<Void> deleteCategory(long id);
}
