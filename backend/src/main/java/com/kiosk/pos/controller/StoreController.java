package com.kiosk.pos.controller;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.domain.StoreStatus;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.StoreDto;
import com.kiosk.pos.payload.response.DeleteResponse;
import com.kiosk.pos.service.StoreService;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<StoreDto> createStore(
            @RequestBody StoreDto storeDto,
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.getUserFromJWT(jwt);
        StoreDto store = storeService.createStore(storeDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(store);
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores(){
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoresByAdmin() throws UserException, Exception {
        return ResponseEntity.ok(storeService.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoresByEmployee() throws UserException, Exception {
        return ResponseEntity.ok(storeService.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(
            @PathVariable("id") Long id,
            @RequestBody StoreDto storeDto
    ) throws UserException, Exception {
        return ResponseEntity.ok(storeService.updateStore(id,storeDto));
    }

    @PutMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable("id") Long id,
            @RequestParam StoreStatus storeStatus
    ) throws Exception {
        return ResponseEntity.ok(storeService.moderateStore(id, storeStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteStoreById(@PathVariable("id") Long id) throws Exception, UserException {
        storeService.deleteStore(id);

        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setMessage("Store deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteResponse);
    }
}
