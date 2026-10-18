package app.controller;

import app.service.IProductoService;
import app.service.IShoppingCartService;
import app.model.ShoppingCart;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private static final String COOKIE_AVISO = "cartNotice";
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
    public String mostrarCarrito(Model modelo, HttpServletRequest solicitud, HttpServletResponse respuesta) {
        String codigo = obtenerCodigoSesion(solicitud);
        modelo.addAttribute("shoppingCart", codigo == null ? new ShoppingCart() : carritoService.getShoppingCart(codigo));
        mostrarAvisoPendiente(modelo, solicitud, respuesta);
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
                             HttpServletRequest solicitud, HttpServletResponse respuesta) {
        String codigo = obtenerCodigoSesion(solicitud);
        if (codigo == null) {
            return redirigirConAviso(Aviso.PRODUCTO_AUSENTE, solicitud, respuesta, redireccion);
        }
        boolean eliminado = carritoService.deleteItem(codigo, idProducto);
        redireccion.addFlashAttribute("mensaje", eliminado
                ? "Producto eliminado del carrito."
                : "El producto no estaba en el carrito.");
        return REDIRECCION_CARRITO;
    }

    @PostMapping("/shopping-cart/vaciar")
    public String vaciarCarrito(RedirectAttributes redireccion, HttpServletRequest solicitud, HttpServletResponse respuesta) {
        String codigo = obtenerCodigoSesion(solicitud);
        if (codigo == null) {
            return redirigirConAviso(Aviso.CARRITO_VACIO, solicitud, respuesta, redireccion);
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
    public String manejarParametrosInvalidos(RedirectAttributes redireccion, HttpServletRequest solicitud, HttpServletResponse respuesta) {
        return redirigirConAviso(Aviso.PARAMETROS_INVALIDOS, solicitud, respuesta, redireccion);
    }

    private String obtenerCodigoSesion(HttpServletRequest solicitud) {
        HttpSession sesion = solicitud.getSession(false);
        Object valor = sesion == null ? null : sesion.getAttribute(ATRIBUTO_CARRITO_SESION);
        return valor instanceof String codigo && !codigo.isBlank() && codigo.length() <= 128 ? codigo : null;
    }

    private String redirigirConAviso(Aviso aviso, HttpServletRequest solicitud, HttpServletResponse respuesta,
                                  RedirectAttributes redireccion) {
        if (solicitud.getSession(false) != null) {
            redireccion.addFlashAttribute(aviso.atributo, aviso.mensaje);
        } else {
            respuesta.addCookie(crearCookieAviso(aviso.name(), 60, solicitud));
        }
        return REDIRECCION_CARRITO;
    }

    private void mostrarAvisoPendiente(Model modelo, HttpServletRequest solicitud, HttpServletResponse respuesta) {
        Cookie[] galletas = solicitud.getCookies();
        if (galletas == null) {
            return;
        }
        for (Cookie galleta : galletas) {
            if (COOKIE_AVISO.equals(galleta.getName())) {
                for (Aviso aviso : Aviso.values()) {
                    if (aviso.name().equals(galleta.getValue()) && !modelo.containsAttribute(aviso.atributo)) {
                        modelo.addAttribute(aviso.atributo, aviso.mensaje);
                    }
                }
                respuesta.addCookie(crearCookieAviso("", 0, solicitud));
                return;
            }
        }
    }

    private Cookie crearCookieAviso(String valor, int duracionSegundos, HttpServletRequest solicitud) {
        Cookie galleta = new Cookie(COOKIE_AVISO, valor);
        galleta.setPath(solicitud.getContextPath() + "/shopping-cart");
        galleta.setHttpOnly(true);
        galleta.setSecure(solicitud.isSecure());
        galleta.setMaxAge(duracionSegundos);
        galleta.setAttribute("SameSite", "Lax");
        return galleta;
    }

    private enum Aviso {
        PRODUCTO_AUSENTE("mensaje", "El producto no estaba en el carrito."),
        CARRITO_VACIO("mensaje", "El carrito ya estaba vacío."),
        PARAMETROS_INVALIDOS("error", "Revisa los datos enviados: los parámetros requeridos deben ser números enteros.");

        private final String atributo;
        private final String mensaje;

        Aviso(String atributo, String mensaje) {
            this.atributo = atributo;
            this.mensaje = mensaje;
        }
    }
}
