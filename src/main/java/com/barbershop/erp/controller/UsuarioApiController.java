package com.barbershop.erp.controller;

import com.barbershop.erp.model.AlocacaoAgendamento;
import com.barbershop.erp.model.Cliente;
import com.barbershop.erp.model.Funcionario;
import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.service.AgendamentoService;
import com.barbershop.erp.service.AlocacaoService;
import com.barbershop.erp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioApiController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AlocacaoService alocacaoService;

    // Criar novo usuário
    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            response.put("success", false);
            response.put("message", "Dados inválidos");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        if (usuarioService.emailJaExiste(usuario.getEmail())) {
            response.put("success", false);
            response.put("message", "Este email já está em uso");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            if (usuario.getTipoUsuario() == null) {
                usuario.setTipoUsuario(com.barbershop.erp.model.enums.TipoUsuario.CLIENTE);
            }

            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);

            Map<String, Object> usuarioResponse = criarUsuarioResponse(usuarioSalvo);

            response.put("success", true);
            response.put("message", "Usuário criado com sucesso");
            response.put("usuario", usuarioResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodos();

            List<Map<String, Object>> usuariosResponse = usuarios.stream()
                    .map(this::criarUsuarioResponse)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuarios", usuariosResponse);
            response.put("total", usuariosResponse.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao listar usuários: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario usuario = usuarioService.buscarPorId(id);

            if (usuario == null) {
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> usuarioResponse = criarUsuarioResponse(usuario);

            response.put("success", true);
            response.put("usuario", usuarioResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao buscar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Atualizar usuário por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioAtualizado, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            response.put("success", false);
            response.put("message", "Dados inválidos");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Usuario usuarioExistente = usuarioService.buscarPorId(id);

            if (usuarioExistente == null) {
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return ResponseEntity.notFound().build();
            }

            if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail()) &&
                    usuarioService.emailJaExiste(usuarioAtualizado.getEmail())) {
                response.put("success", false);
                response.put("message", "Este email já está em uso por outro usuário");
                return ResponseEntity.badRequest().body(response);
            }

            Usuario usuarioSalvo = usuarioService.atualizarUsuario(id, usuarioAtualizado);

            if (usuarioSalvo == null) {
                response.put("success", false);
                response.put("message", "Erro ao atualizar usuário");
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> usuarioResponse = criarUsuarioResponse(usuarioSalvo);

            response.put("success", true);
            response.put("message", "Usuário atualizado com sucesso");
            response.put("usuario", usuarioResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Deletar usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario usuario = usuarioService.buscarPorId(id);

            if (usuario == null) {
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return ResponseEntity.notFound().build();
            }

            if (usuario instanceof Cliente) {
                Cliente cliente = (Cliente) usuario;
                List<?> agendamentos = agendamentoService.listarPorCliente(cliente);
                if (!agendamentos.isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Não é possível deletar cliente com agendamentos ativos");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            if (usuario instanceof Funcionario) {
                Funcionario funcionario = (Funcionario) usuario;
                List<AlocacaoAgendamento> alocacoes = alocacaoService.listarPorFuncionario(funcionario);
                if (!alocacoes.isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Não é possível deletar funcionário com alocações ativas");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            usuarioService.deletarUsuario(id);

            response.put("success", true);
            response.put("message", "Usuário deletado com sucesso");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao deletar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> criarUsuarioResponse(Usuario usuario) {
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", usuario.getId());
        usuarioMap.put("nome", usuario.getNome());
        usuarioMap.put("email", usuario.getEmail());
        usuarioMap.put("telefone", usuario.getTelefone());
        usuarioMap.put("tipoUsuario", usuario.getTipoUsuario());

        if (usuario instanceof Funcionario) {
            Funcionario funcionario = (Funcionario) usuario;
            usuarioMap.put("ativo", funcionario.isAtivo());
        }

        return usuarioMap;
    }
}