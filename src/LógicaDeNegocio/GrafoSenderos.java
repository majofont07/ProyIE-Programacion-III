
package LógicaDeNegocio;

import LógicaDeNegocio.Entidades.PuntoDeInteres;
import LógicaDeNegocio.Entidades.Sendero;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public class GrafoSenderos {
    
    private final Graph<PuntoDeInteres, Sendero> grafo;

    public GrafoSenderos() {
        this.grafo = new SimpleGraph<>(Sendero.class);
    }

    public boolean agregarVertice(PuntoDeInteres punto) {
        return grafo.addVertex(punto);
    }

    public boolean existeVertice(PuntoDeInteres punto) {
        return grafo.containsVertex(punto);
    }

    public boolean existeSendero(PuntoDeInteres a, PuntoDeInteres b) {
        return grafo.containsEdge(a, b);
    }

    public boolean agregarSendero(PuntoDeInteres a, PuntoDeInteres b, Sendero sendero) {
        return grafo.addEdge(a, b, sendero);
    }

    public boolean eliminarSendero(PuntoDeInteres a, PuntoDeInteres b) {
        Sendero s = grafo.getEdge(a, b);
        return s != null && grafo.removeEdge(s);
    }
    public void mostrarConexiones() {
        for (PuntoDeInteres p : grafo.vertexSet()) {
            mostrarConexiones(p);
        }
    }
    
    public void mostrarConexiones(PuntoDeInteres punto) {
    System.out.println("Punto " + punto);
    if (grafo.edgesOf(punto).isEmpty()) {
        System.out.println("   (sin senderos)");
        return;
    }
    for (Sendero s : grafo.edgesOf(punto)) {
        PuntoDeInteres otro;
        if (grafo.getEdgeSource(s).equals(punto)) {
            otro = grafo.getEdgeTarget(s);
        } else {
            otro = grafo.getEdgeSource(s);
        }
        System.out.println("   -> " + otro + " | " + s);
    }
}
    
    public boolean existeCamino(PuntoDeInteres origen, PuntoDeInteres destino) {
        DepthFirstIterator<PuntoDeInteres, Sendero> it = new DepthFirstIterator<>(grafo, origen);
        while (it.hasNext()) {
            if (it.next().equals(destino)) {
                return true;
            }
        }
        return false;
    }
    
    public List<PuntoDeInteres> recorridoDFS(PuntoDeInteres inicio) {
        List<PuntoDeInteres> orden = new ArrayList<>();
        DepthFirstIterator<PuntoDeInteres, Sendero> it = new DepthFirstIterator<>(grafo, inicio);
        while (it.hasNext()) {
            orden.add(it.next());
        }
        return orden;
    }

    public List<PuntoDeInteres> recorridoBFS(PuntoDeInteres inicio) {
        List<PuntoDeInteres> orden = new ArrayList<>();
        BreadthFirstIterator<PuntoDeInteres, Sendero> it = new BreadthFirstIterator<>(grafo, inicio);
        while (it.hasNext()) {
            orden.add(it.next());
        }
        return orden;
    }

    public int contarComponentes() {
        return new ConnectivityInspector<>(grafo).connectedSets().size();
    }
}
