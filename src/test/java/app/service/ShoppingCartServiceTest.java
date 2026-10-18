package app.service;

import app.model.ItemProducto;
import app.model.Producto;
import app.model.ShoppingCart;
import app.repository.IItemProductoRepository;
import app.repository.IProductoRepository;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    private IItemProductoRepository items;
    private IProductoRepository productos;
    private ShoppingCartService servicio;

    private Producto producto;
    private static final String CODIGO = "diego";

    @Before
    public void before() {
        items = mock(IItemProductoRepository.class);
        productos = mock(IProductoRepository.class);

        servicio = new ShoppingCartService(items, productos);

        producto = new Producto(
                1, "Camara", "CAM01", "camera.jpg", 10.0);
    }

    @Test
    public void testGetShoppingCartExitoso() {
        ItemProducto i1 =
                new ItemProducto(producto, 2, CODIGO);

        when(items.findByCodUsuarioOrderByIdShoppingCartAsc(CODIGO)).thenReturn(List.of(i1));

        ShoppingCart resultado = servicio.getShoppingCart(CODIGO);

        assertEquals(1, resultado.getItem().size());
        assertEquals(2, resultado.getItem().get(0).getCantidad());
        assertEquals(20.0, resultado.getTotal(), 0.000001);

        verify(items).findByCodUsuarioOrderByIdShoppingCartAsc(CODIGO);
        verifyNoInteractions(productos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetShoppingCartFallido() {
        servicio.getShoppingCart(null);
    }

    @Test
    public void testAddItemExitoso() {
        when(productos.findById(1)).thenReturn(Optional.of(producto));

        when(items.findByCodUsuarioOrderByIdShoppingCartAsc(CODIGO)).thenReturn(List.of());

        when(items.save(any(ItemProducto.class))).thenAnswer(invocacion -> {
                    ItemProducto recibido = invocacion.getArgument(0);

                    assertSame(producto, recibido.getProducto());
                    assertEquals(2, recibido.getCantidad());
                    assertEquals(CODIGO, recibido.getCodUsuario());

                    return recibido;
                });

        ShoppingCart resultado = servicio.addItem(CODIGO, 1, 2);

        assertEquals(1, resultado.getItem().size());
        assertEquals(2, resultado.getItem().get(0).getCantidad());
        assertEquals(20.0, resultado.getTotal(), 0.000001);

        verify(productos).findById(1);
        verify(items).save(any(ItemProducto.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemFallido() {
        when(productos.findById(99)).thenReturn(Optional.empty());

        servicio.addItem(CODIGO, 99, 2);
    }

    @Test
    public void testDeleteItemExitoso() {
        when(items.deleteByCodUsuarioAndProducto_IdProducto(CODIGO, 1)).thenReturn(1L);

        assertTrue(servicio.deleteItem(CODIGO, 1));

        verify(items).deleteByCodUsuarioAndProducto_IdProducto(CODIGO, 1);
        verifyNoInteractions(productos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteItemFallido() {
        servicio.deleteItem(CODIGO, null);
    }

    @Test
    public void testDeleteAllItemsExitoso() {
        when(items.deleteByCodUsuario(CODIGO)).thenReturn(2L);

        assertTrue(servicio.deleteAllItems(CODIGO));

        verify(items).deleteByCodUsuario(CODIGO);
        verifyNoInteractions(productos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAllItemsFallido() {
        servicio.deleteAllItems("");
    }

    @Test
    public void testToString(){
        String obtenido = servicio.toString().toLowerCase();
        String esperado = "shoppingcartservice(items=mock for iitemproductorepository, hashcode: 37841489, productos=mock for iproductorepository, hashcode: 507061061)";

        assertEquals(esperado,obtenido);
    }
}