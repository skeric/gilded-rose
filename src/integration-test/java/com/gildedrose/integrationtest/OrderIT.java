package com.gildedrose.integrationtest;

import com.miw.gildedrose.GildedRoseApp;
import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.ResourceAccessException;

import java.net.HttpRetryException;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.miw.gildedrose.helper.AssertionHelper.assertPlacedOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = GildedRoseApp.class, webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:data.sql")
public class OrderIT {
    // TODO this is for demonstrating securing endpoint using basic authentication and not suggested used in production code
    @Value("${ordering.user:john}")
    private String user;

    // TODO this is for demonstrating securing endpoint using basic authentication and not suggested used in production code
    @Value("${ordering.password:doe}")
    private String password;

    @Value("${surgePricing.viewCountsTriggerPriceIncrease}")
    private Integer viewCountsTriggerPriceIncrease;

    @Value("${surgePricing.percentageIncrease}")
    private Integer percentageIncrease;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Given an item exists and has sufficient quantity for sell, when ordering that item, then an order is created")
    void givenItemWithSufficientQuantityExists_whenOrderThatItem_thenReturnsResponseWithOrderCreated() {
        HttpEntity<OrderDto> request = new HttpEntity<>(OrderDto.builder()
                .name("Candle")
                .price(2.0f)
                .quantity(2)
                .build());

        ResponseEntity<OrderDto> response = restTemplate.withBasicAuth(user, password)
                .exchange("/ordering/request", HttpMethod.POST, request, OrderDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertPlacedOrder(Objects.requireNonNull(request.getBody()), response.getBody());
    }

    @Test
    @DisplayName("Given an item exists and has sufficient quantity for sell and the price is increased, when ordering that item, then an order is created with increased price")
    void givenItemWithSufficientQuantityExistsAndPriceSurges_whenOrderThatItem_thenReturnsResponseWithOrderCreatedAndWithIncresedPrice() {
        String viewItemUrl = "/inventory/item/Candle";

        // the first time to view the item
        ResponseEntity<ItemDto> response = restTemplate.withBasicAuth(user, password)
                .getForEntity(viewItemUrl, ItemDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getPrice());
        Float basePrice = response.getBody().getPrice();

        // make another n times to view the same item such that the price will increase
        IntStream.rangeClosed(1, viewCountsTriggerPriceIncrease - 1).forEach(i -> {
            ResponseEntity<ItemDto> anotherResponse = restTemplate.getForEntity(viewItemUrl, ItemDto.class);
            assertEquals(HttpStatus.OK, anotherResponse.getStatusCode());
        });

        HttpEntity<OrderDto> request = new HttpEntity<>(OrderDto.builder()
                .name("Candle")
                .price(basePrice) // base price
                .quantity(1)
                .build());

        ResponseEntity<OrderDto> orderResponse = restTemplate.withBasicAuth(user, password)
                .exchange("/ordering/request", HttpMethod.POST, request, OrderDto.class);
        assertEquals(HttpStatus.CREATED, orderResponse.getStatusCode());
        assertNotNull(orderResponse.getBody());
        assertNotNull(orderResponse.getBody().getOrderNumber());
        assertNotNull(request.getBody());
        assertEquals(request.getBody().getName(), orderResponse.getBody().getName());
        assertEquals(request.getBody().getPrice() * (percentageIncrease + 100) / 100, orderResponse.getBody().getPrice());
        assertEquals(request.getBody().getQuantity(), orderResponse.getBody().getQuantity());
        assertTrue(StringUtils.isBlank(orderResponse.getBody().getErrorMessage()));
    }

    @Test
    @DisplayName("Given an item does not exist, when ordering that item using name, then return bad request")
    void givenItemNotExists_whenOrderThatItem_thenReturnsBadRequest() {
        HttpEntity<OrderDto> request = new HttpEntity<>(OrderDto.builder()
                .name("Superbowl")
                .price(2.0f)
                .quantity(2)
                .build());

        ResponseEntity<OrderDto> response = restTemplate.withBasicAuth(user, password)
                .exchange("/ordering/request", HttpMethod.POST, request, OrderDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Item Superbowl is not found", response.getBody().getErrorMessage());
        assertNull(response.getBody().getOrderNumber());
        assertNull(response.getBody().getPrice());
        assertNull(response.getBody().getQuantity());
    }

    @Test
    @DisplayName("Given an item exists but not sufficient quantity for sell, when ordering that item, then return bad request")
    void givenItemExists_whenOrderThatItemWithQuantityExceedingStockQuantity_thenReturnsBadRequest() {
        HttpEntity<OrderDto> request = new HttpEntity<>(OrderDto.builder()
                .name("Candle")
                .price(2.0f)
                .quantity(1000)
                .build());

        ResponseEntity<OrderDto> response = restTemplate.withBasicAuth(user, password)
                .exchange("/ordering/request", HttpMethod.POST, request, OrderDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Quantity for sell is 5 less than requested quantity 1000", response.getBody().getErrorMessage());
        assertNull(response.getBody().getOrderNumber());
        assertNull(response.getBody().getPrice());
        assertNull(response.getBody().getQuantity());
    }

    @Test
    @DisplayName("Given an item exists,when invalid user orders, then return authorized")
    void givenItemExists_whenInvalidUserOrders_thenReturnUnauthorized() {
        HttpEntity<OrderDto> request = new HttpEntity<>(OrderDto.builder()
                .name("Candle")
                .price(2.0f)
                .quantity(1000)
                .build());

        Exception thrown = assertThrows(ResourceAccessException.class, () -> {
            ResponseEntity<OrderDto> response = restTemplate.withBasicAuth("john", "doe")
                    .exchange("/ordering/request", HttpMethod.POST, request, OrderDto.class);
        });

        assertTrue(thrown.getCause() instanceof HttpRetryException);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), ((HttpRetryException) thrown.getCause()).responseCode());
    }
}
