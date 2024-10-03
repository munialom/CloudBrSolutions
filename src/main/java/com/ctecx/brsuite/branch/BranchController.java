package com.ctecx.brsuite.branch;


import com.ctecx.brsuite.util.DataTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;


    @GetMapping(path = "/branches-list", produces = "application/json")
    public List<Branch> searchBranches(@RequestParam String keyword) {
        return branchService.searchBranches(keyword);
    }





    @GetMapping
    public ResponseEntity<DataTableResponse<Branch>> getAllBranches(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length
    ) {
        PageRequest pageRequest = PageRequest.of(start / length, length);
        Page<Branch> branchesPage = branchService.getAllBranchesPaged(pageRequest);

        DataTableResponse<Branch> response = new DataTableResponse<>();
        response.setData(branchesPage.getContent());
        response.setDraw(draw);
        response.setRecordsTotal(branchesPage.getTotalElements());
        response.setRecordsFiltered(branchesPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Optional<Branch> branch = branchService.getBranchById(id);
        return branch.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch createdBranch = branchService.createBranch(branch);
        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch updatedBranch = branchService.updateBranch(id, branch);
        if (updatedBranch != null) {
            return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
