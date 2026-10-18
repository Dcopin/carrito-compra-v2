package app.service;

import app.model.ShoppingCart;
import lombok.ToString;


public interface IShoppingCartService {
    ShoppingCart getShoppingCart(String codUsuario);
    ShoppingCart addItem(String codUsuario, Integer idProducto, int cantidad);
    boolean deleteItem(String codUsuario, Integer idProducto);
    boolean deleteAllItems(String codUsuario);
}
