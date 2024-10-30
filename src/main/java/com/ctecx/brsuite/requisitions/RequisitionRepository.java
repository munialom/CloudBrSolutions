package com.ctecx.brsuite.requisitions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {
    List<Requisition> findByStatus(RequisitionStatus status);
    List<Requisition> findByProductId(Long productId);
    Optional<Requisition> findByRequisitionNumber(String requisitionNumber);
}