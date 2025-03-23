package modelo;

import java.awt.Color;

public abstract class Trazo {
    protected Color color;

    public Trazo(Color color) {
        this.color = color;
    }

    public abstract void dibujar(java.awt.Graphics g);

    public abstract boolean contiene(int x, int y);
    
    public abstract String serializar();

    public static Trazo deserializar(String data) {
        String[] partes = data.split(",");
        String tipo = partes[0];
        Color color = new Color(Integer.parseInt(partes[1]));
        
        switch (tipo) {
            case "Linea":
                return new Linea(
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[3]),
                    Integer.parseInt(partes[4]),
                    Integer.parseInt(partes[5]),
                    color
                );
            case "Rectangulo":
                return new Rectangulo(
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[3]),
                    Integer.parseInt(partes[4]),
                    Integer.parseInt(partes[5]),
                    color
                );
            case "Ovalo":
                return new Ovalo(
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[3]),
                    Integer.parseInt(partes[4]),
                    Integer.parseInt(partes[5]),
                    color
                );
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }
}


