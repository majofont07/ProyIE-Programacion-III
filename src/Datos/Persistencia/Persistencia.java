package Datos.Persistencia;

import java.io.IOException;
import java.util.List;

public interface Persistencia<T> {

    void guardar(List<T> datos) throws IOException;
    List<T> recuperar() throws IOException;
}
