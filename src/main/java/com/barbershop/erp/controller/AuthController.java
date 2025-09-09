package com.barbershop.erp.controller;

import com.barbershop.erp.model.Cliente;
import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.repository.UsuarioRepository;
import com.barbershop.erp.service.AgendamentoService;
import com.barbershop.erp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("showHeader", false);
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("showHeader", false);
        model.addAttribute("usuario", new Usuario());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        model.addAttribute("showHeader", false);
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (usuarioService.emailJaExiste(usuario.getEmail())) {
            model.addAttribute("emailError", "Este email já está em uso");
            return "auth/register";
        }

        usuario.setTipoUsuario(com.barbershop.erp.model.enums.TipoUsuario.CLIENTE);
        usuarioService.salvarUsuario(usuario);

        return "redirect:/login?registered";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        switch (role) {
            case "ROLE_ADMIN":
                model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
                return "admin/dashboard";
            case "ROLE_FUNCIONARIO":
                return "funcionario/dashboard";
            case "ROLE_CLIENTE":
                if (usuario instanceof Cliente cliente) {
                    List<?> agendamentos = agendamentoService.listarPorCliente(cliente);
                    model.addAttribute("agendamentos", agendamentos);
                    model.addAttribute("cliente", cliente);
                }
                return "cliente/dashboard";
            default:
                return "redirect:/login";
        }
    }
}