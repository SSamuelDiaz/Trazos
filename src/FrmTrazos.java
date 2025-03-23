import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FrmTrazos extends JFrame {
    private JPanel pnlDibujo;
    private JComboBox<String> cmbTipoTrazo;
    private JList<String> lstTrazos;
    private DefaultListModel<String> listModel;
    private ListaLigada listaTrazos;
    private int xIni, yIni;
    private boolean trazando, modoBorrador;
    private Trazo trazoTemporal;

    public FrmTrazos() {
        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setTitle("Dibujar Trazos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        pnlDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);
                dibujarTrazos(g);
            }
        };

        cmbTipoTrazo = new JComboBox<>(new String[]{"Línea", "Rectángulo", "Óvalo"});
        listModel = new DefaultListModel<>();
        lstTrazos = new JList<>(listModel);
        listaTrazos = new ListaLigada();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargar = new JButton("Cargar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnSalirBorrador = new JButton("Salir de Borrador");

        JPanel pnlHerramientas = new JPanel();
        pnlHerramientas.setLayout(new FlowLayout());
        pnlHerramientas.add(new JLabel("Tipo de Trazo:"));
        pnlHerramientas.add(cmbTipoTrazo);
        pnlHerramientas.add(btnGuardar);
        pnlHerramientas.add(btnCargar);
        pnlHerramientas.add(btnBorrar);
        pnlHerramientas.add(btnSalirBorrador);

        add(pnlHerramientas, BorderLayout.NORTH);
        add(pnlDibujo, BorderLayout.CENTER);
        add(new JScrollPane(lstTrazos), BorderLayout.EAST);
        
        btnGuardar.addActionListener(e -> guardarTrazos());
        btnCargar.addActionListener(e -> cargarTrazos());
        btnBorrar.addActionListener(e -> activarModoBorrador());
        btnSalirBorrador.addActionListener(e -> desactivarModoBorrador());
    }

    private void configurarEventos() {
        pnlDibujo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modoBorrador) {
                    eliminarTrazo(e.getX(), e.getY());
                } else {
                    xIni = e.getX();
                    yIni = e.getY();
                    trazando = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!modoBorrador && trazando) {
                    int xFin = e.getX();
                    int yFin = e.getY();
                    agregarTrazo(xIni, yIni, xFin, yFin);
                    trazando = false;
                    trazoTemporal = null;
                    pnlDibujo.repaint();
                    listModel.addElement("Trazo " + listModel.size() + ": " + cmbTipoTrazo.getSelectedItem());
                }
            }
        });

        pnlDibujo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!modoBorrador && trazando) {
                    int xFin = e.getX();
                    int yFin = e.getY();
                    
                    trazoTemporal = traerTrazoTemporal(xIni, yIni, xFin, yFin);
                    pnlDibujo.repaint();
                }
            }
        });
    }

    private void agregarTrazo(int x1, int y1, int x2, int y2) {
        Color color = Color.WHITE;
        Trazo trazo = null;

        switch (cmbTipoTrazo.getSelectedIndex()) {
            case 0:
                trazo = new Linea(x1, y1, x2, y2, color);
                break;
            case 1:
                trazo = new Rectangulo(
                    Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1), color);
                break;
            case 2:
                trazo = new Ovalo(
                    Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1), color);
                break;
        }

        if (trazo != null) {
            listaTrazos.agregar(trazo);
        }
    }

    private Trazo traerTrazoTemporal(int x1, int y1, int x2, int y2) {
        Color color = Color.LIGHT_GRAY;
        switch (cmbTipoTrazo.getSelectedIndex()) {
            case 0:
                return new Linea(x1, y1, x2, y2, color);
            case 1:
                return new Rectangulo(
                    Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1), color);
            case 2:
                return new Ovalo(
                    Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1), color);
            default:
                return null;
        }
    }

    private void dibujarTrazos(Graphics g) {
        for (Trazo trazo : listaTrazos.obtenerTodos()) {
            trazo.dibujar(g);
        }
        
        if (trazoTemporal != null) {
            trazoTemporal.dibujar(g);
        }
    }

    private void guardarTrazos() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                String archivo = fileChooser.getSelectedFile().getAbsolutePath();
                listaTrazos.guardar(archivo);
                JOptionPane.showMessageDialog(this, "Trazos guardados con éxito.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar los trazos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarTrazos() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                String archivo = fileChooser.getSelectedFile().getAbsolutePath();
                listaTrazos.cargar(archivo);
                listModel.clear();
                for (int i = 0; i < listaTrazos.obtenerTodos().size(); i++) {
                    listModel.addElement("Trazo " + i + ": " + cmbTipoTrazo.getItemAt(0));
                }
                pnlDibujo.repaint();
                JOptionPane.showMessageDialog(this, "Trazos cargados con éxito.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar los trazos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void activarModoBorrador() {
        modoBorrador = true;
        JOptionPane.showMessageDialog(this, "Modo borrador activado. Haz clic en un trazo para eliminarlo.");
    }

    private void desactivarModoBorrador() {
        modoBorrador = false;
        JOptionPane.showMessageDialog(this, "Modo borrador desactivado.");
    }

    private void eliminarTrazo(int x, int y) {
        for (int i = 0; i < listaTrazos.obtenerTodos().size(); i++) {
            Trazo trazo = listaTrazos.obtenerTodos().get(i);
            if (trazo.contiene(x, y)) {
                listaTrazos.eliminar(i);
                listModel.remove(i);
                pnlDibujo.repaint();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No se encontró un trazo en la posición seleccionada.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmTrazos frame = new FrmTrazos();
            frame.setVisible(true);
        });
    }
}


