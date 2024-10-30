package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();
    }

    @DisplayName("Test for update")
    @Test
    void givenUserWhenGetUpdate() {
        given(repository.findById(1L)).willReturn(Optional.of(user));

        service.updateUser(user.getId(),user);

        verify(repository, times(1)).save(user);
    }

    @DisplayName("Test for delete")
    @Test
    void givenUserWhenGetDelete() {
        service.deleteUser(user.getId());

        assertTrue(repository.findById(1L).isEmpty());
    }

    @DisplayName("Test for findById")
    @Test
    void givenUserWhenGetUserByIdThenReturnUser() {
        given(repository.findById(1L)).willReturn(Optional.of(user));

        User savedUser = service.findUserById(user.getId()).get();

        assertThat(savedUser).isNotNull();
    }

    @DisplayName("Test for findAll (negative)")
    @Test
    void getAllUsersListTestNegative() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        List<User> userList = service.findAllUsers();

        assertThat(userList).isEmpty();
        assertThat(userList.size()).isEqualTo(0);
    }

    @DisplayName("Test for findAll")
    @Test
    void getAllUsersListTest() {
        User user1 = User.builder()
                .id(2L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();
        given(repository.findAll()).willReturn(List.of(user, user1));

        List<User> userList = service.findAllUsers();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }
}
