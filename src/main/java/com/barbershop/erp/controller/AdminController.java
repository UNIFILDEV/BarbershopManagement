package com.barbershop.erp.controller;

import com.barbershop.erp.model.Servico;
import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.model.enums.TipoUsuario;
import com.barbershop.erp.service.AgendamentoService;
import com.barbershop.erp.service.ServicoService;
import com.barbershop.erp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios/index";
    }

    @GetMapping("/usuarios/{id}")
    public String usuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/show";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "admin/usuarios/form";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "admin/usuarios/form";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@Valid @ModelAttribute Usuario usuario,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }

        // Se email já existe para outro usuário (excluindo o próprio)
        if (usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), usuario.getId())) {
            model.addAttribute("emailError", "Este email já está em uso");
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }

        usuarioService.salvarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @PutMapping("/usuarios/{id}")
    public String atualizarUsuario(@PathVariable Long id,
                                   @Valid @ModelAttribute Usuario usuario,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }

        if (usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), id)) {
            model.addAttribute("emailError", "Este email já está em uso");
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }

        usuario.setId(id); // Garantir que o id está definido
        usuarioService.salvarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    // Serviços

    @GetMapping("/servicos")
    public String listarServicos(Model model) {
        model.addAttribute("servicos", servicoService.listarTodos());
        return "admin/servicos";
    }

    @GetMapping("/servicos/novo")
    public String novoServicoForm(Model model) {
        model.addAttribute("servico", new Servico());
        return "admin/servico-form";
    }

    @PostMapping("/servicos/salvar")
    public String salvarServico(@Valid @ModelAttribute Servico servico,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "admin/servico-form";
        }

        servicoService.salvar(servico);
        return "redirect:/admin/servicos";
    }

    @GetMapping("/servicos/editar/{id}")
    public String editarServicoForm(@PathVariable Long id, Model model) {
        Servico servico = servicoService.buscarPorId(id);
        model.addAttribute("servico", servico);
        return "admin/servico-form";
    }

    @GetMapping("/servicos/deletar/{id}")
    public String deletarServico(@PathVariable Long id) {
        servicoService.deletar(id);
        return "redirect:/admin/servicos";
    }

    // Agendamentos

    @GetMapping("/agendamentos")
    public String listarAgendamentos(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarTodos());
        return "admin/agendamentos";
    }

    @PostMapping("/agendamentos/status/{id}")
    public String atualizarStatusAgendamento(@PathVariable Long id,
                                             @RequestParam String status) {
        agendamentoService.atualizarStatus(id, status);
        return "redirect:/admin/agendamentos";
    }
}
