package com.barbershop.erp.controller.admin;

import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.model.enums.TipoUsuario;
import com.barbershop.erp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/show";
    }

    @GetMapping("/novo")
    public String newForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "admin/usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return "redirect:/admin/usuarios";
        model.addAttribute("usuario", usuario);
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "admin/usuarios/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors() || usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), usuario.getId())) {
            if (usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), usuario.getId())) {
                model.addAttribute("emailError", "Este email já está em uso");
            }
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }
        usuarioService.salvarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Usuario usuario,
                         BindingResult result, Model model) {
        if (result.hasErrors() || usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), id)) {
            if (usuarioService.emailJaExisteParaOutroUsuario(usuario.getEmail(), id)) {
                model.addAttribute("emailError", "Este email já está em uso");
            }
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "admin/usuarios/form";
        }
        usuarioService.atualizarUsuario(id, usuario);
        return "redirect:/admin/usuarios";
    }

    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return "redirect:/admin/usuarios";
    }
}
