package com.kiosk.pos.service.impl;

import com.kiosk.pos.model.Category;
import com.kiosk.pos.model.Product;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.ProductDto;
import com.kiosk.pos.repository.CategoryRepository;
import com.kiosk.pos.repository.ProductRepository;
import com.kiosk.pos.repository.StoreRepository;
import com.kiosk.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) throws Exception {
        Store store = storeRepository.findById(productDto.getStoreId())
                .orElseThrow(() -> new Exception("Store not found"));
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));
        Product product = fromDto(productDto, store, category);
        return toDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));

        if (Objects.nonNull(productDto.getStoreId())) {
            Store store = storeRepository.findById(productDto.getStoreId())
                    .orElseThrow(() -> new Exception("Store not found"));

            product.setStore(store);
        }

        if (Objects.nonNull(productDto.getCategoryId())) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new Exception("Category not found"));

            product.setCategory(category);
        }

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSku(productDto.getSku());
        product.setImage(productDto.getImage());
        product.setMrp(productDto.getMrp());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setBrand(product.getBrand());

        return toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id, User user) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProductsByStoreId(Long storeId) {
        return productRepository.findByStoreId(storeId).stream().map(this::toDto).toList();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        return productRepository.searchByKeyword(storeId, keyword).stream().map(this::toDto).toList();
    }

    @Override
    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .brand(product.getBrand())
                .image(product.getImage())
                .mrp(product.getMrp())
                .storeId(product.getStore() != null? product.getStore().getId(): null)
                .categoryId(product.getCategory() != null? product.getCategory().getId(): null)
                .sellingPrice(product.getSellingPrice())
                .build();
    }

    @Override
    public Product fromDto(ProductDto productDto, Store store, Category category) {

        return Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .brand(productDto.getBrand())
                .image(productDto.getImage())
                .category(category)
                .mrp(productDto.getMrp())
                .sellingPrice(productDto.getSellingPrice())
                .store(store)
                .build();
    }
}
