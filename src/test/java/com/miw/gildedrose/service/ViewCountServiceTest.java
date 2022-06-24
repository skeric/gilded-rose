package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import com.miw.gildedrose.repository.ItemViewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ViewCountServiceTest {
    @MockBean
    private ItemViewRepository itemViewRepositoryMock;

    @Autowired
    private ViewCountService viewCountService;

    @Test
    @DisplayName("Update view count")
    void updateViewCount() {
        // given
        ItemViewDto dto = ItemViewDto.builder().itemId(10).build();
        ArgumentCaptor<ItemView> itemViewArgumentCaptor = ArgumentCaptor.forClass(ItemView.class);

        // when
        viewCountService.addViewCount(dto);

        // then
        verify(itemViewRepositoryMock).save(itemViewArgumentCaptor.capture());
        assertEquals(dto.getItemId(), itemViewArgumentCaptor.getValue().getItemId());
        assertNotNull(itemViewArgumentCaptor.getValue().getCreatedAt());
    }
}
