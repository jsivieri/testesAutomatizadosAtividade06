package com.iftm.client.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;




public class ClientServiceTests {

    @InjectMocks
    private ClientService servico;

    @Mock
    private ClientRepository repositorio;

    private long idExistente;
    private long idInexistente;

    @BeforeEach
    void inicializar() {
        MockitoAnnotations.openMocks(this);
        idExistente = 1L;
        idInexistente = 2L;
        doNothing().when(repositorio).deleteById(idExistente);
        doThrow(EmptyResultDataAccessException.class).when(repositorio).deleteById(idInexistente);
    }

    @Test
    public void deleteDeveriaNaoLancarExcecaoQuandoIdExistir() {
        assertDoesNotThrow(() -> {
            servico.delete(idExistente);
        });
    }

    @Test
    public void deleteDeveriaLancarExcecaoQuandoIdNaoExistir() {
        assertThrows(ResourceNotFoundException.class, () -> {
            servico.delete(idInexistente);
        });
    }

    @Test
    public void findAllPagedDeveriaRetornarPaginaComClientes() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Client cliente = new Client(1L, "Maria", "12345678900", 5000.0, null, 2);
        Page<Client> pagina = new PageImpl<>(List.of(cliente));
        when(repositorio.findAll(pageRequest)).thenReturn(pagina);
        Page<ClientDTO> resultado = servico.findAllPaged(pageRequest);
        assertFalse(resultado.isEmpty());
        verify(repositorio).findAll(pageRequest);
    }

    @Test
public void findByIncomeDeveriaRetornarPaginaComClientesComRendaInformada() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    Double renda = 5000.0;

    Client cliente = new Client(1L, "Carlos", "98765432100", renda, null, 1);
    Page<Client> pagina = new PageImpl<>(List.of(cliente));

    when(repositorio.findByIncome(renda, pageRequest)).thenReturn(pagina);

    Page<ClientDTO> resultado = servico.findByIncome(renda, pageRequest);

    assertFalse(resultado.isEmpty());
    verify(repositorio).findByIncome(renda, pageRequest);
}

@Test
public void findByIdDeveriaRetornarClientDTOQuandoIdExistir() {
    Client cliente = new Client(1L, "João", "11111111111", 4000.0, null, 1);
    when(repositorio.findById(idExistente)).thenReturn(Optional.of(cliente));

    ClientDTO resultado = servico.findById(idExistente);

    assertFalse(resultado == null);
    verify(repositorio).findById(idExistente);
}

@Test
public void findByIdDeveriaLancarExcecaoQuandoIdNaoExistir() {
    when(repositorio.findById(idInexistente)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
        servico.findById(idInexistente);
    });

    verify(repositorio).findById(idInexistente);
}

@Test
public void updateDeveriaRetornarClientDTOQuandoIdExistir() {
    Client cliente = new Client(idExistente, "Ana", "22222222222", 6000.0, null, 0);
    ClientDTO dto = new ClientDTO(cliente);

    when(repositorio.getOne(idExistente)).thenReturn(cliente);
    when(repositorio.save(cliente)).thenReturn(cliente);

    ClientDTO resultado = servico.update(idExistente, dto);

    assertFalse(resultado == null);
    verify(repositorio).getOne(idExistente);
    verify(repositorio).save(cliente);
}

@Test
public void updateDeveriaLancarExcecaoQuandoIdNaoExistir() {
    ClientDTO dto = new ClientDTO();
    when(repositorio.getOne(idInexistente)).thenThrow(EntityNotFoundException.class);

    assertThrows(ResourceNotFoundException.class, () -> {
        servico.update(idInexistente, dto);
    });

    verify(repositorio).getOne(idInexistente);
}

@Test
public void insertDeveriaRetornarClientDTO() {
    Client cliente = new Client(null, "Pedro", "33333333333", 4500.0, null, 1);
    Client clienteSalvo = new Client(1L, "Pedro", "33333333333", 4500.0, null, 1);
    ClientDTO dto = new ClientDTO(cliente);

    when(repositorio.save(cliente)).thenReturn(clienteSalvo);

    ClientDTO resultado = servico.insert(dto);

    assertFalse(resultado == null);
    verify(repositorio).save(cliente);
}




}
