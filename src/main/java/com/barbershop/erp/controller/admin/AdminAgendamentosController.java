package com.barbershop.erp.controller.admin;

import com.barbershop.erp.model.*;
import com.barbershop.erp.service.AgendamentoService;
import com.barbershop.erp.service.UsuarioService;
import com.barbershop.erp.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/agendamentos")
public class AdminAgendamentosController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    // Listar todos os agendamentos
    @GetMapping
    public String index(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarTodos());
        return "admin/agendamentos/index";
    }

    // Mostrar detalhes de um agendamento
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Agendamento agendamento = agendamentoService.buscarPorId(id);
        if (agendamento == null) return "redirect:/admin/agendamentos";
        model.addAttribute("agendamento", agendamento);
        return "admin/agendamentos/show";
    }

    // Formulário de criação
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("clientes", usuarioService.listarTodos().stream()
                .filter(u -> u instanceof Cliente)
                .map(u -> (Cliente) u)
                .toList());
        model.addAttribute("funcionarios", usuarioService.listarFuncionariosAtivos());
        model.addAttribute("servicos", servicoService.listarTodos());
        return "admin/agendamentos/form";
    }

    // Criar agendamento
    @PostMapping
    public String criar(@RequestParam Long clienteId,
                       @RequestParam Long servicoId,
                       @RequestParam Long funcionarioId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
                       @RequestParam(required = false) String observacoes,
                       Model model) {
        try {
            Cliente cliente = (Cliente) usuarioService.buscarPorId(clienteId);
            Servico servico = servicoService.buscarPorId(servicoId);
            Funcionario funcionario = (Funcionario) usuarioService.buscarPorId(funcionarioId);

            Agendamento agendamento = agendamentoService.criarAgendamento(cliente, servico, dataHora, funcionario);
            
            if (observacoes != null && !observacoes.trim().isEmpty()) {
                agendamento.setObservacoes(observacoes);
                agendamentoService.atualizarAgendamento(agendamento);
            }

            return "redirect:/admin/agendamentos";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("agendamento", new Agendamento());
            model.addAttribute("clientes", usuarioService.listarTodos().stream()
                    .filter(u -> u instanceof Cliente)
                    .map(u -> (Cliente) u)
                    .toList());
            model.addAttribute("funcionarios", usuarioService.listarFuncionariosAtivos());
            model.addAttribute("servicos", servicoService.listarTodos());
            return "admin/agendamentos/form";
        }
    }

    // Formulário de edição
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Agendamento agendamento = agendamentoService.buscarPorId(id);
        if (agendamento == null) return "redirect:/admin/agendamentos";
        
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("clientes", usuarioService.listarTodos().stream()
                .filter(u -> u instanceof Cliente)
                .map(u -> (Cliente) u)
                .toList());
        model.addAttribute("funcionarios", usuarioService.listarFuncionariosAtivos());
        model.addAttribute("servicos", servicoService.listarTodos());
        return "admin/agendamentos/form";
    }

    // Atualizar agendamento
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                           @RequestParam Long clienteId,
                           @RequestParam Long servicoId,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
                           @RequestParam(required = false) String observacoes,
                           Model model) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);
            if (agendamento == null) return "redirect:/admin/agendamentos";

            Cliente cliente = (Cliente) usuarioService.buscarPorId(clienteId);
            Servico servico = servicoService.buscarPorId(servicoId);

            agendamento.setCliente(cliente);
            agendamento.setServico(servico);
            agendamento.setDataHora(dataHora);
            if (observacoes != null) {
                agendamento.setObservacoes(observacoes);
            }

            agendamentoService.atualizarAgendamento(agendamento);
            return "redirect:/admin/agendamentos";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/agendamentos/editar/" + id;
        }
    }

    // Atualizar status
    @PostMapping("/status/{id}")
    public String atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        agendamentoService.atualizarStatus(id, status);
        return "redirect:/admin/agendamentos";
    }

    // Cancelar agendamento
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return "redirect:/admin/agendamentos";
    }

    // Deletar agendamento
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        agendamentoService.deletar(id);
        return "redirect:/admin/agendamentos";
    }
}
