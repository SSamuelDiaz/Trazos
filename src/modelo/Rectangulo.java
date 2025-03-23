package modelo;

import java.awt.*;

public class Rectangulo extends Trazo {
    private int x, y, ancho, alto;

    public Rectangulo(int x, int y, int ancho, int alto, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawRect(x, y, ancho, alto);
    }

    @Override
    public boolean contiene(int x, int y) {
        return x >= this.x && x <= this.x + ancho && y >= this.y && y <= this.y + alto;
    }
    @Override
    public String serializar() {
        return String.format("Rectangulo,%d,%d,%d,%d,%d",
            color.getRGB(), x, y, ancho, alto);
}

}

