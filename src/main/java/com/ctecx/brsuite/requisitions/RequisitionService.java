package com.ctecx.brsuite.requisitions;

import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.util.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;
    private final ProductRepository productRepository;
    private final RequisitionNumberGenerator requisitionNumberGenerator;

    @Transactional
    public RequisitionDTO createRequisition(RequisitionDTO dto) {
        var requisition = new Requisition();
        requisition.setRequisitionNumber(requisitionNumberGenerator.generateRequisitionNumber());
        return processRequisitionCreation(dto, requisition);
    }

    @Transactional
    public List<RequisitionDTO> createBulkRequisitions(List<RequisitionDTO> dtos) {
        return dtos.stream()
                .map(dto -> {
                    var requisition = new Requisition();
                    requisition.setRequisitionNumber(requisitionNumberGenerator.generateRequisitionNumber());
                    return processRequisitionCreation(dto, requisition);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public RequisitionDTO updateRequisition(Long id, RequisitionDTO dto) {
        var requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisition not found with id: " + id));
        return processRequisitionCreation(dto, requisition);
    }

    private RequisitionDTO processRequisitionCreation(RequisitionDTO dto, Requisition requisition) {
        if (dto.getRequestedProducts() == null || dto.getRequestedProducts().isEmpty()) {
            throw new IllegalArgumentException("Requested products cannot be empty");
        }

        // We'll process the first product in the list since the current Requisition entity
        // supports only one product per requisition
        RequestedProducts requestedProduct = dto.getRequestedProducts().get(0);

        var product = productRepository.findByProductCode(requestedProduct.getProductCode());

        requisition.setProduct(product);
        requisition.setQuantityRequested(requestedProduct.getQuantityRequested());
        requisition.setQuantityIssued(0);
        requisition.setRemainingQuantity(requestedProduct.getQuantityRequested());
        requisition.setStatus(RequisitionStatus.PENDING);
        requisition.setComments(dto.getComments());

        var savedRequisition = requisitionRepository.save(requisition);
        return mapToDTO(savedRequisition);
    }

    @Transactional
    public RequisitionDTO issueStock(Long requisitionId, Integer quantityToIssue) {
        var requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Requisition not found with id: " + requisitionId));

        if (quantityToIssue > requisition.getRemainingQuantity()) {
            throw new IllegalArgumentException("Cannot issue more than remaining quantity");
        }

        requisition.setQuantityIssued(requisition.getQuantityIssued() + quantityToIssue);
        requisition.setRemainingQuantity(requisition.getRemainingQuantity() - quantityToIssue);

        // Update status based on issued quantity
        requisition.setStatus(switch (requisition.getRemainingQuantity()) {
            case 0 -> RequisitionStatus.FULFILLED;
            default -> requisition.getQuantityIssued() > 0 ?
                    RequisitionStatus.PARTIALLY_FULFILLED : requisition.getStatus();
        });

        return mapToDTO(requisitionRepository.save(requisition));
    }

    @Transactional
    public RequisitionDTO updateStatus(Long id, RequisitionStatus status) {
        var requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisition not found with id: " + id));

        requisition.setStatus(status);
        return mapToDTO(requisitionRepository.save(requisition));
    }

    public RequisitionDTO getById(Long id) {
        return requisitionRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Requisition not found with id: " + id));
    }

    public List<RequisitionDTO> getAllRequisitions() {
        return requisitionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    public List<RequisitionDTO> getByStatus(RequisitionStatus status) {
        return requisitionRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RequisitionDTO> getRequisitionsByProduct(Long productId) {
        return requisitionRepository.findByProductId(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RequisitionDTO mapToDTO(Requisition requisition) {
        var dto = new RequisitionDTO();
        dto.setRequisitionNumber(requisition.getRequisitionNumber());

        var requestedProduct = new RequestedProducts();
        requestedProduct.setProductId(requisition.getProduct().getId());
        requestedProduct.setQuantityRequested(requisition.getQuantityRequested());

        dto.setRequestedProducts(List.of(requestedProduct));
        dto.setComments(requisition.getComments());

        return dto;
    }
}