
package LógicaDeNegocio;

import java.io.Serializable;

public class DatosConexion implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private final int codigoOrigen;
    private final int codigoDestino;
    private final double distancia;
    private final int dificultad;
    private final int tiempoEstimado;
    private final boolean habilitado;

    public DatosConexion(int codigoOrigen, int codigoDestino, double distancia,
            int dificultad, int tiempoEstimado, boolean habilitado) {
        this.codigoOrigen = codigoOrigen;
        this.codigoDestino = codigoDestino;
        this.distancia = distancia;
        this.dificultad = dificultad;
        this.tiempoEstimado = tiempoEstimado;
        this.habilitado = habilitado;
    }

    public int getCodigoOrigen() { return codigoOrigen; }
    public int getCodigoDestino() { return codigoDestino; }
    public double getDistancia() { return distancia; }
    public int getDificultad() { return dificultad; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public boolean isHabilitado() { return habilitado; }
}
