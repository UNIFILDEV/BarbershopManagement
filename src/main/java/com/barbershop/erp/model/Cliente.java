package com.barbershop.erp.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    private String endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;

    public Cliente() {
        super();
        setTipoUsuario(com.barbershop.erp.model.enums.TipoUsuario.CLIENTE);
    }

    // Getters e os Setters
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<Agendamento> getAgendamentos() { return agendamentos; }
    public void setAgendamentos(List<Agendamento> agendamentos) { this.agendamentos = agendamentos; }
}