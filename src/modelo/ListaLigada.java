package modelo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListaLigada {
    private Nodo cabeza;

    public void agregar(Trazo trazo) {
        Nodo nuevo = new Nodo(trazo);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    public void eliminar(int indice) {
        if (indice < 0 || cabeza == null) return;

        if (indice == 0) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo actual = cabeza;
        for (int i = 0; actual != null && i < indice - 1; i++) {
            actual = actual.siguiente;
        }

        if (actual == null || actual.siguiente == null) return;
        actual.siguiente = actual.siguiente.siguiente;
    }

    public List<Trazo> obtenerTodos() {
        List<Trazo> trazos = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            trazos.add(actual.trazo);
            actual = actual.siguiente;
        }
        return trazos;
    }

    public void guardar(String archivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            Nodo actual = cabeza;
            while (actual != null) {
                writer.println(actual.trazo.serializar());
                actual = actual.siguiente;
            }
        }
    }

    public void cargar(String archivo) throws IOException {
        cabeza = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                agregar(Trazo.deserializar(linea));
            }
        }
    }
}
