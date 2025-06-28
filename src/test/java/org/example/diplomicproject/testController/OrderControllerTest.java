package org.example.diplomicproject.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.diplomicproject.domain.OrderStatus;
import org.example.diplomicproject.controller.OrderController;
import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = OrderController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllOrders() throws Exception {
        List<Order> orders = List.of(new Order(1L, OrderStatus.NEW, new User(), List.of()), new Order(2L, OrderStatus.PROCESSING, new User(), List.of()));
        given(orderService.findAll()).willReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGetOrderById_found() throws Exception {
        Order order = new Order(1L, OrderStatus.NEW, new User(), List.of());
        given(orderService.findById(1L)).willReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetOrderById_notFound() throws Exception {
        given(orderService.findById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateOrUpdateOrder() throws Exception {
        Order order = new Order(null, OrderStatus.NEW, new User(), List.of());
        Order savedOrder = new Order(1L, OrderStatus.NEW, new User(), List.of());

        given(orderService.save(any(Order.class))).willReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteOrder_found() throws Exception {
        given(orderService.existsById(1L)).willReturn(true);
        doNothing().when(orderService).deleteById(1L);

        mockMvc.perform(delete("/api/orders/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteOrder_notFound() throws Exception {
        given(orderService.existsById(99L)).willReturn(false);

        mockMvc.perform(delete("/api/orders/99").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateOrderStatus_success() throws Exception {
        doNothing().when(orderService).updateStatus(1L, OrderStatus.SHIPPED);

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "SHIPPED")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOrderStatus_notFound() throws Exception {
        doThrow(new RuntimeException("Not found")).when(orderService).updateStatus(99L, OrderStatus.CANCELLED);

        mockMvc.perform(patch("/api/orders/99/status")
                        .param("status", "CANCELLED")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
