package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class
)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    private final UserDto userDto = new UserDto(1, "name", "name@mail.ru");

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(isA(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).createUser(isA(UserDto.class));
    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(userService).deleteUser(anyInt());

        mockMvc.perform(
                delete("/users/1")
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(1);
    }

    @Test
    void getUser() throws Exception {
        Mockito.when(userService.getUser(anyInt())).thenReturn(userDto);

        mockMvc.perform(
                get("/users/1")
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).getUser(1);
    }

    @Test
    void getAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(
                get("/users")
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
    }

    @Test
    void pathUser() throws Exception {
        Mockito.when(userService.patchUser(isA(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).patchUser(isA(UserDto.class));
    }
}