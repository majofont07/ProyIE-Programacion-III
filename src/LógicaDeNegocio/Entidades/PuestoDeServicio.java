
package LógicaDeNegocio.Entidades;

import LógicaDeNegocio.Entidades.PuntoDeInteres;

public class PuestoDeServicio extends PuntoDeInteres{

    //---(TIPO DATO ENUM)---
    public enum TipoServicio{
        GUARDA_PARQUES,
        PRIMEROS_AUXILIOS,
        INFORMACION_VISITANTE
    
     }
    
    private TipoServicio tipoServicio;
    
    //---(CONSTRUCTOR)---
    public PuestoDeServicio() {
        super();
        this.tipoServicio = tipoServicio.GUARDA_PARQUES;
    }
    
    //---(GETTERS Y SETTERS)---
     public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    @Override
    public void mostrarInformacion() {
        System.out.println("=== PUESTO DE SERVICIO ===");
        System.out.println("Código: " + getCodigo());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Altitud: " + getAltitud() + " m");
        System.out.println("Nivel de accesibilidad: " + getNivelAccesibilidad());
        System.out.println("Tipo de servicio: " + tipoServicio);
    }
     @Override
    public String obtenerTipo() {
        return "PuestoServicio";
    }
}
