package ru.practicum.ewm.mainservice.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.category.dto.NewCategoryDto;
import ru.practicum.ewm.mainservice.category.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category fromRequest(NewCategoryDto newCategoryDto);

    CategoryDto toDto(Category category);
}