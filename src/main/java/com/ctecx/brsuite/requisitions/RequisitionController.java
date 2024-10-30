package com.ctecx.brsuite.requisitions;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/requisitions")
@RequiredArgsConstructor
@Validated
public class RequisitionController {

    private final RequisitionService requisitionService;

    @PostMapping
    public ResponseEntity<RequisitionDTO> createRequisition(
            @Valid @RequestBody RequisitionDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requisitionService.createRequisition(dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RequisitionDTO>> createBulkRequisitions(
            @Valid @RequestBody List<RequisitionDTO> dtos) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requisitionService.createBulkRequisitions(dtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequisitionDTO> updateRequisition(
            @PathVariable Long id,
            @Valid @RequestBody RequisitionDTO dto) {
        return ResponseEntity.ok(requisitionService.updateRequisition(id, dto));
    }

    @PutMapping("/{id}/issue")
    public ResponseEntity<RequisitionDTO> issueStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(requisitionService.issueStock(id, quantity));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RequisitionDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam RequisitionStatus status) {
        return ResponseEntity.ok(requisitionService.updateStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequisitionDTO> getRequisition(@PathVariable Long id) {
        return ResponseEntity.ok(requisitionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RequisitionDTO>> getAllRequisitions(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "DESC") String sort) {
        return ResponseEntity.ok(requisitionService.getAllRequisitions());
    }



    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequisitionDTO>> getByStatus(
            @PathVariable RequisitionStatus status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity.ok(requisitionService.getByStatus(status));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RequisitionDTO>> getByProduct(
            @PathVariable Long productId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity.ok(requisitionService.getRequisitionsByProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RequisitionDTO>> searchRequisitions(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) RequisitionStatus status,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        // You'll need to implement this method in the service
        return ResponseEntity.ok(List.of());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRequisition(@PathVariable Long id) {
        requisitionService.updateStatus(id, RequisitionStatus.CANCELLED);
        return ResponseEntity.noContent().build();
    }
}