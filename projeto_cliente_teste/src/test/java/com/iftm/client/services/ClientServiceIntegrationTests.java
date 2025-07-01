
package com.iftm.client.services;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ClientServiceIntegrationTests {

    @Autowired
    private ClientService servico;

    @Autowired
    private ClientRepository repositorio;

    private long idExistente;
    private long idInexistente;
    private long totalClientes;
    private Double rendaExistente;

    @BeforeEach
    void setUp() {
        idExistente = 1L;
        idInexistente = 999L;
        totalClientes = repositorio.count();
        rendaExistente = 5000.0;
    }

    @Test
    public void deleteDeveriaRemoverClienteQuandoIdExistir() {
        Assertions.assertDoesNotThrow(() -> {
            servico.delete(idExistente);
        });
    }

    @Test
    public void deleteDeveriaLancarExcecaoQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            servico.delete(idInexistente);
        });
    }

    @Test
    public void findAllPagedDeveriaRetornarPaginaDeClientes() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ClientDTO> resultado = servico.findAllPaged(pageRequest);
        Assertions.assertFalse(resultado.isEmpty());
    }

    @Test
    public void findByIncomeDeveriaRetornarPaginaDeClientesComRenda() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ClientDTO> resultado = servico.findByIncome(rendaExistente, pageRequest);
        Assertions.assertFalse(resultado.isEmpty());
    }

    @Test
    public void findByIdDeveriaRetornarClienteQuandoIdExistir() {
        ClientDTO resultado = servico.findById(idExistente);
        Assertions.assertNotNull(resultado);
    }

    @Test
    public void findByIdDeveriaLancarExcecaoQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            servico.findById(idInexistente);
        });
    }
}
