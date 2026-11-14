package dev.jhon0206.temansys.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao <T,ID>{
    T save(T entity);
    boolean update(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
}
