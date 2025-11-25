package com.kiosk.pos.service;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.domain.StoreStatus;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.StoreDto;

import java.util.List;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(Long id) throws Exception;
    List<StoreDto> getAllStores();
    Store getStoreByAdmin() throws UserException, Exception;
    StoreDto updateStore(Long id, StoreDto storeDto) throws UserException, Exception;
    void deleteStore(Long id) throws UserException, Exception;
    StoreDto getStoreByEmployee() throws UserException;
    StoreDto moderateStore(Long id, StoreStatus storeStatus) throws Exception;

    StoreDto toDto(Store store);
    Store fromDto(StoreDto storeDto);
}
