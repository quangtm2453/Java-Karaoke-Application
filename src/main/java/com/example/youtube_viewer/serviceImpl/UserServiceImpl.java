package com.example.youtube_viewer.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.repository.UserRepository;
import com.example.youtube_viewer.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {
        User existingUser = findById(user.getId());
        if (existingUser != null) {
            return userRepository.save(user);
        }
        return null;
    }
}
