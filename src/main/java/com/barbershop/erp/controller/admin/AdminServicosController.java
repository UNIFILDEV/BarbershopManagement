package com.barbershop.erp.controller.admin;

import com.barbershop.erp.model.Servico;
import com.barbershop.erp.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/servicos")
public class AdminServicosController {

    @Autowired
    private ServicoService servicoService;

    // Listar todos
    @GetMapping
    public String index(Model model) {
        model.addAttribute("servicos", servicoService.listarTodos());
        return "admin/servicos/index";
    }

    // Mostrar detalhes de um serviço
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Servico servico = servicoService.buscarPorId(id);
        model.addAttribute("servico", servico);
        return "admin/servicos/show";
    }

    // Formulário de criação
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("servico", new Servico());
        return "admin/servicos/form";
    }

    // Criar serviço
    @PostMapping
    public String criar(@Valid @ModelAttribute Servico servico, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/servicos/form";
        }
        servicoService.salvar(servico);
        return "redirect:/admin/servicos";
    }

    // Formulário de edição
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Servico servico = servicoService.buscarPorId(id);
        if (servico == null) return "redirect:/admin/servicos";
        model.addAttribute("servico", servico);
        return "admin/servicos/form";
    }

    // Atualizar serviço
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Servico servico,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/servicos/form";
        }
        Servico existente = servicoService.buscarPorId(id);
        if (existente != null) {
            existente.setNome(servico.getNome());
            existente.setDescricao(servico.getDescricao());
            existente.setPreco(servico.getPreco());
            existente.setDuracaoMinutos(servico.getDuracaoMinutos());
            existente.setAtivo(servico.isAtivo());
            servicoService.salvar(existente);
        }
        return "redirect:/admin/servicos";
    }

    // Deletar serviço
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        servicoService.deletar(id);
        return "redirect:/admin/servicos";
    }
}