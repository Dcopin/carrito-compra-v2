package app.service;

import app.model.Producto;

import java.util.List;

public interface IProductoService {

    List<Producto> getProductos();
    Producto getProductoById(Integer idProducto);
}
