package com.ctecx.brsuite.chartofaccounts;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class CustomAccountChartRepositoryImpl implements CustomAccountChartRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;





    @Override
    public List<Map<String, Object>> getAllAccountCharts() {
        return jdbcTemplate.queryForList("CALL get_chartofaccounts()");
    }
}
