package com.kiosk.pos.service;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.Category;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategoriesByStore(Long storeId);
    CategoryDto getCategoryById(Long id) throws Exception;
    CategoryDto createCategory(CategoryDto categoryDto) throws UserException, Exception;
    CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws Exception, UserException;
    void deleteCategory(Long id) throws Exception, UserException;

    CategoryDto toDto(Category category);
    Category fromDto(CategoryDto categoryDto, Store store);

}
