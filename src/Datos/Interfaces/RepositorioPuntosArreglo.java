package Datos.Interfaces;

import Datos.Interfaces.Excepciones.*;
import LógicaDeNegocio.Entidades.PuntoDeInteres;

public class RepositorioPuntosArreglo implements RepositorioPuntoDeInteres {

    //---(ATRIBUTOS)---
    private static final int CAPACIDAD_MAXIMA = 30;
    private PuntoDeInteres[] puntos;
    private int cantidad;

    //---(CONSTRUCTOR)---
    public RepositorioPuntosArreglo() {
        this.puntos = new PuntoDeInteres[CAPACIDAD_MAXIMA];
        this.cantidad = 0;
    }

    //---(DESARROLLO MÉTODOS ABS)---
    @Override
    public void agregar(PuntoDeInteres punto) throws RepositorioLlenoExcepcion {
        if (estaLleno()) {
            throw new RepositorioLlenoExcepcion("Repositorio lleno (max. " + CAPACIDAD_MAXIMA + ").");
        }
        puntos[cantidad] = punto;
        cantidad++;
    }

    @Override
    public PuntoDeInteres obtener(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            return null;
        }
        return puntos[posicion];
    }

    @Override
    public PuntoDeInteres buscarPorCodigo(int codigo) {
        return buscarPorCodigoRecursivo(codigo, 0);
    }

    //---(MÉTODO RECURSIVO)---
    private PuntoDeInteres buscarPorCodigoRecursivo(int codigo, int indice) {
        if (indice >= cantidad) {
            return null; // No encontrado
        }
        if (puntos[indice].getCodigo() == codigo) {
            return puntos[indice];
        }
        return buscarPorCodigoRecursivo(codigo, indice + 1);
    }

    @Override
    public int cantidad() {
        return cantidad;
    }

    @Override
    public boolean estaLleno() {
        return cantidad >= CAPACIDAD_MAXIMA;
    }

}
