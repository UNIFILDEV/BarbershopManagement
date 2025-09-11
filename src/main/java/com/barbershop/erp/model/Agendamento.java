package com.barbershop.erp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    // Adicione a referência ao Profissional
    @ManyToOne
    @JoinColumn(name = "profissional_id") // Use o nome da coluna da chave estrangeira
    private Funcionario funcionario;

    private LocalDateTime dataHora;

    private String status = "AGENDADO"; // AGENDADO, CONCLUIDO, CANCELADO

    private String observacoes;

    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private List<AlocacaoAgendamento> alocacoes;

    // Construtores
    public Agendamento() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }

    // Adicione os getters e setters para o Profissional
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario profissional) { this.funcionario = profissional; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<AlocacaoAgendamento> getAlocacoes() { return alocacoes; }
    public void setAlocacoes(List<AlocacaoAgendamento> alocacoes) { this.alocacoes = alocacoes; }
}