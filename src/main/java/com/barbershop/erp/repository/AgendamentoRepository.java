package com.barbershop.erp.repository;

import com.barbershop.erp.model.Agendamento;
import com.barbershop.erp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByCliente(Cliente cliente);

    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN ?1 AND ?2")
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByStatus(String status);
}