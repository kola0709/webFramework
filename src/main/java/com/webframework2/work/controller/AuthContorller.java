package com.webframework2.work.controller;

import com.webframework2.work.domain.Role;
import com.webframework2.work.domain.User;
import com.webframework2.work.dto.SignupRequest;
import com.webframework2.work.repository.RoleRepository;
import com.webframework2.work.repository.UserRepository;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class AuthContorller {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if(error != null) {
            model.addAttribute("errorMessage", "로그인 실패: 아이디 또는 비밀번호를 확인하세요");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute SignupRequest signupRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "register";
        }

        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            model.addAttribute("emailError", "이미 사용중인 이메일입니다");
            return "register";
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("기본 ROLE_USER가 없습니다."));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return "redirect:/login";
    }
}
