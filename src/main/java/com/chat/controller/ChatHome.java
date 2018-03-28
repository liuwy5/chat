package com.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 主页
 * Created by lw on 16-12-6.
 */
@Controller
@RequestMapping("/chat")
public class ChatHome {
    @RequestMapping("")
    public String chat(HttpServletRequest request, Model model) {
        String name = "sfdsfdf";
        model.addAttribute("name", name);

        String path = request.getContextPath();
        model.addAttribute("path", path);

        return "chat/chat";
    }
}
