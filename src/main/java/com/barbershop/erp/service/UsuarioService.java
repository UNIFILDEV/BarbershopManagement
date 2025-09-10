package com.barbershop.erp.service;

import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.model.Cliente;
import com.barbershop.erp.model.Funcionario;
import com.barbershop.erp.model.enums.TipoUsuario;
import com.barbershop.erp.repository.UsuarioRepository;
import com.barbershop.erp.repository.ClienteRepository;
import com.barbershop.erp.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name())))
                .build();
    }

    // Criação
    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (usuario.getTipoUsuario() == TipoUsuario.CLIENTE) {
            Cliente cliente = new Cliente();
            cliente.setNome(usuario.getNome());
            cliente.setEmail(usuario.getEmail());
            cliente.setSenha(usuario.getSenha());
            cliente.setTelefone(usuario.getTelefone());
            return clienteRepository.save(cliente);
        } else if (usuario.getTipoUsuario() == TipoUsuario.FUNCIONARIO) {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(usuario.getNome());
            funcionario.setEmail(usuario.getEmail());
            funcionario.setSenha(usuario.getSenha());
            funcionario.setTelefone(usuario.getTelefone());
            return funcionarioRepository.save(funcionario);
        }

        return usuarioRepository.save(usuario);
    }

    // Edição
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);
        if (usuarioExistente == null) return null;

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        usuarioExistente.setTipoUsuario(usuarioAtualizado.getTipoUsuario());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }


    public boolean emailJaExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean emailJaExisteParaOutroUsuario(String email, Long id) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return usuario != null && !usuario.getId().equals(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Funcionario> listarFuncionariosAtivos() {
        return funcionarioRepository.findByAtivoTrue();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
