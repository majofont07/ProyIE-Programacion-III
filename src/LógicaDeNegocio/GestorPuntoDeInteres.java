package LógicaDeNegocio;

import Datos.Interfaces.RepositorioPuntoDeInteres;
import Datos.Interfaces.Excepciones.*;
import LógicaDeNegocio.Entidades.PuntoDeInteres;
import java.util.List;
import LógicaDeNegocio.Entidades.Sendero;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import Datos.Persistencia.Persistencia;

public class GestorPuntoDeInteres {

    private RepositorioPuntoDeInteres repositorio;
    private TreeMap<Integer, PuntoDeInteres> indice;
    private GrafoSenderos grafo;
    private Persistencia<PuntoDeInteres> persistenciaPuntos;
    private Persistencia<DatosConexion> persistenciaConexiones;

    public GestorPuntoDeInteres(RepositorioPuntoDeInteres repositorio, Persistencia<PuntoDeInteres> persistenciaPuntos,
            Persistencia<DatosConexion> persistenciaConexiones) {
        this.repositorio = repositorio;
        this.indice = new TreeMap<>();
        this.grafo = new GrafoSenderos();
        this.persistenciaPuntos = persistenciaPuntos;
        this.persistenciaConexiones = persistenciaConexiones;
    }

    public void validarCodigoPunto(int codigo) throws DatoInvalidoExcepcion, CodigoDuplicadoExcepcion {
        if (codigo <= 0) {
            throw new DatoInvalidoExcepcion("El código debe ser un número entero positivo.");
        }
        if (repositorio.buscarPorCodigo(codigo) != null) {
            throw new CodigoDuplicadoExcepcion("Ya existe un punto con el código " + codigo + ".");
        }
    }

    public void validarNombre(String nombre) throws DatoInvalidoExcepcion {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoExcepcion("El nombre no puede estar vacío.");
        }
    }

    public void validarAltitud(float altitud) throws DatoInvalidoExcepcion {
        if (altitud < 0) {
            throw new DatoInvalidoExcepcion("La altitud debe ser mayor o igual a cero.");
        }
    }

    public void validarNivelAccesibilidad(int nivel) throws DatoInvalidoExcepcion {
        if (nivel < 1 || nivel > 5) {
            throw new DatoInvalidoExcepcion("El nivel de accesibilidad debe estar entre 1 y 5.");
        }
    }

    public boolean repositorioLleno() {
        return repositorio.estaLleno();
    }

    public void registrarPunto(PuntoDeInteres punto) throws DatoInvalidoExcepcion, CodigoDuplicadoExcepcion, RepositorioLlenoExcepcion {
        validarCodigoPunto(punto.getCodigo());
        validarNombre(punto.getNombre());
        validarAltitud(punto.getAltitud());
        validarNivelAccesibilidad(punto.getNivelAccesibilidad());

        repositorio.agregar(punto);
        indice.put(punto.getCodigo(), punto);
        grafo.agregarVertice(punto);
    }

    // --- 2. MOSTRAR TODOS  ---
    public List<PuntoDeInteres> obtenerTodos() {
        List<PuntoDeInteres> lista = new ArrayList<>();
        obtenerTodosRecursivo(0, lista);
        return lista;
    }

    private void obtenerTodosRecursivo(int posicion, List<PuntoDeInteres> lista) {
        if (posicion >= repositorio.cantidad()) {
            return;
        }
        PuntoDeInteres p = repositorio.obtener(posicion);
        if (p != null) {
            lista.add(p);
        }
        obtenerTodosRecursivo(posicion + 1, lista);
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

    public String construirIndice() {
        if (repositorio.cantidad() == 0) {
            return "No hay puntos de interes para construir un indice.";
        }
        indice = new TreeMap<>();
        int cantidad = repositorio.cantidad();
        for (int i = 0; i < cantidad; i++) {
            PuntoDeInteres p = repositorio.obtener(i);
            if (p != null) {
                indice.put(p.getCodigo(), p);
            }
        }
        return "Indice construido correctamente con " + indice.size() + " puntos.";
    }

    public List<PuntoDeInteres> obtenerPuntosOrdenados() {
        return new ArrayList<>(indice.values());
    }

    public PuntoDeInteres buscarPorIndice(int codigo) {
        return indice.get(codigo);
    }

    public boolean eliminarPuntoDelIndice(int codigo) {
        return indice.remove(codigo) != null;
    } //devuelve true si el punto estaba en el indice y fue quitado

    public int cantidadIndexada() {
        return indice.size();
    }

    public int menorCodigoIndexado() {
        return indice.firstKey();
    }

    public int mayorCodigoIndexado() {
        return indice.lastKey();
    }
    // ================= ETAPA 3: GRAFO DE SENDEROS =================

    private PuntoDeInteres obtenerPuntoExistente(int codigo) throws DatoInvalidoExcepcion {
        PuntoDeInteres p = repositorio.buscarPorCodigo(codigo);
        if (p == null) {
            throw new DatoInvalidoExcepcion("No existe un punto de interés con el código " + codigo + ".");
        }
        return p;
    }

    public void validarPuntoExistente(int codigo) throws DatoInvalidoExcepcion {
        obtenerPuntoExistente(codigo);
    }

    public void validarExtremos(int codA, int codB) throws DatoInvalidoExcepcion {
        PuntoDeInteres a = obtenerPuntoExistente(codA);
        PuntoDeInteres b = obtenerPuntoExistente(codB);
        if (a.equals(b)) {
            throw new DatoInvalidoExcepcion("Un sendero no puede conectar un punto consigo mismo.");
        }
        if (grafo.existeSendero(a, b)) {
            throw new DatoInvalidoExcepcion("Ya existe un sendero entre esos dos puntos.");
        }
    }

    public void validarDistancia(double distancia) throws DatoInvalidoExcepcion {
        if (distancia <= 0) {
            throw new DatoInvalidoExcepcion("La distancia debe ser mayor que cero.");
        }
    }

    public void validarDificultad(int dificultad) throws DatoInvalidoExcepcion {
        if (dificultad < 1 || dificultad > 5) {
            throw new DatoInvalidoExcepcion("La dificultad debe estar entre 1 y 5.");
        }
    }

    public void validarTiempo(int tiempo) throws DatoInvalidoExcepcion {
        if (tiempo <= 0) {
            throw new DatoInvalidoExcepcion("El tiempo estimado debe ser mayor que cero.");
        }
    }

    public void registrarSendero(int codA, int codB, double distancia, int dificultad,
            int tiempo, boolean habilitado) throws DatoInvalidoExcepcion {

        validarExtremos(codA, codB);
        validarDistancia(distancia);
        validarDificultad(dificultad);
        validarTiempo(tiempo);
        grafo.agregarSendero(obtenerPuntoExistente(codA), obtenerPuntoExistente(codB),
                new Sendero(distancia, dificultad, tiempo, habilitado));
    }

    public void eliminarSendero(int codA, int codB) throws DatoInvalidoExcepcion {
        PuntoDeInteres a = obtenerPuntoExistente(codA);
        PuntoDeInteres b = obtenerPuntoExistente(codB);
        if (!grafo.eliminarSendero(a, b)) {
            throw new DatoInvalidoExcepcion("No existe un sendero entre esos dos puntos.");
        }
    }

    public Map<PuntoDeInteres, Sendero> obtenerConexionesDePunto(int codigo) throws DatoInvalidoExcepcion {
        return grafo.obtenerConexiones(obtenerPuntoExistente(codigo));
    }

    public Map<PuntoDeInteres, Map<PuntoDeInteres, Sendero>> obtenerTodasLasConexiones() {
        Map<PuntoDeInteres, Map<PuntoDeInteres, Sendero>> todas = new LinkedHashMap<>();
        for (PuntoDeInteres p : obtenerTodos()) {
            todas.put(p, grafo.obtenerConexiones(p));
        }
        return todas;
    }

    public int cantidadSenderos() {
        return grafo.obtenerSenderos().size();
    }

    public PuntoDeInteres[] obtenerExtremos(Sendero sendero) {
        return grafo.obtenerExtremos(sendero);
    }

    public boolean existeCamino(int codOrigen, int codDestino) throws DatoInvalidoExcepcion {
        return grafo.existeCamino(obtenerPuntoExistente(codOrigen), obtenerPuntoExistente(codDestino));
    }

    public List<PuntoDeInteres> recorridoDFS(int codigo) throws DatoInvalidoExcepcion {
        return grafo.recorridoDFS(obtenerPuntoExistente(codigo));
    }

    public List<PuntoDeInteres> recorridoBFS(int codigo) throws DatoInvalidoExcepcion {
        return grafo.recorridoBFS(obtenerPuntoExistente(codigo));
    }

    public int contarComponentes() {
        return grafo.contarComponentes();
    }

    public List<Sendero> obtenerSenderosOrdenadosPorDistancia() {
        // Copia de los senderos del grafo (el grafo no se modifica)
        List<Sendero> senderos = grafo.obtenerSenderos();
        quickSort(senderos, 0, senderos.size() - 1);
        return senderos;
    }

    private void quickSort(List<Sendero> lista, int inicio, int fin) {
        if (inicio < fin) {
            int posPivote = particionar(lista, inicio, fin);
            quickSort(lista, inicio, posPivote - 1);
            quickSort(lista, posPivote + 1, fin);
        }
    }

    private int particionar(List<Sendero> lista, int inicio, int fin) {
        double pivote = lista.get(fin).getDistancia();
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if (lista.get(j).getDistancia() <= pivote) {
                i++;
                intercambiar(lista, i, j);
            }
        }
        intercambiar(lista, i + 1, fin);
        return i + 1;
    }

    private void intercambiar(List<Sendero> lista, int a, int b) {
        Sendero aux = lista.get(a);
        lista.set(a, lista.get(b));
        lista.set(b, aux);
    }

    public void guardarDatos() throws IOException {
        persistenciaPuntos.guardar(obtenerTodos());

        List<DatosConexion> conexiones = new ArrayList<>();
        for (Sendero s : grafo.obtenerSenderos()) {
            PuntoDeInteres[] extremos = grafo.obtenerExtremos(s);
            conexiones.add(new DatosConexion(extremos[0].getCodigo(), extremos[1].getCodigo(),
                    s.getDistancia(), s.getDificultad(), s.getTiempoEstimado(), s.isHabilitado()));
        }
        persistenciaConexiones.guardar(conexiones);
    }

    /*
  Reconstruye repositorio, índice y grafo desde los archivos.
  Devuelve los avisos de elementos omitidos (duplicados o conexiones inválidas).
  Si algún archivo está dañado lanza IOException sin modificar el estado actual.
     */
    public List<String> recuperarDatos() throws IOException {
        // Se leen ambos archivos antes de tocar las estructuras
        List<PuntoDeInteres> puntos = persistenciaPuntos.recuperar();
        List<DatosConexion> conexiones = persistenciaConexiones.recuperar();

        List<String> avisos = new ArrayList<>();

        // Repositorio, índice y vértices del grafo (los mismos objetos en las tres estructuras)
        for (PuntoDeInteres p : puntos) {
            try {
                registrarPunto(p);
            } catch (DatoInvalidoExcepcion | CodigoDuplicadoExcepcion | RepositorioLlenoExcepcion e) {
                avisos.add("Punto omitido (código " + p.getCodigo() + "): " + e.getMessage());
            }
        }

        // Conexiones: extremos localizados por código
        for (DatosConexion c : conexiones) {
            try {
                registrarSendero(c.getCodigoOrigen(), c.getCodigoDestino(), c.getDistancia(),
                        c.getDificultad(), c.getTiempoEstimado(), c.isHabilitado());
            } catch (DatoInvalidoExcepcion e) {
                avisos.add("Sendero omitido (" + c.getCodigoOrigen() + " - "
                        + c.getCodigoDestino() + "): " + e.getMessage());
            }
        }
        return avisos;
    }
}
