package com.barbershop.erp.service;

import com.barbershop.erp.model.AlocacaoAgendamento;
import com.barbershop.erp.model.Funcionario;
import com.barbershop.erp.repository.AlocacaoAgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlocacaoService {

    @Autowired
    private AlocacaoAgendamentoRepository alocacaoRepository;

    public List<AlocacaoAgendamento> listarPorFuncionario(Funcionario funcionario) {
        return alocacaoRepository.findByFuncionario(funcionario);
    }

    public boolean funcionarioDisponivel(Funcionario funcionario, LocalDateTime inicio, LocalDateTime fim) {
        List<AlocacaoAgendamento> conflitos = alocacaoRepository
                .findConflictingAllocations(funcionario, inicio, fim);
        return conflitos.isEmpty();
    }

    public List<AlocacaoAgendamento> listarTodas() {
        return alocacaoRepository.findAll();
    }
}
