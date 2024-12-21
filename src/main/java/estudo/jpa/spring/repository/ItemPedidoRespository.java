package estudo.jpa.spring.repository;

import estudo.jpa.spring.modal.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRespository extends JpaRepository<ItemPedido, Integer> {
}
