package app.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;


public class ItemProductoTest {

    private ItemProducto i1;

    @Before
    public void before(){
        Producto p1 = new Producto(1,"Camara","Ca01","camara.jpg",10.0);
        i1 = new ItemProducto(p1,2,"diego");
    }

    @Test
    public void testGetCantidadExitoso(){
        assertEquals(2, i1.getCantidad());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCantidadFallido(){
        i1.setCantidad(null);
        i1.getCantidad();
    }

    @Test
    public void testSumaCantidadExitosa() {
        i1.sumaCantidad(3);

        assertEquals(5, i1.getCantidad());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSumaCantidadFallido() {
        i1.sumaCantidad(-1);
    }

    @Test
    public void testGetSubtotalExitoso() {
        assertEquals(20.0, i1.getSubtotal(), 0.000001);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSubtotalFallido() {
        i1.setProducto(null);

        i1.getSubtotal();
    }

    @Test
    public void testItemExitoso() {
        i1.setIdShoppingCart(10);

        ItemProducto copia = i1.item();

        assertNotSame(i1, copia);
        assertEquals(i1.getIdShoppingCart(),
                copia.getIdShoppingCart());
        assertEquals(i1.getCodUsuario(),
                copia.getCodUsuario());
        assertEquals(i1.getCantidad(),
                copia.getCantidad());
        assertSame(i1.getProducto(), copia.getProducto());
    }

    @Test(expected = IllegalStateException.class)
    public void testItemFallido() {
        i1.setProducto(null);

        i1.item();
    }

    @Test
    public void testToString(){
        String obtenido = i1.toString().toLowerCase();
        String esperado = "itemproducto(idshoppingcart=null, producto=producto(idproducto=1, nombre=camara, codigo=ca01, imagen=camara.jpg, precio=10.0), codusuario=diego, cantidad=2)";

        assertEquals(esperado,obtenido);
    }

}