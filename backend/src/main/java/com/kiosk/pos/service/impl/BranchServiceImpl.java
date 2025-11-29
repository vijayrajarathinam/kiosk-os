package com.kiosk.pos.service.impl;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.Branch;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.BranchDto;
import com.kiosk.pos.repository.BranchRepository;
import com.kiosk.pos.repository.StoreRepository;
import com.kiosk.pos.service.BranchService;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final UserService userService;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;

    @Override
    public BranchDto createBranch(BranchDto branchDto) throws UserException {
        User currentUser = userService.getCurrentuser();
        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

        Branch branch = fromDto(branchDto, store);
        return toDto(branchRepository.save(branch));
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto branchDto) throws Exception, UserException {
        User currentUser = userService.getCurrentuser();
        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new Exception("Branch not found"));

        branch.setName(branchDto.getName());
        branch.setAddress(branchDto.getAddress());
        branch.setEmail(branchDto.getEmail());
        branch.setPhone(branchDto.getPhone());
        branch.setWorkingDays(branchDto.getWorkingDays());
        branch.setOpenTime(branchDto.getOpenTime());
        branch.setCloseTime(branchDto.getCloseTime());
        branch.setManager(userService.fromDto(branchDto.getManager()));
        branch.setStore(store);

        return toDto(branchRepository.save(branch));
    }

    @Override
    public void deleteBranch(Long id) throws Exception {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new Exception("Branch not found"));

        branchRepository.delete(branch);
    }

    @Override
    public List<BranchDto> getAllBranchesByStoreId(Long storeId) {
        return branchRepository.findByStoreId(storeId).stream().map(this::toDto).toList();
    }

    @Override
    public BranchDto getBranchById(Long id) throws Exception {
        return toDto(branchRepository.findById(id)
            .orElseThrow(() -> new Exception("Branch not found")));
    }

    @Override
    public BranchDto toDto(Branch branch) {
        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .openTime(branch.getOpenTime())
                .closeTime(branch.getCloseTime())
                .storeId(branch.getStore() != null ? branch.getStore().getId() : null)
                .manager(userService.toDto(branch.getManager()))
                .workingDays(branch.getWorkingDays())
                .build();
    }

    @Override
    public Branch fromDto(BranchDto branchDto, Store store) {
        return Branch.builder()
                .name(branchDto.getName())
                .address(branchDto.getAddress())
                .email(branchDto.getEmail())
                .phone(branchDto.getPhone())
                .workingDays(branchDto.getWorkingDays())
                .openTime(branchDto.getOpenTime())
                .closeTime(branchDto.getCloseTime())
                .manager(userService.fromDto(branchDto.getManager()))
                .store(store)
                .build();
    }
}
