package app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Getter @Setter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private Double precio = 0.0;

    public Producto() {
    }

    public Producto(Integer idProducto, String nombre, String codigo, String imagen, Double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.precio = precio;
    }
}