package modelo;

import java.awt.*;

public class Ovalo extends Trazo {
    private int x, y, ancho, alto;

    public Ovalo(int x, int y, int ancho, int alto, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawOval(x, y, ancho, alto);
    }

    @Override
    public boolean contiene(int x, int y) {
        double dx = x - (this.x + ancho / 2.0);
        double dy = y - (this.y + alto / 2.0);
        return (dx * dx) / (ancho * ancho / 4.0) + (dy * dy) / (alto * alto / 4.0) <= 1.0;
    }

    @Override
    public String serializar() {
        return String.format("Ovalo,%d,%d,%d,%d,%d",
            color.getRGB(), x, y, ancho, alto);
}

}


