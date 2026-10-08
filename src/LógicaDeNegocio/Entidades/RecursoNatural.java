package LógicaDeNegocio.Entidades;

import LógicaDeNegocio.Entidades.PuntoDeInteres;

public class RecursoNatural extends PuntoDeInteres {

    //---(TIPO DATO ENUM)---
    public enum Categoria {
        CASCADA,
        LAGUNA,
        BOSQUE,
        FORMACION_ROCOSA
    }

    private Categoria categoria;

    //---(CONSTRUCTOR)---
    public RecursoNatural() {
        super();
        this.categoria = categoria.CASCADA;
    }

    //---(GETTERS Y SETTERS)---
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== RECURSO NATURAL ===");
        System.out.println("Código: " + getCodigo());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Altitud: " + getAltitud() + " m");
        System.out.println("Nivel de accesibilidad: " + getNivelAccesibilidad());
        System.out.println("Categoría: " + categoria);
    }

    @Override
    public String obtenerTipo() {
        return "RecursoNatural";
    }
}
