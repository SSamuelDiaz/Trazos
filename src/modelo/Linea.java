package modelo;

import java.awt.Color;
import java.awt.Graphics;

public class Linea extends Trazo {
    private int x1, y1, x2, y2;

    public Linea(int x1, int y1, int x2, int y2, Color color) {
        super(color);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public boolean contiene(int x, int y) {
        double distancia = Math.hypot(x2 - x1, y2 - y1);
        double distancia1 = Math.hypot(x - x1, y - y1);
        double distancia2 = Math.hypot(x - x2, y - y2);
        return Math.abs(distancia - (distancia1 + distancia2)) < 1.0;
    }

    @Override
    public String serializar() {
        return String.format("Linea,%d,%d,%d,%d,%d",
            color.getRGB(), x1, y1, x2, y2);
    }
}





