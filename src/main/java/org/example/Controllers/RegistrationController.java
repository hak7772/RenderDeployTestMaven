package org.example.Controllers;

import org.example.Entity.User;
import org.example.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Имя HTML-шаблона
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {

        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Имя пользователя уже занято");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email уже зарегистрирован");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // Хешируем пароль перед сохранением в базу данных
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Устанавливаем роль по умолчанию
        userRepository.save(user);

        return "redirect:/login"; // Перенаправляем на страницу входа после успешной регистрации
    }
}
