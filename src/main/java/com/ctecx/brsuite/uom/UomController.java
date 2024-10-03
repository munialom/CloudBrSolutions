package com.ctecx.brsuite.uom;

import com.ctecx.brsuite.util.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/uoms")
public class UomController {

    private final UomService uomService;

    @Autowired
    public UomController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public ResponseEntity<DataTableResponse<Uom>> getAllUom(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length
    ) {
        PageRequest pageRequest = PageRequest.of(start / length, length);
        Page<Uom> branchesPage = uomService.getAllUomsPaged(pageRequest);

        DataTableResponse<Uom> response = new DataTableResponse<>();
        response.setData(branchesPage.getContent());
        response.setDraw(draw);
        response.setRecordsTotal(branchesPage.getTotalElements());
        response.setRecordsFiltered(branchesPage.getTotalElements());

        return ResponseEntity.ok(response);
    }




    @GetMapping("/{id}")
    public ResponseEntity<Uom> getUomById(@PathVariable Long id) {
        Optional<Uom> uom = uomService.getUomById(id);
        return uom.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Uom createUom(@RequestBody Uom uom) {
        return uomService.createUom(uom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Uom> updateUom(@PathVariable Long id, @RequestBody Uom updatedUom) {
        Uom uom = uomService.updateUom(id, updatedUom);
        return uom != null ? ResponseEntity.ok(uom) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUom(@PathVariable Long id) {
        uomService.deleteUom(id);
        return ResponseEntity.noContent().build();
    }
}
