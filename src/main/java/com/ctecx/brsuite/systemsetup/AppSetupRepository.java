package com.ctecx.brsuite.systemsetup;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppSetupRepository extends CrudRepository<AppSetup, String> {
    List<AppSetup> findBySetupCategory(SetupCategory category);
}
