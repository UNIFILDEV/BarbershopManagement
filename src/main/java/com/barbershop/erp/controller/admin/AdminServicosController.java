package com.barbershop.erp.controller.admin;

import com.barbershop.erp.model.Servico;
import com.barbershop.erp.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/servicos")
public class AdminServicosController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("servicos", servicoService.listarTodos());
        return "admin/servicos/index";
    }

    @GetMapping("/novo")
    public String newForm(Model model) {
        model.addAttribute("servico", new Servico());
        return "admin/servicos/form";
    }

    @GetMapping("/editar/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Servico servico = servicoService.buscarPorId(id);
        model.addAttribute("servico", servico);
        return "admin/servicos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Servico servico, BindingResult result) {
        if (result.hasErrors()) return "admin/servicos/form";
        servicoService.salvar(servico);
        return "redirect:/admin/servicos";
    }

    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id) {
        servicoService.deletar(id);
        return "redirect:/admin/servicos";
    }
}
