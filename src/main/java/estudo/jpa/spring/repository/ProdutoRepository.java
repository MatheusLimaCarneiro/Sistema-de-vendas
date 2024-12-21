package estudo.jpa.spring.repository;

import estudo.jpa.spring.modal.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
