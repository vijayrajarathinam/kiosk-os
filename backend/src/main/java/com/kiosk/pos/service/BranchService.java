package com.kiosk.pos.service;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.Branch;
import com.kiosk.pos.model.Store;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.BranchDto;

import java.util.List;

public interface BranchService {

    BranchDto createBranch(BranchDto branchDto) throws UserException;
    BranchDto updateBranch(Long id, BranchDto branchDto) throws Exception, UserException;
    void deleteBranch(Long id) throws Exception;
    List<BranchDto> getAllBranchesByStoreId(Long storeId);
    BranchDto getBranchById(Long id) throws Exception;

    BranchDto toDto(Branch branch);
    Branch fromDto(BranchDto branchDto, Store store);
}
