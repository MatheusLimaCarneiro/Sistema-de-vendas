package estudo.jpa.spring.service;

import estudo.jpa.spring.modal.Pedido;
import estudo.jpa.spring.modal.enums.StatusPedido;
import estudo.jpa.spring.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
