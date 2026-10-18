package app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.Objects;

@ToString @Setter @Getter
public class ShoppingCart {

    private final List<ItemProducto> item;

    public ShoppingCart() {
        this(List.of());
    }

    public ShoppingCart(List<ItemProducto> lineas){
        Objects.requireNonNull(lineas, "No puede ser nulo");
        item = lineas.stream().map(ItemProducto::item).toList();
    }

    public List<ItemProducto> getItem(){
        return item.stream().map(ItemProducto::item).toList();
    }

    public Double getTotal(){
        return item.stream().map(ItemProducto::getSubtotal).reduce(0.0,Double::sum);
    }
}
