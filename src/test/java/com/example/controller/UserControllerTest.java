package com.example.controller;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "Name", value = "ADMIN")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setup(){
       user = User.builder()
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();

        userRepository.deleteAll();
    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception{
        User userTest =  userRepository.save(user);

        ResultActions response = mockMvc.perform(get("/users/{id}", userTest.getId())
                .with(csrf()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(instanceOf(Number.class))))
                .andExpect(jsonPath("$.surname",
                        is(userTest.getSurname())))
                .andExpect(jsonPath("$.name",
                        is(userTest.getName())))
                .andExpect(jsonPath("$.patronymic",
                        is(userTest.getPatronymic())))
                .andExpect(jsonPath("$.birthDate",
                        is(userTest.getBirthDate().toString())))
                .andExpect(jsonPath("$.email",
                        is(userTest.getEmail())))
                .andExpect(jsonPath("$.phone",
                        is(userTest.getPhone())));
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedEmployee() throws Exception{
        ResultActions response = mockMvc.perform(post("/users")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",
                        is(instanceOf(Number.class))))
                .andExpect(jsonPath("$.surname",
                        is(user.getSurname())))
                .andExpect(jsonPath("$.name",
                        is(user.getName())))
                .andExpect(jsonPath("$.patronymic",
                        is(user.getPatronymic())))
                .andExpect(jsonPath("$.birthDate",
                        is(user.getBirthDate().toString())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())))
                .andExpect(jsonPath("$.phone",
                        is(user.getPhone())));
    }

    @Test
    public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception{
        List<User> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(User.builder()
                .id(2L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build());
        listOfEmployees.add(user);
        userRepository.saveAll(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/users"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));

    }

    @Test
    public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception{
        Long userId = 99L;
        userRepository.save(user);

        ResultActions response = mockMvc.perform(get("/users/{id}", userId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturn200() throws Exception{
        userRepository.save(user);

        ResultActions response = mockMvc.perform(delete("/users/{id}", user.getId())
                .with(csrf()));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception{
        userRepository.save(user);

        User updatedUser = new User();
        updatedUser.setSurname("N");
        updatedUser.setName("N");
        updatedUser.setPatronymic(user.getPatronymic());
        updatedUser.setBirthDate(user.getBirthDate());
        updatedUser.setEmail("N");
        updatedUser.setPhone(user.getPhone());

        ResultActions response = mockMvc.perform(put("/users/{id}", user.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
