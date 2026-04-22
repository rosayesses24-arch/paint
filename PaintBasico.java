package paint;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class PaintBasico extends JFrame {

    Color colorActual = Color.BLACK;
    String herramienta = "PINCEL";
    int grosor = 5;

    Lienzo lienzo;

    public PaintBasico() {
        setTitle("Mini Paint");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        lienzo = new Lienzo();
        add(lienzo, BorderLayout.CENTER);

        JPanel herramientas = new JPanel();

        JButton pincel = new JButton("Pincel");
        JButton borrador = new JButton("Borrador");
        JButton linea = new JButton("Línea");
        JButton rect = new JButton("Rectángulo");
        JButton circulo = new JButton("Círculo");
        JButton limpiar = new JButton("Limpiar");

        herramientas.add(pincel);
        herramientas.add(borrador);
        herramientas.add(linea);
        herramientas.add(rect);
        herramientas.add(circulo);
        herramientas.add(limpiar);

        add(herramientas, BorderLayout.NORTH);

        JSlider slider = new JSlider(1, 20, 5);
        herramientas.add(new JLabel("Grosor"));
        herramientas.add(slider);

        JPanel colores = new JPanel();

        Color[] listaColores = {
            Color.WHITE, Color.BLACK, Color.GRAY,
            Color.BLUE, Color.RED, Color.GREEN
        };

        for (Color c : listaColores) {
            JButton btnColor = new JButton();
            btnColor.setBackground(c);
            btnColor.setPreferredSize(new Dimension(40,40));

            btnColor.addActionListener(e -> {
                colorActual = c;
            });

            colores.add(btnColor);
        }

        add(colores, BorderLayout.SOUTH);


        pincel.addActionListener(e -> herramienta = "PINCEL");
        borrador.addActionListener(e -> herramienta = "BORRADOR");
        linea.addActionListener(e -> herramienta = "LINEA");
        rect.addActionListener(e -> herramienta = "RECT");
        circulo.addActionListener(e -> herramienta = "CIRCULO");

        limpiar.addActionListener(e -> lienzo.limpiar());

        slider.addChangeListener(e -> {
            grosor = slider.getValue();
        });

        setVisible(true);
    }

    class Lienzo extends JPanel {

        Image imagen;
        Graphics2D g2;

        int x1, y1, x2, y2;

        public Lienzo() {
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    x1 = e.getX();
                    y1 = e.getY();
                    if (herramienta.equals("PINCEL") || herramienta.equals("BORRADOR")) {
                        g2.setStroke(new BasicStroke(grosor));

                        if (herramienta.equals("BORRADOR")) {
                            g2.setColor(Color.WHITE);
                        } else {
                            g2.setColor(colorActual);
                        }

                        g2.drawLine(x1, y1, x1, y1);
                        repaint();
                }
                    
             }

                public void mouseReleased(MouseEvent e) {
                    x2 = e.getX();
                    y2 = e.getY();

                    dibujarForma();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {

                public void mouseDragged(MouseEvent e) {

                    if (herramienta.equals("PINCEL") || herramienta.equals("BORRADOR")) {
                        x2 = e.getX();
                        y2 = e.getY();

                        g2.setStroke(new BasicStroke(grosor));

                        if (herramienta.equals("BORRADOR")) {
                            g2.setColor(Color.WHITE);
                        } else {
                            g2.setColor(colorActual);
                        }

                        g2.drawLine(x1, y1, x2, y2);

                        x1 = x2;
                        y1 = y2;

                        repaint();
                    }
                }
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (imagen == null) {
                imagen = createImage(getWidth(), getHeight());
                g2 = (Graphics2D) imagen.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                limpiar();
            }

            g.drawImage(imagen, 0, 0, null);
        }

        public void dibujarForma() {
            g2.setStroke(new BasicStroke(grosor));
            g2.setColor(colorActual);

            int w = Math.abs(x2 - x1);
            int h = Math.abs(y2 - y1);

            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);

            switch (herramienta) {
                case "LINEA":
                    g2.drawLine(x1, y1, x2, y2);
                    break;
                case "RECT":
                    g2.drawRect(x, y, w, h);
                    break;
                case "CIRCULO":
                    g2.drawOval(x, y, w, h);
                    break;
            }

            repaint();
        }

        public void limpiar() {
            if (g2 != null) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                repaint();
            }
        }
    }

   
}


