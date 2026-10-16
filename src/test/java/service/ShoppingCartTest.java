package service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShoppingCartTest {

    private ShoppingCart s1;

    @Before
    public void before(){
        s1 = new ShoppingCart();
    }

    @Test
    public void testToString(){
        String obtenido = s1.toString().toLowerCase();
        String esperado = "shoppingcart(idshoppingcart=null, producto=[], codusuario=null, cantidad=0)";

        assertEquals(esperado, obtenido);
    }

    @Test
    public void testAddProducto(){
        boolean obtenido = s1.addProducto(new Producto());
        assertTrue(obtenido);
    }
}