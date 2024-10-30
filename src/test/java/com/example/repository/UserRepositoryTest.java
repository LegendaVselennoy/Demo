package com.example.repository;

import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setup(){
        user = User.builder()
                .id(1L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();

        userRepository.save(user);
    }

    @DisplayName("Test list")
    @Test
    void findAllUserList() {
       User user2 = User.builder()
                .id(2L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();

        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @DisplayName("Test for update")
    @Test
    public void updateUser() {
        User user3 = User.builder()
                .id(3L)
                .surname("Surname")
                .name("Name")
                .patronymic("Patronymic")
                .birthDate(LocalDate.of(2024,10,10))
                .email("email")
                .phone("8321555555")
                .build();

        userRepository.save(user3);

        User savedUser = userRepository.findById(user3.getId()).get();
        savedUser.setName("E");
        savedUser.setSurname("Lend");
        User updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser.getName()).isEqualTo("E");
        assertThat(updatedUser.getSurname()).isEqualTo("Lend");
    }

    @DisplayName("Test for save")
    @Test
    void userSaveThenReturnSavedUser() {
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @DisplayName("Test for findById")
    @Test
    public void userFindByIdThenReturnUser() {
        User userDB = userRepository.findById(user.getId()).orElse(null);

        assertThat(userDB).isNotNull();
    }

    @DisplayName("Test for delete")
    @Test
    public void userDeleteThenReturnUser() {
        userRepository.delete(user);
        Optional<User> userDB = userRepository.findById(user.getId());

        assertThat(userDB).isEmpty();
    }
}