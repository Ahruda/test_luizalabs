package com.luizalabs.orders.infrastructure.rest;

import com.luizalabs.orders.application.file.OrdersFileProcessorUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping(value = "orders/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public class OrdersFileProcessorController {

    private final OrdersFileProcessorUseCase ordersFileProcessorUseCase;

    public OrdersFileProcessorController(OrdersFileProcessorUseCase ordersFileProcessorUseCase) {
        this.ordersFileProcessorUseCase = ordersFileProcessorUseCase;
    }

    @PostMapping
    public ResponseEntity<?> process(@RequestParam("file") MultipartFile file) {

        final var input = new OrdersFileProcessorUseCase.Input(file);

        ordersFileProcessorUseCase.execute(input);

        return ResponseEntity.created(URI.create("/orders")).build();
    }


}
