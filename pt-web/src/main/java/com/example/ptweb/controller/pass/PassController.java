package com.example.ptweb.controller.pass;

import com.example.ptweb.dto.PassDto;
import com.example.ptweb.dto.UserDto;
import com.example.ptweb.service.pass.PassService;
import com.example.ptweb.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
@RequestMapping("/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final UserService userService;

    @GetMapping
    public String passes(@RequestParam("userId") String userId, Model model) {
        List<PassDto> passes = passService.getPasses(userId);
        UserDto user = userService.getUser(userId);

        model.addAttribute("passes", passes);
        model.addAttribute("user", user);
        return "pass/index";
    }

}
