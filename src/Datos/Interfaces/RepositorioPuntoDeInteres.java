package Datos.Interfaces;

import LógicaDeNegocio.Entidades.PuntoDeInteres;
import Datos.Interfaces.Excepciones.RepositorioLlenoExcepcion;

public interface RepositorioPuntoDeInteres {

    void agregar(PuntoDeInteres punto) throws RepositorioLlenoExcepcion;

    PuntoDeInteres obtener(int posicion);

    PuntoDeInteres buscarPorCodigo(int codigo);

    int cantidad();

    boolean estaLleno();
    //ES POSIBLE QUE SE NECESITEN MAS METODOS EN EL FUTURO
}
