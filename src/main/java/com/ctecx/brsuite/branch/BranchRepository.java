package com.ctecx.brsuite.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Branch findByIsHeadQuarterTrue();

    List<Branch> findByBranchNameContainingIgnoreCase(String keyword);

}
