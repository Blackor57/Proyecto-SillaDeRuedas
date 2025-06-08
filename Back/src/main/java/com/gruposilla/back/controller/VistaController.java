package com.gruposilla.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class VistaController {

    @GetMapping("/register")
    public String mostrarFormularioRegistro() {
        return "register";  // busca templates/register.html
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";     // busca templates/login.html
    }

    @GetMapping("/index")
    public String mostrarIndex() {
        return "index";     // busca templates/index.html
    }
}