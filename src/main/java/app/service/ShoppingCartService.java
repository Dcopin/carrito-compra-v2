package app.service;

import app.model.ItemProducto;
import app.model.Producto;
import app.model.ShoppingCart;
import app.repository.IItemProductoRepository;
import app.repository.IProductoRepository;
import jakarta.transaction.Transactional;
import lombok.ToString;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@ToString
@Service
@Transactional
public class ShoppingCartService implements IShoppingCartService{

    private final IItemProductoRepository items;
    private final IProductoRepository productos;

    public ShoppingCartService(IItemProductoRepository items, IProductoRepository productos) {
        this.items = items;
        this.productos = productos;
    }

    @Override
    @Transactional
    public ShoppingCart getShoppingCart(String codUsuario) {
        validarUsuario(codUsuario);
        return new ShoppingCart(items.findByCodUsuarioOrderByIdShoppingCartAsc(codUsuario));
    }

    @Override
    public ShoppingCart addItem(String codUsuario, Integer idProducto, int cantidad) {
        validarProductoYCantidad(codUsuario, idProducto, cantidad);
        Producto product = buscarProducto(idProducto);
        ItemProducto itemNuevo = new ItemProducto(product, cantidad, codUsuario);
        List<ItemProducto> itemCarrito = items.findByCodUsuarioOrderByIdShoppingCartAsc(codUsuario);
        List<ItemProducto> validarItem = buscarItems(itemCarrito, idProducto);
        long total = cantidadAcumulada(validarItem, cantidad);
        validarItemsExistentes(itemCarrito);

        ItemProducto itemGuardado = guardarItem(itemNuevo, validarItem, total);
        eliminarItemsDuplicados(codUsuario, validarItem);
        return carritoActualizado(itemCarrito, validarItem, itemGuardado);
    }

    @Override
    public boolean deleteItem(String codUsuario, Integer idProducto) {
        validarUsuario(codUsuario);
        if (idProducto == null) {
            throw new IllegalArgumentException("El identificador no puede ser nulo");
        }
        return items.deleteByCodUsuarioAndProducto_IdProducto(codUsuario, idProducto) > 0;
    }

    @Override
    public boolean deleteAllItems(String codUsuario) {
        validarUsuario(codUsuario);
        return items.deleteByCodUsuario(codUsuario) > 0;
    }

    private Producto buscarProducto(Integer idProducto) {
        return productos.findById(idProducto).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
    }

    private void validarProductoYCantidad(String codUsuario, Integer idProducto, int cantidad) {
        validarUsuario(codUsuario);
        if (idProducto == null || cantidad <= 0) {
            throw new IllegalArgumentException("Identificador obligatorio y cantidad positiva");
        }
    }

    private List<ItemProducto> buscarItems(List<ItemProducto> itemCarrito, Integer idProducto) {
        return itemCarrito.stream().filter(linea -> linea.getProducto() != null && idProducto.equals(linea.getProducto().getIdProducto())).toList();
    }

    private long cantidadAcumulada(List<ItemProducto> validarItem, int cantidad) {
        long total = cantidad;
        for (ItemProducto linea : validarItem) {
            total += linea.getCantidad();
        }
        return total;
    }

    private void validarItemsExistentes(List<ItemProducto> itemCarrito) {
        new ShoppingCart(itemCarrito);
    }

    private ItemProducto guardarItem(ItemProducto itemNuevo, List<ItemProducto> validarItems, long total) {
        ItemProducto itemObjetivo = itemNuevo;
        if (!validarItems.isEmpty()) {
            itemObjetivo = validarItems.getFirst();
            itemObjetivo.sumaCantidad((int) total - itemObjetivo.getCantidad());
        }
        return items.save(itemObjetivo);
    }

    private void eliminarItemsDuplicados(String codUsuario, List<ItemProducto> validarItems) {
        if (validarItems.size() > 1) {
            List<Integer> idDuplicados = validarItems.subList(1, validarItems.size()).stream().map(ItemProducto::getIdShoppingCart).toList();
            items.deleteByCodUsuarioAndIdShoppingCartIn(codUsuario, idDuplicados);
        }
    }

    private ShoppingCart carritoActualizado(List<ItemProducto> itemCarrito, List<ItemProducto> validarItems, ItemProducto itemGuardado) {
        List<ItemProducto> result = new ArrayList<>();
        boolean itemIncluido = false;
        for (ItemProducto linea : itemCarrito) {
            if (validarItems.contains(linea)) {
                if (!itemIncluido) {
                    result.add(itemGuardado);
                    itemIncluido = true;
                }
            } else {
                result.add(linea);
            }
        }
        if (!itemIncluido) {
            result.add(itemGuardado);
        }
        return new ShoppingCart(result);
    }

    private void validarUsuario(String codUsuario) {
        if (codUsuario == null || codUsuario.isBlank() || codUsuario.length() > 128) {
            throw new IllegalArgumentException("Codigo de Usuario no valido");
        }
    }
}