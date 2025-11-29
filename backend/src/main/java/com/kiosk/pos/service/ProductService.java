package com.kiosk.pos.service;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.Category;
import com.kiosk.pos.model.Product;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, User user) throws Exception;
    ProductDto updateProduct(Long id, ProductDto productDto, User user) throws Exception;
    void deleteProduct(Long id, User user) throws Exception;
    List<ProductDto> getAllProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword(Long storeId, String keyword);

    ProductDto toDto(Product product);
    Product fromDto(ProductDto productDto, Store store, Category category);

}
