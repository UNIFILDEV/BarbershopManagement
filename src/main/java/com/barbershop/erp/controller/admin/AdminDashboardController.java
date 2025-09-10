package com.barbershop.erp.controller.admin;

import com.barbershop.erp.service.AgendamentoService;
import com.barbershop.erp.service.ServicoService;
import com.barbershop.erp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalServicos", servicoService.listarAtivos().size());
        model.addAttribute("totalAgendamentos", agendamentoService.listarTodos().size());
        return "admin/dashboard";
    }
}
