package ru.yandex.tonychem.ewmmainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryDto;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.CategoryMapper;
import ru.yandex.tonychem.ewmmainservice.category.model.dto.NewCategoryDto;
import ru.yandex.tonychem.ewmmainservice.category.model.entity.Category;
import ru.yandex.tonychem.ewmmainservice.category.repository.CategoryRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchCategoryException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> createCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.toCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(newCategory);
        return new ResponseEntity<>(CategoryMapper.toDto(savedCategory), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateCategory(long id, CategoryDto categoryDto) {
        Category categoryFromDB = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchCategoryException("Category with id="
                        + categoryDto.getId() + " was not found"));

        categoryFromDB.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(categoryFromDB);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCategory(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchCategoryException("Category with id=" + id + " was not found");
        }

        categoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Object> categories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size, Sort.Direction.ASC, "id");
        List<Category> categories = categoryRepository.findAll(page).toList();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> categoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchCategoryException("Category with id=" + categoryId + " was not found"));
        return new ResponseEntity<>(CategoryMapper.toDto(category), HttpStatus.OK);
    }
}
