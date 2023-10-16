package ru.practicum.ewm.mainservice.category.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.mainservice.category.entity.Category;
import ru.practicum.ewm.mainservice.repository.Repository;

import java.util.List;

public interface CategoryRepository extends Repository<Category, Long> {

    @Query(value = "SELECT * FROM categories OFFSET ?1 LIMIT ?2", nativeQuery = true)
    List<Category> findFrom(int from, int size);
}