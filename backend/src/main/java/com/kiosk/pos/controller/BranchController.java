package com.kiosk.pos.controller;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.payload.dto.BranchDto;
import com.kiosk.pos.payload.response.DeleteResponse;
import com.kiosk.pos.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branch")
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branchDto) throws UserException {
        BranchDto created = branchService.createBranch(branchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<BranchDto>> getBranchByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(branchService.getAllBranchesByStoreId(storeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> updateBranch(
            @PathVariable Long id,
            @RequestBody BranchDto branchDto
    ) throws UserException, Exception {
        BranchDto updated = branchService.updateBranch(id, branchDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteBranch(@PathVariable Long id) throws Exception {
        branchService.deleteBranch(id);

        DeleteResponse dl = new DeleteResponse();
        dl.setMessage("Branch deleted Successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(dl);
    }
}
