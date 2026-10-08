package LógicaDeNegocio.Entidades;

public class Sendero {

    private double distancia;  //en metros, mayor a 0
    private int dificultad; // entre 1 y 5
    private int tiempoEstimado; //minutos, mayor a 0
    private boolean habilitado;

    public Sendero(double distancia, int dificultad, int tiempoEstimado, boolean habilitado) {
        if (distancia <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }
        if (dificultad < 1 || dificultad > 5) {
            throw new IllegalArgumentException("La dificultad debe estar entre 1 y 5.");
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero.");
        }
        this.distancia = distancia;
        this.dificultad = dificultad;
        this.tiempoEstimado = tiempoEstimado;
        this.habilitado = habilitado;
    }

    public double getDistancia() {
        return distancia;
    }

    public int getDificultad() {
        return dificultad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    private void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    private void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    private void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    private void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "Sendero [distancia=" + distancia + " m, dificultad=" + dificultad
                + ", tiempo=" + tiempoEstimado + " min, "
                + (habilitado ? "habilitado" : "NO habilitado") + "]";
    }

}
