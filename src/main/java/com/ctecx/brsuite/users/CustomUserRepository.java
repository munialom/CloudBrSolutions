package com.ctecx.brsuite.users;

import java.util.List;
import java.util.Map;

public interface CustomUserRepository {
    List<Map<String, Object>> getUserInformationSearch(String searchName);
}