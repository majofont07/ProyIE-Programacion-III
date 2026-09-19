package LógicaDeNegocio;

import Datos.Interfaces.RepositorioPuntoDeInteres;
import Datos.Interfaces.Excepciones.*;
import Datos.Interfaces.RepositorioPuntosArreglo;
import LógicaDeNegocio.Entidades.Mirador;
import LógicaDeNegocio.Entidades.PuntoDeInteres;
import java.util.List;

public class GestorPuntoDeInteres {

    private RepositorioPuntoDeInteres repositorio;
    private ArbolABB<PuntoDeInteres> arbolABB;

    public GestorPuntoDeInteres(RepositorioPuntoDeInteres repositorio) {
        this.repositorio = repositorio;
        this.arbolABB = new ArbolABB<>();
    }

    // --- 1. REGISTRAR PUNTO CON VALIDACIONES ---
    public void registrarPunto(PuntoDeInteres punto) throws DatoInvalidoExcepcion, CodigoDuplicadoExcepcion, RepositorioLlenoExcepcion {
        // Validar código > 0
        if (punto.getCodigo() <= 0) {
            throw new DatoInvalidoExcepcion("El código debe ser un número entero positivo.");
        }
        // Validar nombre no vacío
        if (punto.getNombre() == null || punto.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoExcepcion("El nombre no puede estar vacío.");
        }
        // Validar altitud >= 0
        if (punto.getAltitud() < 0) {
            throw new DatoInvalidoExcepcion("La altitud debe ser mayor o igual a cero.");
        }
        // Validar nivel de accesibilidad entre 1 y 5
        int nivel = punto.getNivelAccesibilidad();
        if (nivel < 1 || nivel > 5) {
            throw new DatoInvalidoExcepcion("El nivel de accesibilidad debe estar entre 1 y 5.");
        }

        // Verificar código duplicado
        if (repositorio.buscarPorCodigo(punto.getCodigo()) != null) {
            throw new CodigoDuplicadoExcepcion("Ya existe un punto con el código " + punto.getCodigo() + ".");
        }

        // Agregar al repositorio
        repositorio.agregar(punto);
        arbolABB.insertar(punto);
    }

    // --- 2. MOSTRAR TODOS  ---
    public void mostrarTodos() {
        if (repositorio.cantidad() == 0) {
            System.out.println("No hay puntos de interés registrados.");
            return;
        }
        mostrarTodosRecursivo(0);
    }

    private void mostrarTodosRecursivo(int posicion) {
        if (posicion >= repositorio.cantidad()) {
            return;
        }
        PuntoDeInteres p = repositorio.obtener(posicion);
        if (p != null) {
            p.mostrarInformacion();
            System.out.println("-------------------------");
        }
        mostrarTodosRecursivo(posicion + 1);
    }

    // --- 3. BUSCAR POR CÓDIGO ---
    public PuntoDeInteres buscarPorCodigo(int codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    // --- 4. CONTAR POR TIPO  ---
    public int contarPorTipo(String tipoBuscado) {
        return contarPorTipoRecursivo(tipoBuscado, 0);
    }

    private int contarPorTipoRecursivo(String tipoBuscado, int posicion) {
        if (posicion >= repositorio.cantidad()) {
            return 0;
        }
        PuntoDeInteres p = repositorio.obtener(posicion);
        int contador = 0;
        if (p != null && p.obtenerTipo().equals(tipoBuscado)) {
            contador = 1;
        }
        return contador + contarPorTipoRecursivo(tipoBuscado, posicion + 1);
    }

    // --- 5. MAYOR ALTITUD ---
    public PuntoDeInteres determinarMayorAltitud() {
        if (repositorio.cantidad() == 0) {
            return null;
        }
        return mayorAltitudRecursivo(0);
    }

    private PuntoDeInteres mayorAltitudRecursivo(int posicion) {
        if (posicion >= repositorio.cantidad() - 1) {
            return repositorio.obtener(posicion);
        }
        PuntoDeInteres actual = repositorio.obtener(posicion);
        PuntoDeInteres mayorResto = mayorAltitudRecursivo(posicion + 1);
        if (actual.getAltitud() >= mayorResto.getAltitud()) {
            return actual;
        } else {
            return mayorResto;
        }
    }

    // --- 6. ALTITUD PROMEDIO ---
    public double calcularAltitudPromedio() {
        int cantidad = repositorio.cantidad();
        if (cantidad == 0) {
            return 0.0;
        }
        double suma = sumarAltitudesRecursivo(0);
        return suma / cantidad;
    }

    private double sumarAltitudesRecursivo(int posicion) {
        if (posicion >= repositorio.cantidad()) {
            return 0.0;
        }
        PuntoDeInteres p = repositorio.obtener(posicion);
        return p.getAltitud() + sumarAltitudesRecursivo(posicion + 1);
    }

    // --- 7. CONTAR ACCESIBILIDAD ---
    public int contarAccesibilidadAlta() {
        return contarAccesibilidadAltaRecursivo(0);
    }

    private int contarAccesibilidadAltaRecursivo(int posicion) {
        if (posicion >= repositorio.cantidad()) {
            return 0;
        }
        PuntoDeInteres p = repositorio.obtener(posicion);
        int suma = 0;
        if (p.getNivelAccesibilidad() == 4 || p.getNivelAccesibilidad() == 5) {
            suma = 1;
        }
        return suma + contarAccesibilidadAltaRecursivo(posicion + 1);
    }

    // --- 8- DEVOLVER CANTIDAD ---
    public int cantidad() {
        return repositorio.cantidad();
    }

    public void construirIndice() {
        arbolABB = new ArbolABB<>();
        int cantidad = repositorio.cantidad();
        for (int i = 0; i < cantidad; i++) {
            PuntoDeInteres p = repositorio.obtener(i);
            if (p != null) {
                arbolABB.insertar(p);
            }
        }
        System.out.println("Índice construido correctamente.");
    }

    public void mostrarPuntosOrdenados() {
        if (arbolABB.estaVacio()) {
            System.out.println("El arbol se encuentra vacio.");
            return;
        }
        List<PuntoDeInteres> ordenados = arbolABB.obtenerOrdenados();
        for (PuntoDeInteres p : ordenados) {
            p.mostrarInformacion();
        }
    }

    public void buscarPorIndice(int codigo) {
        PuntoDeInteres referencia = new Mirador(codigo); // constructor solo con código, esto es solo para la comparacion
        PuntoDeInteres encontrado = arbolABB.buscar(referencia);
        if (encontrado != null) {
            encontrado.mostrarInformacion();
        } else {
            System.out.println("No existe un punto con el código " + codigo);
        }
    }

    public void eliminarPuntoDelIndice(int codigo) {
        PuntoDeInteres clave = new Mirador(codigo);
        if (arbolABB.buscar(clave) == null) {
            System.out.println("No existe ningun punto con el codigo " + codigo + " en el indice.");
            return;
        }
        arbolABB.eliminar(clave);
        System.out.println("Punto eliminado del indice correctamente.");
    }

    public void mostrarEstadisticasDelArbol() {
        System.out.println("----- Estadisticas del Arbol -----");
        System.out.println("Cantidad de nodos: " + arbolABB.contarNodos());
        System.out.println("Altura del arbol: " + arbolABB.altura());
        System.out.println("Cantidad de hojas: " + arbolABB.contarHojas());
        System.out.println("Cantidad de nodos internos: " + arbolABB.contarNodosInternos());
    }
}
