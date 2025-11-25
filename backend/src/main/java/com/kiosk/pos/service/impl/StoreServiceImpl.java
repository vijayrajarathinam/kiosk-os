package com.kiosk.pos.service.impl;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.domain.StoreStatus;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.StoreContact;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.StoreDto;
import com.kiosk.pos.payload.dto.UserDto;
import com.kiosk.pos.repository.StoreRepository;
import com.kiosk.pos.repository.UserRepository;
import com.kiosk.pos.service.StoreService;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final UserService userService;
    private final StoreRepository storeRepository;

    public StoreDto toDto(Store store){

        return StoreDto.builder()
                .id(store.getId())
                .Brand(store.getBrand())
                .contact(store.getContact())
                .storeType(store.getStoreType())
                .description(store.getDescription())
                .storeStatus(store.getStoreStatus())
                .storeAdmin(userService.toDto(store.getStoreAdmin()))
                .build();
    }

    public Store fromDto(StoreDto storeDto) {
        return Store.builder()
                .id(storeDto.getId())
                .Brand(storeDto.getBrand())
                .contact(storeDto.getContact())
                .storeType(storeDto.getStoreType())
                .description(storeDto.getDescription())
                .storeStatus(storeDto.getStoreStatus())
                .storeAdmin(userService.fromDto(storeDto.getStoreAdmin()))
                .build();
    }

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = fromDto(storeDto);
        return toDto(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) throws Exception {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));
        return toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(this::toDto).toList();
    }

    @Override
    public Store getStoreByAdmin() throws UserException, Exception {
        User storeAdmin = userService.getCurrentuser();
        if(Objects.isNull(storeAdmin))
            throw new UserException("You don't have permission for the store");

        Store store = storeRepository.findByStoreAdminId(storeAdmin.getId());
        if(Objects.isNull(store))
            throw new Exception("Store not found");

        return store;
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws UserException, Exception {
        Store store = getStoreByAdmin();
        store.setBrand(storeDto.getBrand());
        store.setDescription(storeDto.getDescription());
        if(Objects.nonNull(storeDto.getStoreType()))
            store.setStoreType(storeDto.getStoreType());
        if(Objects.nonNull(storeDto.getContact())) {
            StoreContact storeContact = StoreContact.builder()
                    .address(storeDto.getContact().getAddress())
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .build();
            store.setContact(storeContact);
        }
        store.setId(id);

        Store updated = storeRepository.save(store);

        return toDto(updated);
    }

    @Override
    public void deleteStore(Long id) throws UserException, Exception {
        Store store = getStoreByAdmin();
        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserException {
        User currentuser = userService.getCurrentuser();
        if(Objects.isNull(currentuser))
            throw new UserException("You don't have permission for the store");

        Store store = storeRepository.findByStoreAdminId(currentuser.getId());
        return toDto(store);
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus storeStatus) throws Exception {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));
        store.setStoreStatus(storeStatus);

        return toDto(storeRepository.save(store));
    }
}
