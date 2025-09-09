package com.barbershop.erp.controller;

import com.barbershop.erp.model.*;
import com.barbershop.erp.repository.UsuarioRepository;
import com.barbershop.erp.service.AlocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private AlocacaoService alocacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario instanceof Funcionario) {
            Funcionario funcionario = (Funcionario) usuario;
            List<AlocacaoAgendamento> alocacoes = alocacaoService.listarPorFuncionario(funcionario);

            model.addAttribute("funcionario", funcionario);
            model.addAttribute("alocacoes", alocacoes);
        }

        return "funcionario/dashboard";
    }
}