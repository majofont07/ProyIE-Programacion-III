package LógicaDeNegocio.Entidades;

import LógicaDeNegocio.Entidades.PuntoDeInteres;

public class Mirador extends PuntoDeInteres {

    //---(TIPO DATO ENUM)---
    public enum TipoVista {
        PANORAMICA,
        PAISAJE,
        FAUNA
    }

    private TipoVista tipoVista;

    //---(CONSTRUCTOR)---
    public Mirador() {
        super();
        this.tipoVista = TipoVista.PANORAMICA;
    }

    public Mirador(int codigo) {
        super(codigo);
    }

    //---(GETTERS Y SETTERS)---
    public TipoVista getTipoVista() {
        return tipoVista;
    }

    public void setTipoVista(TipoVista tipoVista) {
        this.tipoVista = tipoVista;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== MIRADOR ===");
        System.out.println("Código: " + getCodigo());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Altitud: " + getAltitud() + " m");
        System.out.println("Nivel de accesibilidad: " + getNivelAccesibilidad());
        System.out.println("Tipo de vista: " + tipoVista);
    }

    @Override
    public String obtenerTipo() {
        return "Mirador";
    }
}
