package com.barbershop.erp.model.enums;

public enum TipoUsuario {
    ADMIN("Administrador"),
    CLIENTE("Cliente"),
    FUNCIONARIO("Funcionário");

    private final String label;

    TipoUsuario(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
