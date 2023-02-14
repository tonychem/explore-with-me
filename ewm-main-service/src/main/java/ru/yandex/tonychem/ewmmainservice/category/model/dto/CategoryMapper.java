package ru.yandex.tonychem.ewmmainservice.category.model.dto;


import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
