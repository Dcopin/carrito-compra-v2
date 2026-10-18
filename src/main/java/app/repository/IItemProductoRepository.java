package app.repository;

import app.model.ItemProducto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IItemProductoRepository {
    @EntityGraph(attributePaths = "producto")
    List<ItemProducto> findByCodUsuarioOrderByIdShoppingCartAsc(String codUsuario);
    ItemProducto save(ItemProducto item);
    long deleteByCodUsuarioAndProducto_IdProducto(String codUsuario, Integer idProducto);
    long deleteByCodUsuario(String codUsuario);
    long deleteByCodUsuarioAndIdShoppingCartIn(String codUsuario, List<Integer> ids);
}
