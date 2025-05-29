package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.CategoryDTO;
import org.iti.ecomus.dto.CategoryNoProductDTO;
import org.iti.ecomus.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);
    CategoryNoProductDTO toCategoryNoProductDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> toDtoList(List<Category> categories);
    List<Category> toCategoryList(List<CategoryDTO> dtos);
}
