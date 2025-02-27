package com.ctecx.brsuite.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor

public class UserSearchService {

    private final CustomUserRepository customUserRepository;


    public List<Map<String, Object>> getUserInformationSearch(String searchName) {
        return customUserRepository.getUserInformationSearch(searchName);
    }
}
