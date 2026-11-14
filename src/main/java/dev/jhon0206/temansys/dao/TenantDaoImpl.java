package dev.jhon0206.temansys.dao;

import dev.jhon0206.temansys.config.DatabaseConnection;
import dev.jhon0206.temansys.exception.DataAccessException;
import dev.jhon0206.temansys.model.Tenant;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TenantDaoImpl implements TenantDao {

    private static final String SELECT_ALL = """
            SELECT
                idinquilinos, dni, nombres, paterno, materno, telefono, correo, deuda, fecha_ingreso
            FROM inquilinos
            """;
    private static final String SELECT_BY_ID = SELECT_ALL.concat(" WHERE idinquilinos = ?");
    private static final String SELECT_BY_DNI = SELECT_ALL.concat(" WHERE dni = ?");
    private static final String INSERT_SQL = """
            INSERT INTO inquilinos
                (dni, nombres, paterno, materno, telefono, correo, deuda, fecha_ingreso)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE inquilinos SET dni = ?, nombres = ?, paterno = ?, materno = ?,
                                  telefono = ?, correo = ?, deuda = ?, fecha_ingreso = ?
            WHERE idinquilinos = ?
            """;
    private static final String DELETE_SQL = "DELETE FROM inquilinos WHERE idinquilinos = ?";

    private final DatabaseConnection db;

    public TenantDaoImpl() {
        this.db = DatabaseConnection.getInstance();
    }

    @Override
    public Tenant save(Tenant tenant) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, tenant);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    tenant.setId(keys.getInt(1));
                }
            }
            return tenant;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving tenant " + tenant.getDni(), e);
        }
    }

    @Override
    public Optional<Tenant> findByDni(String dni) {
        return findOne(SELECT_BY_DNI, dni);
    }

    @Override
    public Optional<Tenant> findById(Integer id) {
        return findOne(SELECT_BY_ID, id);
    }


    private void bind(PreparedStatement ps, Tenant t) throws SQLException {
        ps.setString(1, t.getDni());
        ps.setString(2, t.getFirstName());
        ps.setString(3, t.getPaternalSurname());
        ps.setString(4, t.getMaternalSurname());
        ps.setString(5, t.getPhone());
        ps.setString(6, t.getEmail());
        ps.setBigDecimal(7, t.getDebt());
        ps.setObject(8, t.getEntryDate());
    }

    @Override
    public boolean update(Tenant tenant) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            bind(ps, tenant);
            ps.setInt(9, tenant.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating tenant " + tenant.getId(), e);
        }
    }

    @Override
    public List<Tenant> findAll() {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            List<Tenant> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error listing tenants", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting tenant " + id, e);
        }
    }

    private static Tenant mapper(ResultSet rs) throws SQLException {
        return new Tenant(
                rs.getInt("idinquilinos"),
                rs.getString("dni"),
                rs.getString("nombres"),
                rs.getString("paterno"),
                rs.getString("materno"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getBigDecimal("deuda"),
                rs.getObject("fecha_ingreso", LocalDate.class));
    }

    private Optional<Tenant> findOne(String sql, Object param) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapper(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error querying tenant", e);
        }
    }
}
