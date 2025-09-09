package com.barbershop.erp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alocacao_agendamentos")
public class AlocacaoAgendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    private LocalDateTime inicioAlocacao;
    private LocalDateTime fimAlocacao;

    // Construtores
    public AlocacaoAgendamento() {}

    public AlocacaoAgendamento(Agendamento agendamento, Funcionario funcionario,
                               LocalDateTime inicioAlocacao, LocalDateTime fimAlocacao) {
        this.agendamento = agendamento;
        this.funcionario = funcionario;
        this.inicioAlocacao = inicioAlocacao;
        this.fimAlocacao = fimAlocacao;
    }

    // Getters e os Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public LocalDateTime getInicioAlocacao() { return inicioAlocacao; }
    public void setInicioAlocacao(LocalDateTime inicioAlocacao) { this.inicioAlocacao = inicioAlocacao; }

    public LocalDateTime getFimAlocacao() { return fimAlocacao; }
    public void setFimAlocacao(LocalDateTime fimAlocacao) { this.fimAlocacao = fimAlocacao; }
}