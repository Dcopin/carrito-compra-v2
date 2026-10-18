package app.service;

import app.model.Producto;
import app.repository.IProductoRepository;
import lombok.ToString;
import org.springframework.stereotype.Service;
import java.util.List;

@ToString
@Service
public class ProductoService implements IProductoService{

    private final IProductoRepository productoRepo;

    public ProductoService(IProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    @Override
    public List<Producto> getProductos() {
        return productoRepo.findAll();
    }

    @Override
    public Producto getProductoById(Integer idProducto) {
        return productoRepo.findById(idProducto).orElse(null);
    }
}