package LógicaDeNegocio;

import java.util.ArrayList;
import java.util.List;

public class ArbolABB<T extends Comparable<T>> {

    // Clase interna Nodo
    private NodoABB raiz;

    public ArbolABB() {
        raiz = null;
    }
    // -------------------------------------------------
    // VERIFICAR SI EL ÁRBOL ESTÁ VACÍO
    // -------------------------------------------------

    public boolean estaVacio() {
        return raiz == null;
    }
    // -------------------------------------------------
    // INSERTAR
    // -------------------------------------------------

    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
    }

    private NodoABB insertarRecursivo(NodoABB<T> nodo, T dato) {
        // Se encontró la posición donde insertar
        if (nodo == null) {
            return new NodoABB(dato);
        }
        int comparacion = dato.compareTo(nodo.getDato());
        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), dato));
        }
        // Si comparacion == 0 no se inserta
        // porque no permitimos duplicados
        return nodo;
    }
// -------------------------------------------------
    // BUSCAR
    // -------------------------------------------------

    public T buscar(T dato) {
        NodoABB<T> nodoEncontrado = buscarRecursivo(raiz, dato);
        if (nodoEncontrado != null) {
            return nodoEncontrado.getDato();
        }
        return null;
    }

    private NodoABB<T> buscarRecursivo(NodoABB<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        int comparacion = dato.compareTo(nodo.getDato());
        if (comparacion == 0) {
            return nodo;
        }
        if (comparacion < 0) {
            return buscarRecursivo(nodo.getIzquierdo(), dato);
        }

        return buscarRecursivo(nodo.getDerecho(), dato);
    }
    // -------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------

    public void eliminar(T dato) {
        raiz = eliminarRecursivo(raiz, dato);
    }

    private NodoABB eliminarRecursivo(NodoABB<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        int comparacion = dato.compareTo(nodo.getDato());
        if (comparacion < 0) {
            nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), dato));
        } else {

            // -----------------------------------------
            // CASO 1:
            // Nodo sin hijo izquierdo
            // -----------------------------------------
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            // -----------------------------------------
            // CASO 2:
            // Nodo sin hijo derecho
            // -----------------------------------------
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // -----------------------------------------
            // CASO 3:
            // Nodo con dos hijos
            // -----------------------------------------
            NodoABB<T> sucesor = buscarMinimo(nodo.getDerecho());
            // Copiar el dato del sucesor
            nodo.setDato(sucesor.getDato());
            // Eliminar el sucesor
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), sucesor.getDato()));
        }
        return nodo;
    }
    // -------------------------------------------------
    // BUSCAR EL MENOR ELEMENTO DE UN SUBÁRBOL
    // -------------------------------------------------

    private NodoABB buscarMinimo(NodoABB<T> nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return buscarMinimo(nodo.getIzquierdo());
    }
// -------------------------------------------------
    // RECORRIDO INORDEN
    // -------------------------------------------------

    public void inOrden() {
        inOrdenRecursivo(raiz);
        System.out.println();
    }

    private void inOrdenRecursivo(NodoABB<T> nodo) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.getIzquierdo());
            System.out.print(nodo.getDato() + " ");
            inOrdenRecursivo(nodo.getDerecho());
        }
    }

    public List<T> obtenerOrdenados() {
        List<T> lista = new ArrayList<>();
        obtenerOrdenadosRecursivo(raiz, lista);
        return lista;
    }

    private void obtenerOrdenadosRecursivo(NodoABB<T> nodo, List<T> lista) {
        if (nodo != null) {
            obtenerOrdenadosRecursivo(nodo.getIzquierdo(), lista);
            lista.add(nodo.getDato());
            obtenerOrdenadosRecursivo(nodo.getDerecho(), lista);
        }
    }
// -------------------------------------------------
    // RECORRIDO PREORDEN
    // -------------------------------------------------

    public void preOrden() {
        preOrdenRecursivo(raiz);
        System.out.println();
    }

    private void preOrdenRecursivo(NodoABB<T> nodo) {
        if (nodo != null) {
            System.out.print(nodo.getDato() + " ");
            preOrdenRecursivo(nodo.getIzquierdo());
            preOrdenRecursivo(nodo.getDerecho());
        }
    }
// -------------------------------------------------
    // RECORRIDO POSTORDEN
    // -------------------------------------------------

    public void postOrden() {
        postOrdenRecursivo(raiz);
        System.out.println();
    }

    private void postOrdenRecursivo(NodoABB nodo) {
        if (nodo != null) {
            postOrdenRecursivo(nodo.getIzquierdo());
            postOrdenRecursivo(nodo.getDerecho());
            System.out.print(nodo.getDato() + " ");
        }
    }

    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(NodoABB nodo) {
        if (nodo == null) {
            return -1;
        }
        int alturaIzq = alturaRecursiva(nodo.getIzquierdo());
        int alturaDer = alturaRecursiva(nodo.getDerecho());

        return 1 + Math.max(alturaIzq, alturaDer);
    }

    public int contarNodos() {
        return contarNodosRecursivo(raiz);
    }

    private int contarNodosRecursivo(NodoABB nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarNodosRecursivo(nodo.getIzquierdo()) + contarNodosRecursivo(nodo.getDerecho());
    }

    public int contarHojas() {
        return contarHojasRecursivo(raiz);
    }

    private int contarHojasRecursivo(NodoABB<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return 1;
        }
        return contarHojasRecursivo(nodo.getIzquierdo()) + contarHojasRecursivo(nodo.getDerecho());
    }

    public int contarNodosInternos() {
        return contarNodosInternosRecursivo(raiz);
    }

    private int contarNodosInternosRecursivo(NodoABB<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return 0; // es hoja, no cuenta como interno
        }
        return 1 + contarNodosInternosRecursivo(nodo.getIzquierdo()) + contarNodosInternosRecursivo(nodo.getDerecho());
    }
}
