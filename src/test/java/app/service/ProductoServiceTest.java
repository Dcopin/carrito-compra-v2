package app.service;

import app.model.Producto;
import app.repository.IProductoRepository;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    private IProductoRepository repositorio;
    private ProductoService servicio;
    private Producto producto;

    @Before
    public void before() {
        repositorio = mock(IProductoRepository.class);
        servicio = new ProductoService(repositorio);

        producto = new Producto(
                1, "Camara", "CAM01", "camera.jpg", 10.0);
    }

    // getProductos()

    @Test
    public void testGetProductosExitoso() {
        when(repositorio.findAll())
                .thenReturn(List.of(producto));

        List<Producto> resultado = servicio.getProductos();

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));

        verify(repositorio).findAll();
    }

    @Test(expected = RuntimeException.class)
    public void testGetProductosFallido() {
        when(repositorio.findAll())
                .thenThrow(new RuntimeException("Error de consulta"));

        servicio.getProductos();
    }

    // getProductoById()

    @Test
    public void testGetProductoByIdExitoso() {
        when(repositorio.findById(1))
                .thenReturn(Optional.of(producto));

        Producto resultado = servicio.getProductoById(1);

        assertSame(producto, resultado);

        verify(repositorio).findById(1);
    }

    @Test
    public void testGetProductoByIdNoEncontrado() {
        when(repositorio.findById(99))
                .thenReturn(Optional.empty());

        Producto resultado = servicio.getProductoById(99);

        assertNull(resultado);

        verify(repositorio).findById(99);
    }

    @Test(expected = RuntimeException.class)
    public void testGetProductoByIdFallido() {
        when(repositorio.findById(1))
                .thenThrow(new RuntimeException("Error de consulta"));

        servicio.getProductoById(1);
    }
}