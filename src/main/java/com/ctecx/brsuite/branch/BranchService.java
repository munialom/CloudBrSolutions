package com.ctecx.brsuite.branch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public Branch getHeadQuarterBranch() {
        return branchRepository.findByIsHeadQuarterTrue();
    }


    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public Branch updateBranch(Long id, Branch updatedBranch) {
        if (branchRepository.existsById(id)) {
            updatedBranch.setId(id);
            return branchRepository.save(updatedBranch);
        }
        return null; // Handle not found scenario
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }

    public Page<Branch> getAllBranchesPaged(PageRequest pageRequest) {
        return branchRepository.findAll(pageRequest);
    }

    public List<Branch> searchBranches(String keyword) {
        List<Branch> branches = branchRepository.findByBranchNameContainingIgnoreCase(keyword);

        return branches.stream()
                .map(branch -> new Branch(branch.getId(), branch.getBranchName(), branch.getBranchLocation(), branch.getBranchCode(), branch.isHeadQuarter()))
                .collect(Collectors.toList());
    }

}
