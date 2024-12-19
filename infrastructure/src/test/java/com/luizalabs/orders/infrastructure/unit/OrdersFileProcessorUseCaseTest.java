package com.luizalabs.orders.infrastructure.unit;

import com.luizalabs.orders.application.file.OrdersFileProcessorUseCase;
import com.luizalabs.orders.domain.exception.FileInvalidException;
import com.luizalabs.orders.domain.exception.OrderNotFoundException;
import com.luizalabs.orders.domain.order.Order;
import com.luizalabs.orders.domain.product.Product;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrdersFileProcessorUseCaseTest {

    @Mock
    private UserOrdersRepository userOrdersRepository;

    @InjectMocks
    private OrdersFileProcessorUseCase ordersFileProcessorUseCase;

    @Captor
    ArgumentCaptor<List<UserOrder>> captor;

    @Test
    @DisplayName("Should be process a valid file")
    void processAndSaveFileOrders() throws Exception {
        String fileContent = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("text/plain");

        OrdersFileProcessorUseCase.Input input = new OrdersFileProcessorUseCase.Input(file);
        ordersFileProcessorUseCase.execute(input);

        verify(userOrdersRepository, times(1)).create(captor.capture());
        List<UserOrder> userOrders = captor.getValue();

        // Assertions
        assertEquals(1, userOrders.size());
        UserOrder userOrder = userOrders.get(0);
        assertEquals(70L, userOrder.getUserId());
        assertEquals("Palmer Prosacco", userOrder.getName());

        assertEquals(1, userOrder.getOrders().size());
        Order order = userOrder.getOrders().get(0);
        assertEquals(753L, order.getOrderId());
        assertEquals(LocalDate.of(2021, 3, 8), order.getDate());

        assertEquals(1, order.getProducts().size());
        Product product = order.getProducts().get(0);
        assertEquals(3L, product.getProductIdFile());
        assertEquals(1836.74, product.getValue());
    }

    @Test
    @DisplayName("Should be process one order with two products")
    void processOneOrderWithTwoProducts() throws Exception {
        String fileContent =
                "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n"+
                "0000000070                              Palmer Prosacco00000007530000000004     1009.5420210308";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("text/plain");

        OrdersFileProcessorUseCase.Input input = new OrdersFileProcessorUseCase.Input(file);
        ordersFileProcessorUseCase.execute(input);

        verify(userOrdersRepository, times(1)).create(captor.capture());
        List<UserOrder> userOrders = captor.getValue();

        // Assertions
        assertEquals(1, userOrders.size());
        final var userOrder = userOrders.get(0);
        assertEquals(70L, userOrder.getUserId());
        assertEquals("Palmer Prosacco", userOrder.getName());

        assertEquals(1, userOrder.getOrders().size());
        final var order = userOrder.getOrders().get(0);
        assertEquals(753L, order.getOrderId());
        assertEquals(LocalDate.of(2021, 3, 8), order.getDate());

        assertEquals(2, order.getProducts().size());
        final var product1 = order.getProducts().get(0);
        assertEquals(3L, product1.getProductIdFile());
        assertEquals(1836.74, product1.getValue());

        final var product2 = order.getProducts().get(1);
        assertEquals(4L, product2.getProductIdFile());
        assertEquals(1009.54, product2.getValue());
    }

    @Test
    @DisplayName("Should be throw exception when file is empty")
    void throwExceptionWhenFileIsEmpty() throws Exception {

        String fileContent = "";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("text/plain");

        OrdersFileProcessorUseCase.Input input = new OrdersFileProcessorUseCase.Input(file);

        assertThrows(FileInvalidException.class, () -> ordersFileProcessorUseCase.execute(input));

        verifyNoInteractions(userOrdersRepository);
    }

    @Test
    @DisplayName("Should be throw exception when file is invalid")
    void throwExceptionWhenFileIsInvalid() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/json");

        OrdersFileProcessorUseCase.Input input = new OrdersFileProcessorUseCase.Input(file);
        assertThrows(FileInvalidException.class, () -> ordersFileProcessorUseCase.execute(input));

        verifyNoInteractions(userOrdersRepository);
    }
}
