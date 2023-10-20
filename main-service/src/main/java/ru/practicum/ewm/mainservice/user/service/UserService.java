package ru.practicum.ewm.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mainservice.user.entity.User;
import ru.practicum.ewm.mainservice.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getById(long userId) {
        return userRepository.findByIdOrThrowNotFoundException(userId, "User");
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    public List<User> findAll(int from, int size) {
        return userRepository.findAll(from, size);
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }
}