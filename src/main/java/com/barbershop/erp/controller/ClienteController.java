package com.barbershop.erp.controller;

import com.barbershop.erp.model.*;
import com.barbershop.erp.repository.UsuarioRepository;
import com.barbershop.erp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario instanceof Cliente cliente) {
            List<Agendamento> agendamentos = agendamentoService.listarPorCliente(cliente);
            model.addAttribute("agendamentos", agendamentos);
            model.addAttribute("cliente", cliente);
            return "redirect:/";
        }

        return "/dashboard";
    }

    @GetMapping("/agendamentos")
    public String listarAgendamentos(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            List<Agendamento> agendamentos = agendamentoService.listarPorCliente(cliente);
            model.addAttribute("agendamentos", agendamentos);
        }

        return "cliente/dashboard";
    }

    @GetMapping("/agendar")
    public String agendarForm(Model model) {
        model.addAttribute("servicos", servicoService.listarAtivos());
        model.addAttribute("funcionarios", usuarioService.listarFuncionariosAtivos());
        return "cliente/agendamentos/form";
    }

    @PostMapping("/agendar")
    public String criarAgendamento(@RequestParam Long servicoId,
                                   @RequestParam Long funcionarioId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
                                   @RequestParam(required = false) String observacoes,
                                   Authentication authentication,
                                   Model model) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

            if (usuario instanceof Cliente) {
                Cliente cliente = (Cliente) usuario;
                Servico servico = servicoService.buscarPorId(servicoId);
                Funcionario funcionario = (Funcionario) usuarioService.buscarPorId(funcionarioId);

                Agendamento agendamento = agendamentoService.criarAgendamento(
                        cliente, servico, dataHora, funcionario);

                if (observacoes != null && !observacoes.trim().isEmpty()) {
                    agendamento.setObservacoes(observacoes);
                }

                return "redirect:/cliente/dashboard?success";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("servicos", servicoService.listarAtivos());
            model.addAttribute("funcionarios", usuarioService.listarFuncionariosAtivos());
            return "cliente/agendar";
        }

        return "redirect:/cliente/dashboard";
    }

    @PostMapping("/agendamentos/cancelar/{id}")
    public String cancelarAgendamento(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return "redirect:/dashboard?success=teste";
    }
}