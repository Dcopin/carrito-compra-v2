package app.controller;

import app.model.Producto;
import app.model.ShoppingCart;
import app.service.IProductoService;
import app.service.IShoppingCartService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShoppingCartControllerTest {
    private IProductoService productos;
    private IShoppingCartService carrito;
    private ShoppingCartController controlador;
    private MockHttpServletRequest solicitud;
    private ExtendedModelMap modelo;
    private RedirectAttributesModelMap redireccion;
    private static final String CODIGO = "sesion-prueba";

    @Before
    public void before() {
        productos = mock(IProductoService.class);
        carrito = mock(IShoppingCartService.class);
        controlador = new ShoppingCartController(productos, carrito);
        solicitud = new MockHttpServletRequest();
        modelo = new ExtendedModelMap();
        redireccion = new RedirectAttributesModelMap();
    }

    private void prepararSesion() {
        solicitud.getSession().setAttribute(
                ShoppingCartController.ATRIBUTO_CARRITO_SESION, CODIGO);
    }

    @Test
    public void testMostrarProductosExitoso() {
        List<Producto> catalogo = List.of(new Producto());
        when(productos.getProductos()).thenReturn(catalogo);
        assertEquals("index", controlador.mostrarProductos(modelo));
        assertSame(catalogo, modelo.get("productos"));
        verify(productos).getProductos();
        verifyNoInteractions(carrito);
    }

    @Test(expected = IllegalStateException.class)
    public void testMostrarProductosFallido() {
        when(productos.getProductos()).thenThrow(new IllegalStateException("Consulta fallida"));
        controlador.mostrarProductos(modelo);
    }

    @Test
    public void testMostrarCarritoExitoso() {
        prepararSesion();
        ShoppingCart esperado = new ShoppingCart();
        when(carrito.getShoppingCart(CODIGO)).thenReturn(esperado);
        assertEquals("shopping-cart", controlador.mostrarCarrito(modelo, solicitud));
        assertSame(esperado, modelo.get("shoppingCart"));
        verify(carrito).getShoppingCart(CODIGO);
    }

    @Test(expected = IllegalStateException.class)
    public void testMostrarCarritoFallido() {
        prepararSesion();
        when(carrito.getShoppingCart(CODIGO)).thenThrow(new IllegalStateException("Datos invalidos"));
        controlador.mostrarCarrito(modelo, solicitud);
    }

    @Test
    public void testAgregarProductoExitoso() {
        assertEquals("redirect:/shopping-cart",
                controlador.agregarProducto(1, 2, redireccion, solicitud));
        String codigo = (String) solicitud.getSession(false).getAttribute(
                ShoppingCartController.ATRIBUTO_CARRITO_SESION);
        assertNotNull(codigo);
        assertEquals(codigo, UUID.fromString(codigo).toString());
        verify(carrito).addItem(codigo, 1, 2);
        assertEquals("Producto agregado al carrito.", redireccion.getFlashAttributes().get("mensaje"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarProductoFallido() {
        prepararSesion();
        when(carrito.addItem(CODIGO, 1, 0)).thenThrow(new IllegalArgumentException("Cantidad invalida"));
        controlador.agregarProducto(1, 0, redireccion, solicitud);
    }

    @Test
    public void testAgregarProductoReutilizaCodigo() {
        prepararSesion();
        controlador.agregarProducto(1, 2, redireccion, solicitud);
        verify(carrito).addItem(CODIGO, 1, 2);
        assertEquals(CODIGO, solicitud.getSession(false).getAttribute(
                ShoppingCartController.ATRIBUTO_CARRITO_SESION));
    }

    @Test
    public void testEliminarProductoExitoso() {
        prepararSesion();
        when(carrito.deleteItem(CODIGO, 1)).thenReturn(true);
        assertEquals("redirect:/shopping-cart",
                controlador.eliminarProducto(1, redireccion, solicitud));
        assertEquals("Producto eliminado del carrito.", redireccion.getFlashAttributes().get("mensaje"));
        verify(carrito).deleteItem(CODIGO, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEliminarProductoFallido() {
        prepararSesion();
        when(carrito.deleteItem(CODIGO, null)).thenThrow(new IllegalArgumentException("ID invalido"));
        controlador.eliminarProducto(null, redireccion, solicitud);
    }

    @Test
    public void testEliminarProductoAusente() {
        prepararSesion();
        when(carrito.deleteItem(CODIGO, 1)).thenReturn(false);
        assertEquals("redirect:/shopping-cart",
                controlador.eliminarProducto(1, redireccion, solicitud));
        assertEquals("El producto no estaba en el carrito.", redireccion.getFlashAttributes().get("mensaje"));
    }

    @Test
    public void testVaciarCarritoExitoso() {
        prepararSesion();
        when(carrito.deleteAllItems(CODIGO)).thenReturn(true);
        assertEquals("redirect:/shopping-cart", controlador.vaciarCarrito(redireccion, solicitud));
        assertEquals("El carrito está vacío.", redireccion.getFlashAttributes().get("mensaje"));
        verify(carrito).deleteAllItems(CODIGO);
    }

    @Test(expected = IllegalStateException.class)
    public void testVaciarCarritoFallido() {
        prepararSesion();
        when(carrito.deleteAllItems(CODIGO)).thenThrow(new IllegalStateException("Error al eliminar"));
        controlador.vaciarCarrito(redireccion, solicitud);
    }

    @Test
    public void testVaciarCarritoYaVacio() {
        prepararSesion();
        when(carrito.deleteAllItems(CODIGO)).thenReturn(false);
        assertEquals("redirect:/shopping-cart", controlador.vaciarCarrito(redireccion, solicitud));
        assertEquals("El carrito ya estaba vacío.", redireccion.getFlashAttributes().get("mensaje"));
    }

    @Test
    public void testConsultarSinSesionNoLaCrea() {
        assertEquals("shopping-cart", controlador.mostrarCarrito(modelo, solicitud));
        assertTrue(((ShoppingCart) modelo.get("shoppingCart")).getItem().isEmpty());
        assertNull(solicitud.getSession(false));
        verifyNoInteractions(carrito);
    }

    @Test
    public void testEliminarSinSesionGeneraAviso() {
        assertEquals("redirect:/shopping-cart",
                controlador.eliminarProducto(1, redireccion, solicitud));
        assertEquals("El producto no estaba en el carrito.", redireccion.getFlashAttributes().get("mensaje"));
        assertNull(solicitud.getSession(false));
        verifyNoInteractions(carrito);
    }

    @Test
    public void testVaciarSinSesionGeneraAviso() {
        assertEquals("redirect:/shopping-cart", controlador.vaciarCarrito(redireccion, solicitud));
        assertEquals("El carrito ya estaba vacío.", redireccion.getFlashAttributes().get("mensaje"));
        assertNull(solicitud.getSession(false));
        verifyNoInteractions(carrito);
    }

    @Test
    public void testManejarErrorNegocio() {
        assertEquals("redirect:/shopping-cart", controlador.manejarErrorNegocio(
                new IllegalArgumentException("Cantidad invalida"), redireccion));
        assertEquals("No se pudo completar la operación: Cantidad invalida",
                redireccion.getFlashAttributes().get("error"));
        assertFalse(redireccion.getFlashAttributes().containsKey("mensaje"));
    }

    @Test
    public void testManejarParametrosInvalidos() {
        prepararSesion();
        assertEquals("redirect:/shopping-cart",
                controlador.manejarParametrosInvalidos(redireccion));
        assertEquals("Revisa los datos enviados: los parámetros requeridos deben ser números enteros.",
                redireccion.getFlashAttributes().get("error"));
        verifyNoInteractions(carrito, productos);
    }

    @Test
    public void testToString(){
        String obtenido = controlador.toString().toLowerCase();
        String esperado = ("ShoppingCartController(productoService=" + productos + ", carritoService=" + carrito + ")").toLowerCase();

        assertEquals(esperado,obtenido);
    }
}