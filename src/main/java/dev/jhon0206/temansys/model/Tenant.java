package dev.jhon0206.temansys.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Tenant {
    private Integer id;
    private final String dni;
    private final String firstName;
    private final String paternalSurname;
    private final String maternalSurname;
    private final String phone;
    private final String email;
    private final BigDecimal debt;
    private final LocalDate entryDate;

    public Tenant(Integer id, String dni, String firstName, String paternalSurname, String maternalSurname, String phone, String email, BigDecimal debt, LocalDate entryDate) {
        this.id = id;
        this.dni = dni;
        this.firstName = firstName;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.phone = phone;
        this.email = email;
        this.debt = debt;
        this.entryDate = entryDate;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }
}
