package com.miw.gildedrose.facade;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.service.OrderService;
import com.miw.gildedrose.service.SurgePriceService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderFacadeTest {
    @MockBean
    private OrderService orderServiceMock;

    @MockBean
    private SurgePriceService surgePriceServiceMock;

    private OrderFacade orderFacade;
    private OrderDto request;

    @BeforeEach
    void setup() {
        orderFacade = new OrderFacade(orderServiceMock, surgePriceServiceMock);

        request = OrderDto.builder()
                .name("Old Typewriter")
                .price(100.00f)
                .quantity(1)
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Order successfully")
    void orderSuccessfully() {
        // given
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name(request.getName())
                .description("Office Supplies")
                .price(request.getPrice())
                .build();

        when(orderServiceMock.findItem(request.getName())).thenReturn(itemDto);
        Float latestPrice = 110f;
        when(surgePriceServiceMock.adjustPrice(itemDto.getId(), request.getPrice()))
                .thenReturn(latestPrice);

        ArgumentCaptor<OrderDto> argumentCaptorOrderDto = ArgumentCaptor.forClass(OrderDto.class);

        // when
        OrderDto result = orderFacade.order(request);

        // then
        verify(orderServiceMock).order(argumentCaptorOrderDto.capture(), eq(itemDto.getId()));
        assertEquals(itemDto.getName(), argumentCaptorOrderDto.getValue().getName());
        assertEquals(latestPrice, argumentCaptorOrderDto.getValue().getPrice());
        assertEquals(request.getQuantity(), argumentCaptorOrderDto.getValue().getQuantity());
        assertNull(argumentCaptorOrderDto.getValue().getOrderNumber());

        verify(orderServiceMock).order(argumentCaptorOrderDto.getValue(), itemDto.getId());
    }

    @Test
    @DisplayName("Fail to order due to item not found")
    void failToOrderNotExistItem() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("Fail to order due to not enough item quantity")
    void failToOrderNotEnoughQuantityForSell() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("Fail to order due to not enough item quantity when order is being processed")
    void failToOrderBecauseOfQuantityForSellDropped() {
        // TODO implementation
        assertTrue(true);
    }

}
