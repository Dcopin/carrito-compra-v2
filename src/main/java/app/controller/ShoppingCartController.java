package app.controller;

import app.service.IProductoService;
import app.service.IShoppingCartService;
import app.model.ShoppingCart;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import java.util.UUID;

import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ToString
@Controller
public class ShoppingCartController {
    public static final String ATRIBUTO_CARRITO_SESION = ShoppingCartController.class.getName() + ".cartCode";
    private static final String REDIRECCION_CARRITO = "redirect:/shopping-cart";
    private final IProductoService productoService;
    private final IShoppingCartService carritoService;

    public ShoppingCartController(IProductoService productoService, IShoppingCartService carritoService) {
        this.productoService = productoService;
        this.carritoService = carritoService;
    }

    @GetMapping("/")
    public String mostrarProductos(Model modelo) {
        modelo.addAttribute("productos", productoService.getProductos());
        return "index";
    }

    @GetMapping("/shopping-cart")
    public String mostrarCarrito(Model modelo, HttpServletRequest solicitud) {
        String codigo = obtenerCodigoSesion(solicitud);
        modelo.addAttribute("shoppingCart", codigo == null ? new ShoppingCart() : carritoService.getShoppingCart(codigo));
        return "shopping-cart";
    }

    @PostMapping("/shopping-cart")
    public String agregarProducto(@RequestParam("idProducto") Integer idProducto, @RequestParam("cantidad") int cantidad, RedirectAttributes redireccion, HttpServletRequest solicitud) {
        HttpSession sesion = solicitud.getSession();
        String codigo;
        synchronized (sesion) {
            codigo = obtenerCodigoSesion(solicitud);
            if (codigo == null) {
                codigo = UUID.randomUUID().toString();
                sesion.setAttribute(ATRIBUTO_CARRITO_SESION, codigo);
            }
        }
        carritoService.addItem(codigo, idProducto, cantidad);
        redireccion.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return REDIRECCION_CARRITO;
    }

    @PostMapping("/shopping-cart/eliminar")
    public String eliminarProducto(@RequestParam("idProducto") Integer idProducto, RedirectAttributes redireccion,
                             HttpServletRequest solicitud) {
        String codigo = obtenerCodigoSesion(solicitud);
        if (codigo == null) {
            redireccion.addFlashAttribute("mensaje", "El producto no estaba en el carrito.");
            return REDIRECCION_CARRITO;
        }
        boolean eliminado = carritoService.deleteItem(codigo, idProducto);
        redireccion.addFlashAttribute("mensaje", eliminado
                ? "Producto eliminado del carrito."
                : "El producto no estaba en el carrito.");
        return REDIRECCION_CARRITO;
    }

    @PostMapping("/shopping-cart/vaciar")
    public String vaciarCarrito(RedirectAttributes redireccion, HttpServletRequest solicitud) {
        String codigo = obtenerCodigoSesion(solicitud);
        if (codigo == null) {
            redireccion.addFlashAttribute("mensaje", "El carrito ya estaba vacío.");
            return REDIRECCION_CARRITO;
        }
        boolean vaciado = carritoService.deleteAllItems(codigo);
        redireccion.addFlashAttribute("mensaje", vaciado
                ? "El carrito está vacío."
                : "El carrito ya estaba vacío.");
        return REDIRECCION_CARRITO;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String manejarErrorNegocio(IllegalArgumentException excepcion, RedirectAttributes redireccion) {
        redireccion.addFlashAttribute("error", "No se pudo completar la operación: " + excepcion.getMessage());
        return REDIRECCION_CARRITO;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public String manejarParametrosInvalidos(RedirectAttributes redireccion) {
        redireccion.addFlashAttribute("error", "Revisa los datos enviados: los parámetros requeridos deben ser números enteros.");
        return REDIRECCION_CARRITO;
    }

    private String obtenerCodigoSesion(HttpServletRequest solicitud) {
        HttpSession sesion = solicitud.getSession(false);
        Object valor = sesion == null ? null : sesion.getAttribute(ATRIBUTO_CARRITO_SESION);
        return valor instanceof String codigo && !codigo.isBlank() && codigo.length() <= 128 ? codigo : null;
    }

}
