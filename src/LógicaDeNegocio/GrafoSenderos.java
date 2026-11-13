
package LógicaDeNegocio;

import LógicaDeNegocio.Entidades.PuntoDeInteres;
import LógicaDeNegocio.Entidades.Sendero;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<PuntoDeInteres, Sendero> obtenerConexiones(PuntoDeInteres punto) {
    Map<PuntoDeInteres, Sendero> conexiones = new LinkedHashMap<>();
    for (Sendero s : grafo.edgesOf(punto)) {
        PuntoDeInteres otro = grafo.getEdgeSource(s).equals(punto)
                ? grafo.getEdgeTarget(s)
                : grafo.getEdgeSource(s);
        conexiones.put(otro, s);
    }
    return conexiones;
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
    public List<Sendero> obtenerSenderos() {
    return new ArrayList<>(grafo.edgeSet());
}

public PuntoDeInteres[] obtenerExtremos(Sendero sendero) {
    return new PuntoDeInteres[]{grafo.getEdgeSource(sendero), grafo.getEdgeTarget(sendero)};
}
}
