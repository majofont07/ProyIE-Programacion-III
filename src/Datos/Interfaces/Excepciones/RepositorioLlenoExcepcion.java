package Datos.Interfaces.Excepciones;

public class RepositorioLlenoExcepcion extends Exception {

    public RepositorioLlenoExcepcion(String mensaje) {
        super(mensaje);
    }
}
