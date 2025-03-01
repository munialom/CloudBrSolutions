package com.ctecx.brsuite.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controller
@RestController
@RequestMapping("/api/users")
public class UserSearchController {

    private final UserSearchService userSearchService;

    @Autowired
    public UserSearchController(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> getAllRoleNineUsers(
            @RequestParam(value = "searchName", required = false) String searchName) {
        List<Map<String, Object>> userInfo = userSearchService.getUserInformationSearch(searchName);
        return ResponseEntity.ok(userInfo);
    }
}