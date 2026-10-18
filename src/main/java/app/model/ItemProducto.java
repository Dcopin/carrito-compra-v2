package app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Setter @Getter
@Entity
@Table(name = "shopping_cart")
public class ItemProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idShoppingCart;
    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
    private String codUsuario;
    private Integer cantidad;

    protected ItemProducto() {
    }

    public ItemProducto(Producto producto, int cantidad, String codUsuario) {

        Double precio = producto.getPrecio();

        if (precio == null || precio < 0) {
            throw new IllegalArgumentException("Precio Invalido");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.codUsuario = codUsuario;
    }

    public int getCantidad(){
        if(cantidad == null || cantidad <= 0){
            throw new IllegalStateException("Cantidad Invalida");
        }
        return  cantidad;
    }

    public void sumaCantidad(int cant){
        int cantidadActual = getCantidad();
        if (cant <= 0){
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        cantidad = cantidadActual + cant;
    }

    public Double getSubtotal() {
        if (producto == null || producto.getPrecio() == null || producto.getPrecio() < 0) {
            throw new IllegalStateException(
                    "Producto con precio invalido");
        }
        return producto.getPrecio() * getCantidad();
    }

    public ItemProducto item(){
        getSubtotal();
        ItemProducto item = new ItemProducto(producto, getCantidad(), codUsuario);
        item.idShoppingCart = idShoppingCart;
        return item;
    }
}
