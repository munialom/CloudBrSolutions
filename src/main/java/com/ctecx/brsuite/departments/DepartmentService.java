package com.ctecx.brsuite.departments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> departmentList() {
        return departmentRepository.findAll();
    }

    public Page<Department> getAllDepartmentsPaged(PageRequest pageRequest) {
        return departmentRepository.findAll(pageRequest);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public List<Department> searchDepartments(String keyword) {
        return departmentRepository.findByNameContainingIgnoreCase(keyword);
    }

}
