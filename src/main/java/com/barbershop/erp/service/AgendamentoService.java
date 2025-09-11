package com.barbershop.erp.service;

import com.barbershop.erp.model.*;
import com.barbershop.erp.repository.AgendamentoRepository;
import com.barbershop.erp.repository.AlocacaoAgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AlocacaoAgendamentoRepository alocacaoRepository;

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public List<Agendamento> listarPorCliente(Cliente cliente) {
        return agendamentoRepository.findByCliente(cliente);
    }

    public List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }

    @Transactional
    public Agendamento criarAgendamento(Cliente cliente, Servico servico,
                                        LocalDateTime dataHora, Funcionario funcionario) {

        // Verificar disponibilidade do funcionário
        LocalDateTime fimServico = dataHora.plusMinutes(servico.getDuracaoMinutos());

        List<AlocacaoAgendamento> conflitos = alocacaoRepository
                .findConflictingAllocations(funcionario, dataHora, fimServico);

        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Funcionário não disponível neste horário");
        }

        // Criar agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setDataHora(dataHora);
        agendamento.setStatus("AGENDADO");

        agendamento = agendamentoRepository.save(agendamento);

        // Criar alocação
        AlocacaoAgendamento alocacao = new AlocacaoAgendamento(
                agendamento, funcionario, dataHora, fimServico);

        alocacaoRepository.save(alocacao);

        return agendamento;
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id).orElse(null);
    }

    public Agendamento atualizarStatus(Long id, String novoStatus) {
        Agendamento agendamento = buscarPorId(id);
        if (agendamento != null) {
            agendamento.setStatus(novoStatus);
            return agendamentoRepository.save(agendamento);
        }
        return null;
    }

    @Transactional
    public void cancelarAgendamento(Long id) {
        Agendamento agendamento = buscarPorId(id);
        if (agendamento != null) {
            agendamento.setStatus("CANCELADO");
            agendamentoRepository.save(agendamento);
        }
    }

    public void deletar(Long id) {
        agendamentoRepository.deleteById(id);
    }

    @Transactional
    public Agendamento atualizarAgendamento(Agendamento agendamento) {
        if (agendamento.getId() == null) {
            throw new RuntimeException("ID do agendamento é obrigatório para atualização");
        }
        
        Agendamento agendamentoExistente = buscarPorId(agendamento.getId());
        if (agendamentoExistente == null) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        // Verificar se o status permite edição
        if ("CONCLUIDO".equals(agendamentoExistente.getStatus())) {
            throw new RuntimeException("Não é possível editar um agendamento concluído");
        }

        // Atualizar campos
        agendamentoExistente.setCliente(agendamento.getCliente());
        agendamentoExistente.setServico(agendamento.getServico());
        agendamentoExistente.setDataHora(agendamento.getDataHora());
        agendamentoExistente.setObservacoes(agendamento.getObservacoes());

        return agendamentoRepository.save(agendamentoExistente);
    }
}