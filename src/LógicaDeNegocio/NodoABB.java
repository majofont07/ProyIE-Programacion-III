package LógicaDeNegocio;

public class NodoABB<T> {

    private T dato;
    private NodoABB<T> izquierdo;
    private NodoABB<T> derecho;

    public NodoABB(T dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoABB<T> getIzquierdo() {
        return izquierdo;
    }

    public NodoABB<T> getDerecho() {
        return derecho;
    }

    public void setIzquierdo(NodoABB<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public void setDerecho(NodoABB<T> derecho) {
        this.derecho = derecho;
    }
}
