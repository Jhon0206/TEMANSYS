package dev.jhon0206.temansys;

import dev.jhon0206.temansys.controller.TenantController;
import dev.jhon0206.temansys.dao.TenantDaoImpl;
import dev.jhon0206.temansys.dto.TenantDto;
import dev.jhon0206.temansys.service.TenantService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TemansysApp {
    private static final System.Logger LOG = System.getLogger(TemansysApp.class.getName());

    static void main() {
        try (var controller = new TenantController(new TenantService(new TenantDaoImpl()))) {

            var newTenant = new TenantDto(null, "12345678", "Juan", "Perez", "Lopez",
                    "999888777", "juan@mail.com", new BigDecimal("150.50"), LocalDate.now());

            controller.create(newTenant)
                    .thenCompose(saved -> {
                        LOG.log(System.Logger.Level.INFO, "Saved: {0}", saved);
                        return controller.findAll();
                    })
                    .thenAccept(list -> list.forEach(t ->
                            LOG.log(System.Logger.Level.INFO, "Tenant: {0}", t)))
                    .exceptionally(ex -> {
                        LOG.log(System.Logger.Level.ERROR, "Operation failed", ex);
                        return null;
                    })
                    .join();
        } catch (Exception e) {
            LOG.log(System.Logger.Level.ERROR, e.getMessage());
        }
    }
}
