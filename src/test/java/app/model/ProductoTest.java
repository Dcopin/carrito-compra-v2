package app.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class ProductoTest {

    private Producto p1;

    @Before
    public void before(){
        p1 = new Producto();
    }

    @Test
    public void testToString(){
        String obtenido = p1.toString().toLowerCase();
        String esperado = "producto(idproducto=null, nombre=null, codigo=null, imagen=null, precio=0.0)";

        assertEquals(esperado,obtenido);
    }

}