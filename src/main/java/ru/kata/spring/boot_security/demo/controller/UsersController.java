package ru.kata.spring.boot_security.demo.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UsersController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("admin")
    public String listUser(ModelMap modelMap) {
        modelMap.addAttribute("list", userService.getAllUsers());
        return "adminPage";
    }

    @GetMapping("user")
    public String infoUser(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "userPage";
    }

    @GetMapping(value = "user/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "createNew";
    }

    @PostMapping(value = "user/new")
    public String newUser(@ModelAttribute User user,
                          @RequestParam(value = "roless") String[] role) throws NotFoundException {
        Set<Role> rolesSet = new HashSet<>();
        for (String roles : role) {
            rolesSet.add(roleService.getByName(roles));
        }
        user.setRoles(rolesSet);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "user/edit/{id}")
    public String editUser(@PathVariable("id") long id, ModelMap model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping(value = "user/edit/{id}")
    public String editUser(@ModelAttribute User user, @RequestParam(value = "roless") String[] role) throws NotFoundException {
        Set<Role> rolesSet = new HashSet<>();
        for (String roles : role) {
            rolesSet.add((roleService.getByName(roles)));
        }
        user.setRoles(rolesSet);
        userService.edit(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "user/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }
}
