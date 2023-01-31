package ru.yandex.tonychem.ewmmainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryDto;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.NewCategoryDto;
import ru.yandex.tonychem.ewmmainservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable long id,
                                                 @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        return categoryService.deleteCategory(id);
    }
}
