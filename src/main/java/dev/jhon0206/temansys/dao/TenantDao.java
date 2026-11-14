package dev.jhon0206.temansys.dao;

import dev.jhon0206.temansys.model.Tenant;

import java.util.Optional;

public interface TenantDao extends GenericDao<Tenant, Integer>{
    Optional<Tenant> findByDni(String dni);
}
