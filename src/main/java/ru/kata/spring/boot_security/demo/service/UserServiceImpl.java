package ru.kata.spring.boot_security.demo.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    private BCryptPasswordEncoder bCrypt() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        user.setPassword(bCrypt().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void edit(User user) {
        if (ObjectUtils.isEmpty(user.getPassword())) {
            userRepository.findById(user.getId()).map(User::getPassword).ifPresent(user::setPassword);
            userRepository.save(user);
            return;
        }
        user.setPassword(bCrypt().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getById(long id) {
        User user = null;
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            user = opt.get();
        }
        return user;
    }

    @Override
    public User getByName(String name) throws NotFoundException {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new NotFoundException(name);
        }
        return user;
    }

}
