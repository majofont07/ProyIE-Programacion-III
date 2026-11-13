package Presentación;

import Datos.Interfaces.RepositorioPuntosArreglo;
import Datos.Interfaces.Excepciones.*;
import LógicaDeNegocio.GestorPuntoDeInteres;
import LógicaDeNegocio.Entidades.*;
import java.util.List;
import java.util.Scanner;
import Datos.Persistencia.ArchivoObjetos;
import Datos.Persistencia.Persistencia;
import LógicaDeNegocio.DatosConexion;
import java.io.IOException;
import java.util.Map;

public class MenuEcoRoute {

    //---(ATRIBUTOS)---
    private Scanner sc = new Scanner(System.in);
    private GestorPuntoDeInteres gestor;

    //---(CONSTRUCTOR)---
    public MenuEcoRoute() {
        Persistencia<PuntoDeInteres> archivoPuntos = new ArchivoObjetos<>("PUNTOS.DAT");
        Persistencia<DatosConexion> archivoConexiones = new ArchivoObjetos<>("SENDEROS.DAT");
        this.gestor = new GestorPuntoDeInteres(new RepositorioPuntosArreglo(), archivoPuntos, archivoConexiones);
    }

    //---(MÉTODOS)---
    public void mostrarMenu() {
        recuperarDatosAlIniciar();
        int opcion = 0;
        do {
            System.out.println("\n=== ECOROUTE - MENU PRINCIPAL ===");
            System.out.println("1. Registrar un punto de interes");
            System.out.println("2. Mostrar todos los puntos de interes");
            System.out.println("3. Buscar un punto de interes por codigo");
            System.out.println("4. Contar puntos de interes por tipo");
            System.out.println("5. Determinar el punto de mayor altitud");
            System.out.println("6. Calcular la altitud promedio");
            System.out.println("7. Contar puntos con accesibilidad alta");
            //opciones de la parte 1 de la etapa 2
            System.out.println("8. Construir el indice");
            System.out.println("9. Mostrar los puntos ordenados");
            System.out.println("10. Buscar un punto utilizando el indice");
            System.out.println("11. Eliminar un punto del indice");
            System.out.println("12. Mostrar cantidad de puntos indexados");
            System.out.println("13. Registrar un sendero");
            System.out.println("14. Eliminar un sendero");
            System.out.println("15. Mostrar todos los senderos");
            System.out.println("16. Mostrar las conexiones de un punto");
            System.out.println("17. Verificar si existe camino entre dos puntos");
            System.out.println("18. Recorrido en profundidad (DFS)");
            System.out.println("19. Recorrido en amplitud (BFS)");
            System.out.println("20. Determinar sectores aislados");
            System.out.println("21. Mostrar senderos ordenados por distancia");
            System.out.println("22. Guardar datos en archivos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
                continue; // Vuelve a mostrar el menú sin incrementar el bucle
            }

            switch (opcion) {
                case 1 ->
                    registrarPunto();
                case 2 -> {
                    List<PuntoDeInteres> todos = gestor.obtenerTodos();
                    if (todos.isEmpty()) {
                        System.out.println("No hay puntos.");
                    } else {
                        todos.forEach(p -> {
                            p.mostrarInformacion();
                            System.out.println("------");
                        });
                    }
                }
                case 3 ->
                    buscarPorCodigo();
                case 4 ->
                    contarPorTipo();
                case 5 ->
                    mostrarMayorAltitud();
                case 6 ->
                    mostrarAltitudPromedio();
                case 7 ->
                    mostrarAccesibilidadAlta();
                case 8 ->
                    System.out.println(gestor.construirIndice());
                case 9 ->
                    mostrarPuntosOrdenados();
                case 10 ->
                    buscarPuntoPorIndice();
                case 11 ->
                    eliminarPuntoDelIndice();
                case 12 ->
                    mostrarCantidadIndexada();
                case 13 ->
                    registrarSendero();
                case 14 ->
                    eliminarSendero();
                case 15 ->
                    mostrarTodosLosSenderos();
                case 16 ->
                    mostrarConexionesDePunto();
                case 17 ->
                    verificarCamino();
                case 18 ->
                    recorrerDFS();
                case 19 ->
                    recorrerBFS();
                case 20 ->
                    System.out.println("Cantidad de sectores (componentes conexas): " + gestor.contarComponentes());
                case 21 ->
                    mostrarSenderosOrdenadosPorDistancia();
                case 22 ->
                    guardarDatos();
                case 0 ->
                    System.out.println("Saliendo del sistema...");
                default ->
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrarPunto() {

        System.out.println("\n--- REGISTRO DE PUNTO DE INTERES ---");
        if (gestor.repositorioLleno()) {
            System.out.println("Error: el repositorio está lleno, no se pueden registrar más puntos.");
            return;
        }
        System.out.println("Seleccione el tipo:");
        System.out.println("1. Mirador");
        System.out.println("2. Recurso Natural");
        System.out.println("3. Puesto de Servicio");
        System.out.print("Opcion: ");
        int tipo = leerEntero();
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo invalido.");
            return;
        }

        PuntoDeInteres nuevoPunto = null;

        try {
            // Cada dato se valida apenas se ingresa
            System.out.print("Codigo: ");
            int codigo = leerEntero();
            gestor.validarCodigoPunto(codigo);
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            gestor.validarNombre(nombre);
            System.out.print("Altitud (metros): ");
            float altitud = leerFloat();
            gestor.validarAltitud(altitud);
            System.out.print("Nivel de accesibilidad (1-5): ");
            int nivel = leerEntero();
            gestor.validarNivelAccesibilidad(nivel);

            switch (tipo) {
                case 1 -> {
                    System.out.print("Tipo de vista (PANORAMICA, PAISAJE, FAUNA): ");
                    Mirador.TipoVista vista = Mirador.TipoVista.valueOf(sc.nextLine().trim().toUpperCase());
                    Mirador mirador = new Mirador();
                    mirador.setCodigo(codigo);
                    mirador.setNombre(nombre);
                    mirador.setAltitud(altitud);
                    mirador.setNivelAccesibilidad(nivel);
                    mirador.setTipoVista(vista);
                    nuevoPunto = mirador;
                }
                case 2 -> {
                    System.out.print("Categoría (CASCADA, LAGUNA, BOSQUE, FORMACION_ROCOSA): ");
                    RecursoNatural.Categoria cat = RecursoNatural.Categoria.valueOf(sc.nextLine().trim().toUpperCase());
                    RecursoNatural recurso = new RecursoNatural();
                    recurso.setCodigo(codigo);
                    recurso.setNombre(nombre);
                    recurso.setAltitud(altitud);
                    recurso.setNivelAccesibilidad(nivel);
                    recurso.setCategoria(cat);
                    nuevoPunto = recurso;
                }
                case 3 -> {
                    System.out.print("Tipo de servicio (GUARDA_PARQUES, PRIMEROS_AUXILIOS, INFORMACION_VISITANTE): ");
                    PuestoDeServicio.TipoServicio serv = PuestoDeServicio.TipoServicio.valueOf(sc.nextLine().trim().toUpperCase());
                    PuestoDeServicio puesto = new PuestoDeServicio();
                    puesto.setCodigo(codigo);
                    puesto.setNombre(nombre);
                    puesto.setAltitud(altitud);
                    puesto.setNivelAccesibilidad(nivel);
                    puesto.setTipoServicio(serv);
                    nuevoPunto = puesto;
                }
            }

            gestor.registrarPunto(nuevoPunto);
            System.out.println("Punto registrado con exito.");

        } catch (DatoInvalidoExcepcion | CodigoDuplicadoExcepcion | RepositorioLlenoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Valor ingresado no valido para el tipo seleccionado.");
        }
    }

    private void buscarPorCodigo() {

        System.out.print("Ingrese el codigo a buscar: ");
        int codigo = leerEntero();
        PuntoDeInteres p = gestor.buscarPorCodigo(codigo);
        if (p != null) {
            p.mostrarInformacion();
        } else {
            System.out.println("No se encontro un punto de interes con el codigo ingresado.");
        }
    }

    private void contarPorTipo() {

        System.out.println("Seleccione el tipo a contar:");
        System.out.println("1. Mirador");
        System.out.println("2. RecursoNatural");
        System.out.println("3. PuestoServicio");
        System.out.print("Opcion: ");
        int op = leerEntero();
        String tipo = switch (op) {
            case 1 ->
                "Mirador";
            case 2 ->
                "RecursoNatural";
            case 3 ->
                "PuestoServicio";
            default ->
                "";
        };
        if (tipo.isEmpty()) {
            System.out.println("Opcion inválida.");
            return;
        }
        int cantidad = gestor.contarPorTipo(tipo);
        System.out.println("Cantidad de puntos de tipo " + tipo + ": " + cantidad);
    }

    private void mostrarMayorAltitud() {
        PuntoDeInteres p = gestor.determinarMayorAltitud();
        if (p == null) {
            System.out.println("No hay puntos registrados.");
        } else {
            System.out.println("El punto con mayor altitud es:");
            p.mostrarInformacion();
        }
    }

    private void mostrarAltitudPromedio() {
        if (gestor.cantidad() == 0) {
            System.out.println("No hay puntos registrados para calcular el promedio.");
            return;
        }
        double promedio = gestor.calcularAltitudPromedio();
        System.out.printf("La altitud promedio es: %.2f metros", promedio);
    }

    private void mostrarAccesibilidadAlta() {
        int cantidad = gestor.contarAccesibilidadAlta();
        System.out.println("Cantidad de puntos con accesibilidad alta (nivel 4 o 5): " + cantidad);
    }

    private void mostrarPuntosOrdenados() {
        List<PuntoDeInteres> ordenados = gestor.obtenerPuntosOrdenados();
        if (ordenados.isEmpty()) {
            System.out.println("El indice se encuentra vacio.");
            return;
        }
        for (PuntoDeInteres p : ordenados) {
            p.mostrarInformacion();
            System.out.println("-------------------------");
        }
    }

    public void buscarPuntoPorIndice() {
        if (gestor.cantidadIndexada() == 0) {
            System.out.println("El indice esta vacio. Construya el indice primero.");
            return;
        }
        System.out.print("Ingrese el código a buscar: ");
        int codigo = leerEntero();
        PuntoDeInteres p = gestor.buscarPorIndice(codigo);
        if (p != null) {
            p.mostrarInformacion();
        } else {
            System.out.println("No existe un punto con el código " + codigo);
        }
    }

    private void eliminarPuntoDelIndice() {
        if (gestor.cantidadIndexada() == 0) {
            System.out.println("El indice esta vacio. No hay puntos para eliminar.");
            return;
        }
        System.out.print("Ingrese el código a eliminar del índice: ");
        int codigo = leerEntero();
        if (gestor.eliminarPuntoDelIndice(codigo)) {
            System.out.println("Punto eliminado del indice correctamente.");
        } else {
            System.out.println("No existe ningun punto con el codigo " + codigo + " en el indice.");
        }
    }

    private void mostrarCantidadIndexada() {
        System.out.println("----- Estado del Indice -----");
        int cantidad = gestor.cantidadIndexada();
        System.out.println("Cantidad de puntos indexados: " + cantidad);
        if (cantidad > 0) {
            System.out.println("Menor codigo indexado: " + gestor.menorCodigoIndexado());
            System.out.println("Mayor codigo indexado: " + gestor.mayorCodigoIndexado());
        }
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Debe ingresar un numero entero. Intente nuevamente: ");
            }
        }
    }

    private float leerFloat() {
        while (true) {
            try {
                return Float.parseFloat(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Debe ingresar un numero válido. Intente nuevamente: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Debe ingresar un numero válido. Intente nuevamente: ");
            }
        }
    }

// Acepta S / SI / SÍ / N / NO (mayúsculas o minúsculas) y repregunta si no es válido
    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = sc.nextLine().trim().toLowerCase().replace("í", "i");
            if (r.equals("s") || r.equals("si")) {
                return true;
            }
            if (r.equals("n") || r.equals("no")) {
                return false;
            }
            System.out.println("Respuesta no válida. Ingrese S o N.");
        }
    }

    private void registrarSendero() {
        try {
            // Cada dato se valida apenas se ingresa
            System.out.print("Código del primer punto: ");
            int a = leerEntero();
            gestor.validarPuntoExistente(a);
            System.out.print("Código del segundo punto: ");
            int b = leerEntero();
            gestor.validarExtremos(a, b); // existen, no son el mismo y no hay sendero previo
            System.out.print("Distancia (metros): ");
            double distancia = leerDouble();
            gestor.validarDistancia(distancia);
            System.out.print("Dificultad (1-5): ");
            int dificultad = leerEntero();
            gestor.validarDificultad(dificultad);
            System.out.print("Tiempo estimado (minutos): ");
            int tiempo = leerEntero();
            gestor.validarTiempo(tiempo);
            boolean habilitado = leerSiNo("¿Habilitado? (S/N): ");

            gestor.registrarSendero(a, b, distancia, dificultad, tiempo, habilitado);
            System.out.println("Sendero registrado con éxito.");
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarTodosLosSenderos() {
        Map<PuntoDeInteres, Map<PuntoDeInteres, Sendero>> todas = gestor.obtenerTodasLasConexiones();
        if (todas.isEmpty()) {
            System.out.println("No hay puntos de interés registrados.");
            return;
        }
        for (Map.Entry<PuntoDeInteres, Map<PuntoDeInteres, Sendero>> e : todas.entrySet()) {
            imprimirConexiones(e.getKey(), e.getValue());
        }
    }

    private void imprimirConexiones(PuntoDeInteres punto, Map<PuntoDeInteres, Sendero> conexiones) {
        System.out.println("Punto " + punto);
        if (conexiones.isEmpty()) {
            System.out.println("   (sin senderos)");
            return;
        }
        for (Map.Entry<PuntoDeInteres, Sendero> c : conexiones.entrySet()) {
            System.out.println("   -> " + c.getKey() + " | " + c.getValue());
        }
    }

    private void mostrarConexionesDePunto() {
        System.out.print("Código del punto: ");
        int codigo = leerEntero();
        try {
            Map<PuntoDeInteres, Sendero> conexiones = gestor.obtenerConexionesDePunto(codigo);
            imprimirConexiones(gestor.buscarPorCodigo(codigo), conexiones);
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarSenderosOrdenadosPorDistancia() {
        List<Sendero> senderos = gestor.obtenerSenderosOrdenadosPorDistancia();
        if (senderos.isEmpty()) {
            System.out.println("No hay senderos registrados.");
            return;
        }
        System.out.println("----- Senderos ordenados por distancia (menor a mayor) -----");
        int n = 1;
        for (Sendero s : senderos) {
            PuntoDeInteres[] extremos = gestor.obtenerExtremos(s);
            System.out.println(n + ". " + extremos[0].getCodigo() + " " + extremos[0].getNombre()
                    + " <-> " + extremos[1].getCodigo() + " " + extremos[1].getNombre());
            System.out.println("   Distancia: " + s.getDistancia() + " m | Dificultad: " + s.getDificultad()
                    + " | Tiempo estimado: " + s.getTiempoEstimado() + " min");
            n++;
        }
    }

    private void eliminarSendero() {
        System.out.print("Código del primer punto: ");
        int a = leerEntero();
        System.out.print("Código del segundo punto: ");
        int b = leerEntero();
        try {
            gestor.eliminarSendero(a, b);
            System.out.println("Sendero eliminado.");
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verificarCamino() {
        System.out.print("Código de origen: ");
        int origen = leerEntero();
        System.out.print("Código de destino: ");
        int destino = leerEntero();
        try {
            boolean existe = gestor.existeCamino(origen, destino);
            System.out.println(existe ? "Sí existe un camino entre los puntos."
                    : "No existe un camino entre los puntos.");
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void recorrerDFS() {
        System.out.print("Código del punto inicial: ");
        int codigo = leerEntero();
        try {
            System.out.println("Recorrido DFS:");
            for (PuntoDeInteres p : gestor.recorridoDFS(codigo)) {
                System.out.println("  " + p);
            }
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void recorrerBFS() {
        System.out.print("Código del punto inicial: ");
        int codigo = leerEntero();
        try {
            System.out.println("Recorrido BFS:");
            for (PuntoDeInteres p : gestor.recorridoBFS(codigo)) {
                System.out.println("  " + p);
            }
        } catch (DatoInvalidoExcepcion e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void recuperarDatosAlIniciar() {
        try {
            List<String> avisos = gestor.recuperarDatos();
            if (gestor.cantidad() == 0 && gestor.cantidadSenderos() == 0 && avisos.isEmpty()) {
                System.out.println("No hay datos guardados. Se inicia con las estructuras vacías.");
            } else {
                System.out.println("Datos recuperados: " + gestor.cantidad() + " puntos y "
                        + gestor.cantidadSenderos() + " senderos.");
            }
            for (String aviso : avisos) {
                System.out.println("Aviso: " + aviso);
            }
        } catch (IOException e) {
            System.out.println("Error al recuperar los datos: " + e.getMessage());
            System.out.println("Se inicia con las estructuras vacías. Los archivos no se modifican hasta que use la opción 22.");
        }
    }

    private void guardarDatos() {
        try {
            gestor.guardarDatos();
            System.out.println("Datos guardados en PUNTOS.DAT (" + gestor.cantidad() + " puntos) y SENDEROS.DAT ("
                    + gestor.cantidadSenderos() + " senderos).");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }
}
