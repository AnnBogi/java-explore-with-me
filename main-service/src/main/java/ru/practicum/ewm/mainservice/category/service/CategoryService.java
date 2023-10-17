package ru.practicum.ewm.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mainservice.category.entity.Category;
import ru.practicum.ewm.mainservice.category.repository.CategoryRepository;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConditionNotMetException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findCategory(int from, int size) {
        return categoryRepository.findFrom(from, size);
    }

    public Category findById(long categoryId) {
        return categoryRepository.findByIdOrThrowNotFoundException(categoryId, "Category");
    }

    public void deleteById(long categoryId) {
        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new ConditionNotMetException("The category is not empty");
        }
        categoryRepository.deleteById(categoryId);
    }

    public Category updateCategory(Category updatedCategory) {
        Category category = categoryRepository.findByIdOrThrowNotFoundException(updatedCategory.getId(), "Category");
        if (updatedCategory.getName().equals(category.getName())) {
            return category;
        }
        return categoryRepository.save(updatedCategory);
    }
}