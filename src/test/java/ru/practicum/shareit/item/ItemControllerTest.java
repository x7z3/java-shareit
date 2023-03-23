package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.exception.WrongItemOwnerException;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ChangingItemOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ItemController.class
)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto commentDto;

    public ItemControllerTest() {
        itemDto = ItemDto.builder()
                .id(1)
                .available(true)
                .name("item")
                .description("description")
                .ownerId(1)
                .build();

        commentDto = CommentDto.builder()
                .itemId(1)
                .authorName("commentAuthor")
                .created(LocalDateTime.now())
                .text("comment text")
                .userId(2)
                .build();
    }

    @Test
    void createItem() throws Exception {
        Mockito.when(
                itemService.createItem(isA(ItemDto.class))
        ).thenReturn(itemDto);

        mockMvc.perform(
                post("/items")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).createItem(itemDto);
    }

    @Test
    void deleteItem() throws Exception {
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(itemService).deleteItem(1);

        mockMvc.perform(
                delete("/items/1")
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).deleteItem(1);
    }

    @Test
    void patchItem() throws Exception {
        Mockito.when(itemService.patchItem(isA(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(
                patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, "1")
                        .content(mapper.writeValueAsString(itemDto))
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).patchItem(itemDto);
    }

    @Test
    void patchItem_throwExceptionChangingItemOwnerException() throws Exception {
        Mockito.when(itemService.patchItem(isA(ItemDto.class))).thenThrow(ChangingItemOwnerException.class);

        mockMvc.perform(
                patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, "1")
                        .content(mapper.writeValueAsString(itemDto))
        ).andExpect(status().isForbidden());
    }

    @Test
    void patchItem_throwExceptionWrongItemOwner() throws Exception {
        Mockito.when(itemService.patchItem(isA(ItemDto.class))).thenThrow(WrongItemOwnerException.class);

        mockMvc.perform(
                patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, "1")
                        .content(mapper.writeValueAsString(itemDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    void getAllItems() throws Exception {
        Mockito.when(itemService.getAllItems(anyInt())).thenReturn(List.of(itemDto));

        mockMvc.perform(
                get("/items")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).getAllItems(1);
    }

    @Test
    void getItem() throws Exception {
        Mockito.when(itemService.getItem(anyInt(), anyInt())).thenReturn(itemDto);

        mockMvc.perform(
                get("/items/1")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).getItem(1, 1);
    }

    @Test
    void getItem_whenWrongItemAcquired_thenThrowException() throws Exception {
        Mockito.when(itemService.getItem(anyInt(), anyInt())).thenThrow(ItemNotFoundException.class);

        mockMvc.perform(
                get("/items/11")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isNotFound());
    }

    @Test
    void searchItem() throws Exception {
        Mockito.when(itemService.searchItem(anyString())).thenReturn(anyList());

        mockMvc.perform(
                get("/items/search").param("text", "abc")
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).searchItem("abc");
    }

    @Test
    void addItemComment() throws Exception {
        Mockito.when(itemService.addItemComment(isA(CommentDto.class), anyInt())).thenReturn(commentDto);

        mockMvc.perform(
                post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto))
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 1)
        ).andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1)).addItemComment(commentDto, 1);
    }
}