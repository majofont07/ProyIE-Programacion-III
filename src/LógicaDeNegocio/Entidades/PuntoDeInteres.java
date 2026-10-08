package LógicaDeNegocio.Entidades;

public abstract class PuntoDeInteres implements Comparable<PuntoDeInteres> {

    //---(ATRIBUTOS)---
    private int codigo; // debe ser unico en el sistema  
    private String nombre; // denomionacion del lugar
    private float altitud; // expresada enmetros
    private int nivelAccesibilidad; // entre 1 y 5

    /*1: Acceso muy difícil 
    2: Acceso dificil 
    3: Acceso moderado 
    4: Acceso fácil 
    5: Acceso muy fácil */
    //---(CONSTRUCTOR)---
    public PuntoDeInteres() {
        this.codigo = 0;
        this.nombre = "";
        this.altitud = 0;
        this.nivelAccesibilidad = 0;
    }

    public PuntoDeInteres(int codigo) {
        this.codigo = codigo;
        // el resto de los atributos queda en su valor por defecto (null, 0, 0.0, etc.)
    }

    //---(GETTERS Y STTERS)---
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un número entero positivo.");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public float getAltitud() {
        return altitud;
    }

    public void setAltitud(float altitud) {
        if (altitud < 0) {
            throw new IllegalArgumentException("La altitud debe ser mayor o igual a cero.");
        }
        this.altitud = altitud;
    }

    public int getNivelAccesibilidad() {
        return nivelAccesibilidad;
    }

    public void setNivelAccesibilidad(int nivelAccesibilidad) {
        if (nivelAccesibilidad < 1 || nivelAccesibilidad > 5) {
            throw new IllegalArgumentException("El nivel de accesibilidad debe estar entre 1 y 5.");
        }
        this.nivelAccesibilidad = nivelAccesibilidad;
    }

    //---(MÉTODOS ABS)---
    public abstract void mostrarInformacion();

    public abstract String obtenerTipo();

    @Override
    public int compareTo(PuntoDeInteres otro) {
        return Integer.compare(this.codigo, otro.codigo);
    }
}
