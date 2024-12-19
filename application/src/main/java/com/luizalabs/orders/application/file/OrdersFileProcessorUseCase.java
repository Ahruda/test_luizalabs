package com.luizalabs.orders.application.file;

import com.luizalabs.orders.application.UnitUseCase;
import com.luizalabs.orders.domain.exception.FileInvalidException;
import com.luizalabs.orders.domain.file.OrderFile;
import com.luizalabs.orders.domain.order.Order;
import com.luizalabs.orders.domain.product.Product;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class OrdersFileProcessorUseCase implements UnitUseCase<OrdersFileProcessorUseCase.Input> {

    private final UserOrdersRepository userOrdersRepository;

    public OrdersFileProcessorUseCase(UserOrdersRepository userOrdersRepository) {
        this.userOrdersRepository = userOrdersRepository;
    }

    @Override
    public void execute(Input input) {
        log.info("I=Starting_execution c=OrdersFileProcessorUseCase m=execute");

        fileTypeIsValid(input.file.getContentType());

        List<OrderFile> ordersFileList = readFile(input.file);

        if (ordersFileList.isEmpty()) {
            throw new FileInvalidException("File empty or content invalid");
        }

        List<UserOrder> userOrders = extractUsersOrders(ordersFileList);
        userOrdersRepository.create(userOrders);
        log.info("I=File_processed_successfully c=OrdersFileProcessorUseCase m=execute");
    }

    private List<OrderFile> readFile(MultipartFile file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            var lines = 0;

            List<OrderFile> ordersFile = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                ordersFile.add(parseLineToOrderFile(line));
                lines++;
            }
            log.info("I=File_read c=OrdersFileProcessorUseCase m=execute totalLines={}", lines);

            return ordersFile;
        } catch (IOException e) {
            log.error("E=Error_processing_file c=OrdersFileProcessorUseCase m=execute");
        }

        throw new FileInvalidException("Error processing file");
    }

    private List<UserOrder> extractUsersOrders(List<OrderFile> ordersFile) {
        Map<Long, UserOrder> userMap = new HashMap<>();

        ordersFile.forEach(orderFile -> {
            userMap.putIfAbsent(orderFile.getUserId(),
                    UserOrder.builder()
                            .userId(orderFile.getUserId())
                            .name(orderFile.getName())
                            .orders(new ArrayList<>())
                            .build()
            );

            var user = userMap.get(orderFile.getUserId());

            var existingOrder = user.getOrders().stream()
                    .filter(order -> order.getOrderId().equals(orderFile.getOrderId()))
                    .findFirst()
                    .orElseGet(() -> {
                        var newOrder = Order.builder()
                                .orderId(orderFile.getOrderId())
                                .date(orderFile.getDate())
                                .products(new ArrayList<>())
                                .build();
                        user.getOrders().add(newOrder);
                        return newOrder;
                    });

            var orderProduct = Product.builder()
                    .productIdFile(orderFile.getProductId())
                    .value(orderFile.getValueProduct())
                    .build();

            existingOrder.getProducts().add(orderProduct);
        });

        return new ArrayList<>(userMap.values());
    }

    private OrderFile parseLineToOrderFile(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return OrderFile.builder()
                .userId(Long.valueOf(line.substring(0, 10)))
                .name(line.substring(10, 55).trim())
                .orderId(Long.valueOf(line.substring(55, 65)))
                .productId(Long.valueOf(line.substring(65, 75)))
                .valueProduct(Double.valueOf(line.substring(75, 87)))
                .date(LocalDate.parse(line.substring(87, 95), formatter))
                .build();
    }

    private void fileTypeIsValid(String contentType) {

        Set<String> allowedTypes = Set.of("text/plain", "text/csv");

        if (!allowedTypes.contains(contentType)) {
            throw new FileInvalidException("Invalid file type: " + contentType + ". Only text/plain and text/csv are allowed.");
        }
    }

    public record Input(MultipartFile file) {}
}
