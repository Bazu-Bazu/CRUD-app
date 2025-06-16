package com.example.CRUD_app.service;

import com.example.CRUD_app.model.User;
import com.example.CRUD_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("The user with this email already exists");
        }

        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("The user with id " + id + " doesn't exists");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String email, String firstName, String lastName, LocalDate birth) {
        Optional<User> optionalUserById = userRepository.findById(id);
        if (optionalUserById.isEmpty()) {
            throw new IllegalStateException("The user with id " + id + " doesn't exists");
        }

        User user = optionalUserById.get();
        if (email != null && !email.equals(user.getEmail())) {
            Optional<User> optionalUserByEmail = userRepository.findByEmail(email);
            if (optionalUserByEmail.isPresent()) {
                throw new IllegalStateException("The user with this email already exists");
            }
            user.setEmail(email);
        }

        if (firstName != null && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (lastName != null && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (birth != null && birth != user.getBirth()) {
            user.setBirth(birth);
        }

        userRepository.save(user);
    }

}
