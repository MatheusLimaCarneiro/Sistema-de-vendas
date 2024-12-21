package estudo.jpa.spring.repository;

import estudo.jpa.spring.modal.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientesRepository extends JpaRepository<Cliente, Integer> {

}