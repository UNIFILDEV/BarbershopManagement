package com.barbershop.erp.service;

import com.barbershop.erp.model.Servico;
import com.barbershop.erp.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public List<Servico> listarAtivos() {
        return servicoRepository.findByAtivoTrue();
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        servicoRepository.deleteById(id);
    }
}