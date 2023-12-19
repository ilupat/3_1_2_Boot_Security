package ru.kata.spring.boot_security.demo.service;

import javassist.NotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    void add(Role role);

    void edit(Role role);

    Role getById(long id);

    Role getByName(String name) throws NotFoundException;
}
