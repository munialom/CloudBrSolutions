package com.ctecx.brsuite.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

// Repository Implementation
@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomUserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getUserInformationSearch(String searchName) {

        return jdbcTemplate.queryForList("CALL SearchUsersByRoleAndName(?)", searchName);
    }
}