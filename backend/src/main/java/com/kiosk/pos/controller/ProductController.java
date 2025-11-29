package com.kiosk.pos.controller;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.ProductDto;
import com.kiosk.pos.payload.response.DeleteResponse;
import com.kiosk.pos.service.ProductService;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(
            @RequestBody ProductDto productDto,
            @RequestHeader("Authorization") String jwt
    ) throws UserException, Exception {
        User user = userService.getUserFromJWT(jwt);
        return ResponseEntity.ok(productService.createProduct(productDto, user));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(@PathVariable("storeId") Long id) {
        return ResponseEntity.ok(productService.getAllProductsByStoreId(id));
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDto>> getProductsBySearch(@PathVariable("storeId") Long storeId, @RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchByKeyword(storeId,keyword));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable("id") Long id,
            @RequestBody ProductDto productDto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception, UserException {
        User user = userService.getUserFromJWT(jwt);
        return ResponseEntity.ok(productService.updateProduct(id, productDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String jwt
    ) throws UserException, Exception {
        User user = userService.getUserFromJWT(jwt);
        productService.deleteProduct(id, user);

        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setMessage("Product Deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteResponse);
    }
}
