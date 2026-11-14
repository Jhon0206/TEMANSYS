package dev.jhon0206.temansys.controller;

import dev.jhon0206.temansys.dto.TenantDto;
import dev.jhon0206.temansys.service.TenantService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TenantController implements AutoCloseable{

    private final TenantService service;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public TenantController(TenantService service) {
        this.service = service;
    }

    public CompletableFuture<TenantDto> create(TenantDto dto) {
        return CompletableFuture.supplyAsync(() -> service.create(dto), executor);
    }

    public CompletableFuture<TenantDto> update(TenantDto dto) {
        return CompletableFuture.supplyAsync(() -> service.update(dto), executor);
    }

    public CompletableFuture<Optional<TenantDto>> findById(int id) {
        return CompletableFuture.supplyAsync(() -> service.findById(id), executor);
    }

    public CompletableFuture<List<TenantDto>> findAll() {
        return CompletableFuture.supplyAsync(service::findAll, executor);
    }

    public CompletableFuture<Void> delete(int id) {
        return CompletableFuture.runAsync(() -> service.delete(id), executor);
    }

    @Override
    public void close() throws Exception {
        executor.close();
    }
}
