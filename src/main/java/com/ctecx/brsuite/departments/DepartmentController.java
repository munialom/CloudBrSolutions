package com.ctecx.brsuite.departments;

import com.ctecx.brsuite.util.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public ResponseEntity<DataTableResponse<Department>> getAllDepartments(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length
    ) {
        PageRequest pageRequest = PageRequest.of(start / length, length);
        Page<Department> departmentsPage = departmentService.getAllDepartmentsPaged(pageRequest);

        DataTableResponse<Department> response = new DataTableResponse<>();
        response.setData(departmentsPage.getContent());
        response.setDraw(draw);
        response.setRecordsTotal(departmentsPage.getTotalElements());
        response.setRecordsFiltered(departmentsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/search", produces = "application/json")
    public List<Department> searchDepartments(@RequestParam String keyword) {
        return departmentService.searchDepartments(keyword);
    }

}
