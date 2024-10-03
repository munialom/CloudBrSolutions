package com.ctecx.brsuite.chartofaccounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountChartRepository extends JpaRepository<AccountChart, Integer>,CustomAccountChartRepository {

    List<AccountChart> findByBankAccountTrue();

    List<AccountChart> findByNameContainingIgnoreCase(String keyword);

    List<AccountChart> findByNameContainingIgnoreCaseAndAccountGroupEnumIn(String keyword, List<AccountGroup> accountGroups);

    @Query("SELECT MAX(ac.accountCode) FROM AccountChart ac WHERE ac.accountGroupEnum = :accountGroup")
    Integer findHighestCodeByAccountGroup(@Param("accountGroup") AccountGroup accountGroup);

    List<AccountChart> findByParentIsNull();

    boolean existsByName(String name);
    Optional<AccountChart> findByName(String name);







}