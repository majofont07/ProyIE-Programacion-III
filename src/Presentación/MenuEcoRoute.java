package Presentación;

import LógicaDeNegocio.Excepciones.*;
import Datos.Interfaces.RepositorioPuntosArreglo;
import LógicaDeNegocio.GestorPuntoDeInteres;
import LógicaDeNegocio.Entidades.*;
import java.util.Scanner;

public class MenuEcoRoute {

    //---(ATRIBUTOS)---
    private Scanner sc = new Scanner(System.in);
    private GestorPuntoDeInteres gestor;

    //---(CONSTRUCTOR)---
    public MenuEcoRoute() {
        this.gestor = new GestorPuntoDeInteres(new RepositorioPuntosArreglo());
    }

    //---(MÉTODOS)---
    public void mostrarMenu() {
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
            System.out.println("11. Eliminar un punto de i8ndice");
            System.out.println("12. Mostrar estadisticas del arbol");
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
                case 2 ->
                    gestor.mostrarTodos();
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
                    gestor.construirIndice();
                case 9 ->
                    gestor.mostrarPuntosOrdenados();
                case 10 ->
                    buscarPuntoPorIndice();
                case 11 ->
                    eliminarPuntoIndice();
                case 12 ->
                    gestor.mostrarEstadisticasDelArbol();
                case 0 ->
                    System.out.println("Saliendo del sistema...");
                default ->
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrarPunto() {

        System.out.println("\n--- REGISTRO DE PUNTO DE INTERES ---");
        System.out.println("Seleccione el tipo:");
        System.out.println("1. Mirador");
        System.out.println("2. Recurso Natural");
        System.out.println("3. Puesto de Servicio");
        System.out.print("Opcion: ");
        int tipo = leerEntero();

        PuntoDeInteres nuevoPunto = null;

        try {
            System.out.print("Codigo: ");
            int codigo = leerEntero();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Altitud (metros): ");
            float altitud = leerFloat();
            System.out.print("Nivel de accesibilidad (1-5): ");
            int nivel = leerEntero();

            switch (tipo) {
                case 1 -> {
                    System.out.print("Tipo de vista (PANORAMICA, PAISAJE, FAUNA): ");
                    Mirador.TipoVista vista = Mirador.TipoVista.valueOf(sc.nextLine().toUpperCase());
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
                    RecursoNatural.Categoria cat = RecursoNatural.Categoria.valueOf(sc.nextLine().toUpperCase());
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
                    PuestoDeServicio.TipoServicio serv = PuestoDeServicio.TipoServicio.valueOf(sc.nextLine().toUpperCase());
                    PuestoDeServicio puesto = new PuestoDeServicio();
                    puesto.setCodigo(codigo);
                    puesto.setNombre(nombre);
                    puesto.setAltitud(altitud);
                    puesto.setNivelAccesibilidad(nivel);
                    puesto.setTipoServicio(serv);
                    nuevoPunto = puesto;
                }
                default ->
                    System.out.println("Tipo invalido.");
            }

            if (nuevoPunto != null) {
                gestor.registrarPunto(nuevoPunto);
                System.out.println("Punto registrado con exito.");
            }

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

    private void buscarPuntoPorIndice() {
        System.out.print("Ingrese el código a buscar: ");
        int codigo = leerEntero();
        gestor.buscarPorIndice(codigo);
    }

    private void eliminarPuntoIndice() {
        System.out.println("Ingrese el código a eliminar:");
        int coidgo = leerEntero();
        gestor.eliminarPuntoDelIndice(coidgo);
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
}
