package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ItemRequestController.class
)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .itemId(1)
                .itemName("item name")
                .description("description")
                .requestorId(1)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void createRequest() throws Exception {
        Mockito.when(itemRequestService.createItemRequest(isA(ItemRequestDto.class))).thenReturn(itemRequestDto);

        mockMvc.perform(
                post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
        ).andExpect(status().isOk());

        Mockito.verify(itemRequestService, times(1)).createItemRequest(isA(ItemRequestDto.class));
    }

    @Test
    void getItemRequests() throws Exception {
        Mockito.when(itemRequestService.getItemRequests(anyInt())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(
                get("/requests")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isOk());

        Mockito.verify(itemRequestService, times(1)).getItemRequests(1);
    }

    @Test
    void testGetItemRequests() throws Exception {
        Mockito.when(itemRequestService.getItemRequests(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(
                get("/requests/all")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
                        .queryParam("from", "1")
                        .queryParam("size", "10")
        ).andExpect(status().isOk());

        Mockito.verify(itemRequestService, times(1)).getItemRequests(1, 10, 1);
    }

    @Test
    void getItemRequest() throws Exception {
        Mockito.when(itemRequestService.getItemRequest(anyInt(), anyInt())).thenReturn(itemRequestDto);

        mockMvc.perform(
                get("/requests/1")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isOk());

        Mockito.verify(itemRequestService, times(1)).getItemRequest(1, 1);
    }
}