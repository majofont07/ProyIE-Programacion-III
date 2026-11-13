
package Datos.Persistencia;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ArchivoObjetos<T extends Serializable> implements Persistencia<T> {
    
    private final Path ruta;

    public ArchivoObjetos(String nombreArchivo) {
         if (nombreArchivo == null || nombreArchivo.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }
        this.ruta = Paths.get(nombreArchivo);
    }
    
    @Override
    public void guardar(List<T> datos) throws IOException {
        if (datos == null) {
            throw new IllegalArgumentException("La colección a guardar no puede ser null.");
        }
        // Se escribe primero en un temporal: si falla, el archivo anterior queda intacto.
        Path temporal = Paths.get(ruta.toString() + ".tmp");
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(temporal))) {
            salida.writeObject(new ArrayList<>(datos));
        } catch (IOException e) {
            Files.deleteIfExists(temporal);
            throw new IOException("No se pudo escribir el archivo " + ruta + ": " + e.getMessage(), e);
        }
        try {
            Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Files.deleteIfExists(temporal);
            throw new IOException("No se pudo reemplazar el archivo " + ruta + ": " + e.getMessage(), e);
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public List<T> recuperar() throws IOException {
        if (!Files.exists(ruta)) {
            return new ArrayList<>(); // primera ejecución: colección vacía
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ruta))) {
            return (List<T>) entrada.readObject();
        } catch (EOFException e) {
            throw new IOException("El archivo " + ruta + " está incompleto o dañado.", e);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("El archivo " + ruta + " es incompatible con esta versión del programa.", e);
        } catch (IOException e) {
            throw new IOException("No se pudo leer el archivo " + ruta + ": " + e.getMessage(), e);
        }
    }

  
}
