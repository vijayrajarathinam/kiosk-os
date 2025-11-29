package com.kiosk.pos.controller;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.payload.dto.CategoryDto;
import com.kiosk.pos.payload.response.DeleteResponse;
import com.kiosk.pos.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto
    ) throws Exception, UserException {
        CategoryDto created = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDto>> getCategoryByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(categoryService.getCategoriesByStore(storeId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable Long categoryId
    ) throws UserException, Exception {
        CategoryDto updated = categoryService.updateCategory(categoryId,categoryDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updated);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<DeleteResponse> deleteCategory(
            @PathVariable Long categoryId
    ) throws UserException, Exception {
         categoryService.deleteCategory(categoryId);

         DeleteResponse dl = new DeleteResponse();
         dl.setMessage("Category Deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(dl);
    }
}
