package com.barbershop.erp.repository;

import com.barbershop.erp.model.AlocacaoAgendamento;
import com.barbershop.erp.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlocacaoAgendamentoRepository extends JpaRepository<AlocacaoAgendamento, Long> {
    List<AlocacaoAgendamento> findByFuncionario(Funcionario funcionario);

    @Query("SELECT aa FROM AlocacaoAgendamento aa WHERE aa.funcionario = ?1 " +
            "AND ((aa.inicioAlocacao BETWEEN ?2 AND ?3) OR (aa.fimAlocacao BETWEEN ?2 AND ?3) " +
            "OR (aa.inicioAlocacao <= ?2 AND aa.fimAlocacao >= ?3))")
    List<AlocacaoAgendamento> findConflictingAllocations(Funcionario funcionario,
                                                         LocalDateTime inicio, LocalDateTime fim);
}