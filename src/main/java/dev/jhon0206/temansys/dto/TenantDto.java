package dev.jhon0206.temansys.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TenantDto(
        Integer id,
        String dni,
        String firstName,
        String paternalSurname,
        String maternalSurname,
        String phone,
        String email,
        BigDecimal debt,
        LocalDate entryDate) {
}
