package com.miw.gildedrose.controller;

import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.exception.BadRequestException;
import com.miw.gildedrose.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("ordering")
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping(value = "/request", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<OrderDto> buy(@RequestBody OrderDto request) {
        try {
            OrderDto result = orderFacade.order(request);
            return ResponseEntity.created(buildCreatedOrderUri(request.getOrderNumber())).body(result);
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest()
                    .body(OrderDto.builder()
                            .name(request.getName())
                            .errorMessage(ex.getMessage())
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(OrderDto.builder()
                            .name(request.getName())
                            .errorMessage(ex.getMessage())
                            .build());
        }
    }

    private URI buildCreatedOrderUri(String orderNumber) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        return uriComponentsBuilder.path("/createdOrder/{orderNumber}").buildAndExpand(orderNumber).toUri();
    }
}
