// src/main/java/com/barbershop/erp/config/DataLoader.java
package com.barbershop.erp.config;

import com.barbershop.erp.model.*;
import com.barbershop.erp.model.enums.TipoUsuario;
import com.barbershop.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Só cria dados se não existir nenhum usuário
        if (usuarioRepository.count() == 0) {

            System.out.println("\n=== CRIANDO DADOS INICIAIS ===");

            // ========== CRIAR USUÁRIO CLIENTE ==========
            Cliente cliente = new Cliente();
            cliente.setNome("Cliente");
            cliente.setEmail("cliente@barbearia.com");
            cliente.setSenha(passwordEncoder.encode("123456"));
            cliente.setTipoUsuario(TipoUsuario.CLIENTE);
            cliente.setTelefone("(11) 99999-0000");
            usuarioRepository.save(cliente);

            // ========== CRIAR USUÁRIO ADMIN ==========
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@barbearia.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            admin.setTelefone("(11) 99999-0000");
            usuarioRepository.save(admin);

            System.out.println("✅ ADMIN criado: admin@barbearia.com / admin123");

            // ========== CRIAR FUNCIONÁRIOS ==========
            Funcionario funcionario1 = new Funcionario();
            funcionario1.setNome("Primeiro");
            funcionario1.setEmail("primeiro@barbearia.com");
            funcionario1.setSenha(passwordEncoder.encode("123456"));
            funcionario1.setTelefone("(11) 99999-1111");
            funcionario1.setEspecialidade("Cortes tradicionais e modernos");
            funcionario1.setAtivo(true);
            funcionarioRepository.save(funcionario1);

            Funcionario funcionario2 = new Funcionario();
            funcionario2.setNome("Segundo");
            funcionario2.setEmail("segundo@barbearia.com");
            funcionario2.setSenha(passwordEncoder.encode("123456"));
            funcionario2.setTelefone("(11) 99999-2222");
            funcionario2.setEspecialidade("Especialista em barbas");
            funcionario2.setAtivo(true);
            funcionarioRepository.save(funcionario2);

            System.out.println("✅ FUNCIONÁRIOS criados: primeiro@barbearia.com e segundo@barbearia.com / 123456");

            // ========== CRIAR SERVIÇOS ==========
            Servico corte = new Servico("Corte de Cabelo", "Corte masculino tradicional",
                    new BigDecimal("25.00"), 30);
            servicoRepository.save(corte);

            Servico barba = new Servico("Barba", "Fazer e aparar barba",
                    new BigDecimal("15.00"), 20);
            servicoRepository.save(barba);

            Servico corteBarba = new Servico("Corte + Barba", "Pacote completo",
                    new BigDecimal("35.00"), 45);
            servicoRepository.save(corteBarba);

            Servico lavagem = new Servico("Lavagem", "Lavagem e hidratação capilar",
                    new BigDecimal("10.00"), 15);
            servicoRepository.save(lavagem);

            Servico sobrancelha = new Servico("Sobrancelha", "Design de sobrancelhas masculinas",
                    new BigDecimal("12.00"), 15);
            servicoRepository.save(sobrancelha);

            System.out.println("✅ SERVIÇOS criados: 5 serviços disponíveis");

            System.out.println("\n=== DADOS INICIAIS CARREGADOS COM SUCESSO! ===");
            System.out.println("🌐 Acesse: http://localhost:3000");
            System.out.println("👑 Admin: admin@barbearia.com / admin123");
            System.out.println("👨‍💼 Funcionário 1: primeiro@barbearia.com / 123456");
            System.out.println("👨‍💼 Funcionário 2: segundo@barbearia.com / 123456");
            System.out.println("Cliente: cliente@barbearia.com / 123456");
            System.out.println("===============================================\n");
        }
    }
}