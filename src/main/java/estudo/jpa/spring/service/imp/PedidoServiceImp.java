package estudo.jpa.spring.service.imp;

import estudo.jpa.spring.exception.PedidoNaoEncontradoException;
import estudo.jpa.spring.exception.RegraNegocioException;
import estudo.jpa.spring.modal.Cliente;
import estudo.jpa.spring.modal.ItemPedido;
import estudo.jpa.spring.modal.Pedido;
import estudo.jpa.spring.modal.Produto;
import estudo.jpa.spring.modal.enums.StatusPedido;
import estudo.jpa.spring.repository.ClientesRepository;
import estudo.jpa.spring.repository.ItemPedidoRespository;
import estudo.jpa.spring.repository.PedidoRepository;
import estudo.jpa.spring.repository.ProdutoRepository;
import estudo.jpa.spring.rest.dto.ItemPedidoDTO;
import estudo.jpa.spring.rest.dto.PedidoDTO;
import estudo.jpa.spring.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImp implements PedidoService {


    private final PedidoRepository pedidos;

    private final ClientesRepository clientes;

    private final ProdutoRepository produtos;

    private final ItemPedidoRespository itemPedidoRespository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientes.findById(idCliente)
                .orElseThrow(() ->
                        new RegraNegocioException("Código de cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setLocalDate(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedidos = converterItems(pedido, dto.getItems());
        pedidos.save(pedido);
        itemPedidoRespository.saveAll(itemsPedidos);
        pedido.setItens((itemsPedidos));
        return pedido;
    }


    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if (items.isEmpty()){
            throw new RegraNegocioException("Não é possivel realizar pedido sem items");
        }
        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtos.findById(idProduto)
                            .orElseThrow(() ->
                                    new RegraNegocioException("Código de produto invalido: " +
                                            idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }


    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidos.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidos
                .findById(id)
                .map(pedido ->{
                    pedido.setStatus(statusPedido);
                    return pedidos.save(pedido);
                })
                .orElseThrow(()->
                        new PedidoNaoEncontradoException() );
    }
}
