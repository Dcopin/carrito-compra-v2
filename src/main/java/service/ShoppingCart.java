package service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.LinkedList;
import java.util.List;

@ToString @Setter @Getter
public class ShoppingCart {
    private Integer idShoppingCart;
    private List<Producto> producto;
    private String codUsuario;
    private int cantidad;

    public ShoppingCart(){
        producto = new LinkedList<>();
    }

    public boolean addProducto(Producto p1){
        producto.add(p1);
        return true;
    }

}
