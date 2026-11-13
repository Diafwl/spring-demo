package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Item;
import com.example.demo.service.ItemNotFoundException;
import com.example.demo.service.ItemService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
class ItemControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ItemService service;

    private static Item item(long id, String name, int quantity) {
        Item item = new Item(name, quantity);
        item.setId(id);
        return item;
    }

    @Test
    void listReturnsItems() throws Exception {
        given(service.list()).willReturn(List.of(item(1, "apple", 3)));

        mvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("apple"));
    }

    @Test
    void createReturns201WithLocation() throws Exception {
        given(service.create("pear", 7)).willReturn(item(42, "pear", 7));

        mvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"pear\",\"quantity\":7}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/items/42"))
                .andExpect(jsonPath("$.id").value(42));
    }

    @Test
    void createRejectsBlankName() throws Exception {
        mvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"quantity\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("name: name is required"));
    }

    @Test
    void missingItemReturns404() throws Exception {
        given(service.get(anyLong())).willThrow(new ItemNotFoundException(99));

        mvc.perform(get("/api/items/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Item not found: 99"));
    }
}
