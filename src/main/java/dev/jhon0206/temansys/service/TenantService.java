package dev.jhon0206.temansys.service;

import dev.jhon0206.temansys.dao.TenantDao;
import dev.jhon0206.temansys.dto.TenantDto;
import dev.jhon0206.temansys.exception.ValidationException;
import dev.jhon0206.temansys.model.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TenantService {

    private final TenantDao dao;

    public TenantService(TenantDao dao) {
        this.dao = dao;
    }

    public TenantDto create(TenantDto dto) {
        validate(dto);
        if (dao.findByDni(dto.dni()).isPresent()) {
            throw new ValidationException("A tenant with DNI %s already exists".formatted(dto.dni()));
        }
        return toDto(dao.save(toEntity(dto)));
    }

    public TenantDto update(TenantDto dto) {
        if (dto.id() == null) {
            throw new ValidationException("Id is required to update");
        }
        validate(dto);
        dao.findByDni(dto.dni())
                .filter(other -> !other.getId().equals(dto.id()))
                .ifPresent(other -> {
                    throw new ValidationException("DNI %s belongs to another tenant".formatted(dto.dni()));
                });
        if (!dao.update(toEntity(dto))) {
            throw new ValidationException("Tenant %d not found".formatted(dto.id()));
        }
        return dto;
    }

    public Optional<TenantDto> findByDni(String dni) {
        return dao.findByDni(dni).map(TenantService::toDto);
    }

    public Optional<TenantDto> findById(int id) {
        return dao.findById(id).map(TenantService::toDto);
    }

    public List<TenantDto> findAll() {
        return dao.findAll().stream().map(TenantService::toDto).toList();
    }

    public void delete(int id) {
        if (!dao.deleteById(id)) {
            throw new ValidationException("Tenant %d not found".formatted(id));
        }
    }

    private static void validate(TenantDto dto) {
        if (dto.dni() == null || !dto.dni().matches("\\d{8}")) {
            throw new ValidationException("DNI must have exactly 8 digits");
        }
        requireText(dto.firstName(), "First name");
        requireText(dto.paternalSurname(), "Paternal surname");
        requireText(dto.maternalSurname(), "Maternal surname");
        if (dto.debt() == null || dto.debt().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Debt must be zero or positive");
        }
        if (dto.entryDate() == null) {
            throw new ValidationException("Entry date is required");
        }
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("%s is required".formatted(field));
        }
    }

    private static Tenant toEntity(TenantDto d) {
        return new Tenant(d.id(), d.dni(), d.firstName(), d.paternalSurname(),
                d.maternalSurname(), d.phone(), d.email(), d.debt(), d.entryDate());
    }

    private static TenantDto toDto(Tenant t) {
        return new TenantDto(t.getId(), t.getDni(), t.getFirstName(), t.getPaternalSurname(),
                t.getMaternalSurname(), t.getPhone(), t.getEmail(), t.getDebt(), t.getEntryDate());
    }
}
