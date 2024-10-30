package com.example.service;

import com.example.entity.User;
import com.example.entity.dto.ContactInfoDto;
import com.example.exception.RequestValidationException;
import com.example.mapper.ContactMapper;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private static final String EMPTY_FILE = "Empty path file";

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public Optional<ContactInfoDto> findContactInfoById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(contactMapper::userToContactDTO);
    }

    public void updateUser(Long id, User user) {
        if (userRepository.findById(id).isEmpty()) {
            throw new RequestValidationException("User with id [%s] not found".formatted(id));
        }
        User userUpdated = userRepository.findById(id).get();
        userUpdated.setSurname(user.getSurname());
        userUpdated.setName(user.getName());
        userUpdated.setPatronymic(user.getPatronymic());
        userUpdated.setBirthDate(user.getBirthDate());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setPhone(user.getPhone());
        userRepository.save(userUpdated);
    }

    public byte[] getPhoto(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            Path path = Paths.get(user.get().getPathFile());
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
    }

    public void deletePhoto(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user != null) {
            Path path = Paths.get(user.getPathFile());
            try {
                Files.deleteIfExists(path);
                user.setPathFile(EMPTY_FILE);
                userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
