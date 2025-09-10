package com.barbershop.erp.controller.admin;

import com.barbershop.erp.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/agendamentos")
public class AdminAgendamentosController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarTodos());
        return "admin/agendamentos/index";
    }

    @PostMapping("/status/{id}")
    public String atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        agendamentoService.atualizarStatus(id, status);
        return "redirect:/admin/agendamentos";
    }
}
