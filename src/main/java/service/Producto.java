package service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Getter @Setter
public class Producto {

    private Integer idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private double precio;

    public Producto() {
    }



}
