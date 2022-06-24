package com.gildedrose.integrationtest;

import com.miw.gildedrose.GildedRoseApp;
import com.miw.gildedrose.dto.ItemDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = GildedRoseApp.class, webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:data.sql")
public class InventoryIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Given an item exists, when finding that item by name, then return response ok with that item")
    void givenItemExists_whenFindItemByName_thenReturnsResponseWithThatItem() {
        ResponseEntity<ItemDto> response = restTemplate.getForEntity("/inventory/item/Candle", ItemDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Candle", response.getBody().getName());
        assertEquals("Home", response.getBody().getDescription());
        assertEquals(2.0f, response.getBody().getPrice());
    }

    @Test
    @DisplayName("Given an item does not exist, when finding that item by name, then return not found")
    void givenAnItemNotExist_whenFindItemByName_thenReturnsNotFoundResponse() {
        ResponseEntity<ItemDto> response = restTemplate.getForEntity("/inventory/item/Superbowl", ItemDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Given multiple items exist, when finding all items, then return response ok with all those items")
    void givenMultipleItemsExist_whenFindAllItems_thenReturnsResponseWithExistingItems() {
        ParameterizedTypeReference<List<ItemDto>> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<ItemDto>> response = restTemplate.exchange("/inventory/items",
                HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(CollectionUtils.isEmpty(response.getBody()));
        assertEquals(5, response.getBody().size());

        response.getBody().forEach(dto -> {
            assertNotNull(dto.getId());
            assertFalse(StringUtils.isBlank(dto.getName()));
            assertFalse(StringUtils.isBlank(dto.getDescription()));
            assertNotNull(dto.getPrice());
        });
    }

}
