package com.barbershop.erp.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "funcionarios")
public class Funcionario extends Usuario {

    private String especialidade;
    private boolean ativo = true;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<AlocacaoAgendamento> alocacoes;

    public Funcionario() {
        super();
        setTipoUsuario(com.barbershop.erp.model.enums.TipoUsuario.FUNCIONARIO);
    }

    // Getters e os Setters
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public List<AlocacaoAgendamento> getAlocacoes() { return alocacoes; }
    public void setAlocacoes(List<AlocacaoAgendamento> alocacoes) { this.alocacoes = alocacoes; }
}