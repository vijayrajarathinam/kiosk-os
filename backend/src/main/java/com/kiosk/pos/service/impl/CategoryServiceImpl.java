package com.kiosk.pos.service.impl;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.domain.UserRole;
import com.kiosk.pos.model.Category;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.CategoryDto;
import com.kiosk.pos.repository.CategoryRepository;
import com.kiosk.pos.repository.StoreRepository;
import com.kiosk.pos.service.CategoryService;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final UserService userService;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
        List<Category> categories = categoryRepository.findByStoreId(storeId);
        return categories.stream().map(this::toDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
        return toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws UserException, Exception {
        Store store = storeRepository.findById(categoryDto.getStoreId())
                .orElseThrow(() -> new Exception("Store not found"));

        checkAuthority(store);
        Category category = Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .store(store)
                .build();
        return toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws Exception, UserException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not exist"));

        checkAuthority(category.getStore());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return toDto(categoryRepository.save(fromDto(categoryDto, category.getStore())));
    }

    @Override
    public void deleteCategory(Long id) throws Exception, UserException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not exist"));

        checkAuthority(category.getStore());
        categoryRepository.deleteById(id);
    }

    private void checkAuthority(Store store) throws UserException, Exception {
        User user = userService.getCurrentuser();

        boolean isStAdmin = user.getRole().equals(UserRole.ROLE_STORE_ADMIN);
        boolean isManager = user.getRole().equals(UserRole.ROLE_STORE_MANAGER);
        boolean sameStore = user.equals(store.getStoreAdmin());

        if(!(isStAdmin && sameStore) && !isManager)
            throw new Exception("You don't have permission to manage thsi category");
    }

    @Override
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .storeId(category.getStore().getId())
                .build();
    }

    @Override
    public Category fromDto(CategoryDto categoryDto, Store store) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .store(store)
                .build();
    }
}
