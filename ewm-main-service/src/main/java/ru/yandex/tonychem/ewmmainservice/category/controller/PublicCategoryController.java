package ru.yandex.tonychem.ewmmainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> categories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoryService.categories(from, size);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> categoryById(@PathVariable long categoryId) {
        return categoryService.categoryById(categoryId);
    }
}
