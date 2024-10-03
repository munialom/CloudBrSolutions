package com.ctecx.brsuite.uom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UomService {

    private final UomRepository uomRepository;

    @Autowired
    public UomService(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    public List<Uom> getAllUoms() {
        return uomRepository.findAll();
    }

    public Optional<Uom> getUomById(Long id) {
        return uomRepository.findById(id);
    }

    public Uom createUom(Uom uom) {
        return uomRepository.save(uom);
    }

    public Uom updateUom(Long id, Uom updatedUom) {
        if (uomRepository.existsById(id)) {
            updatedUom.setId(id);
            return uomRepository.save(updatedUom);
        }
        return null; // You can handle this differently based on your use case
    }

    public void deleteUom(Long id) {
        uomRepository.deleteById(id);
    }
    public Page<Uom> getAllUomsPaged(PageRequest pageRequest) {
        return uomRepository.findAll(pageRequest);
    }

    public List<Uom> getAllProductUnits() {
      return   uomRepository.findAll();
    }

    public Uom createProductUnit(Uom productUnit) {
       return uomRepository.save(productUnit);
    }
}
