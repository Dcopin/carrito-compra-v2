package app.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ShoppingCartTest {

    private Producto p1;
    private ShoppingCart s1;

    @Before
    public void before(){
        p1 =new Producto(1,"Camara","CA01","camara.jpg",10.0);
        ItemProducto i1 = new ItemProducto(p1,2,"diego");
        s1 = new ShoppingCart(List.of(i1));
    }

    @Test
    public void testGetItemExitoso() {
        List<ItemProducto> resultado = s1.getItem();

        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getCantidad());
        assertSame(p1, resultado.get(0).getProducto());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetItemFallido() {
        p1.setPrecio(null);

        s1.getItem();
    }

    @Test
    public void testGetTotalExitoso() {
        assertEquals(20.0, s1.getTotal(), 0.000001);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTotalFallido() {
        p1.setPrecio(null);

        s1.getTotal();
    }

    @Test
    public void testToString(){
        String obtenido = s1.toString().toLowerCase();
        String esperado = "shoppingcart(item=[itemproducto(idshoppingcart=null, producto=producto(idproducto=1, nombre=camara, codigo=ca01, imagen=camara.jpg, precio=10.0), codusuario=diego, cantidad=2)])";

        assertEquals(esperado, obtenido);
    }


}