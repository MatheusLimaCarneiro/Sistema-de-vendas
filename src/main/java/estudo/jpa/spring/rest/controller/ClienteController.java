package estudo.jpa.spring.rest.controller;

import estudo.jpa.spring.modal.Cliente;
import estudo.jpa.spring.repository.ClientesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClientesRepository clientes;

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Integer id){
        return clientes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente com ID " + id + " não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clientes.save(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        clientes.findById(id)
                .ifPresentOrElse(
                        cliente -> clientes.delete(cliente),
                        () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"); }
                );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable @Valid Integer id,
                                 @RequestBody Cliente cliente){
        clientes
                .findById(id)
                .map( clienteExistente-> {
                    cliente.setId((clienteExistente.getId()));
                    clientes.save(cliente);
                    return cliente;
                })
                .orElseThrow(()->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @GetMapping
    public List<Cliente> find(Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);

        return clientes.findAll(example);

    }
}
