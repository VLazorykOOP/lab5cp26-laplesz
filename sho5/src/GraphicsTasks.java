import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class GraphicsTasks extends JFrame {

    public GraphicsTasks() {
        setTitle("Лабораторна: Без'є та Фрактал Коха");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Крива Без'є", new BezierPanel());
        tabbedPane.addTab("2. Сніжинка Коха", new KochPanel(4));
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GraphicsTasks().setVisible(true);
        });
    }
}

class BezierPanel extends JPanel {

    private final Point p1 = new Point(50, 300);
    private final Point p2 = new Point(150, 50);
    private final Point p3 = new Point(550, 50);
    private final Point p4 = new Point(650, 300);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        g2d.drawLine(p2.x, p2.y, p3.x, p3.y);
        g2d.drawLine(p3.x, p3.y, p4.x, p4.y);

        g2d.setColor(Color.BLACK);
        g2d.drawString("P1", p1.x, p1.y + 20);
        g2d.drawString("P2", p2.x, p2.y - 10);
        g2d.drawString("P3", p3.x, p3.y - 10);
        g2d.drawString("P4", p4.x, p4.y + 20);

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        Path2D.Double path = new Path2D.Double();
        path.moveTo(p1.x, p1.y);

        for (double t = 0; t <= 1; t += 0.001) {
            double x = Math.pow(1 - t, 3) * p1.x +
                    3 * Math.pow(1 - t, 2) * t * p2.x +
                    3 * (1 - t) * Math.pow(t, 2) * p3.x +
                    Math.pow(t, 3) * p4.x;

            double y = Math.pow(1 - t, 3) * p1.y +
                    3 * Math.pow(1 - t, 2) * t * p2.y +
                    3 * (1 - t) * Math.pow(t, 2) * p3.y +
                    Math.pow(t, 3) * p4.y;

            path.lineTo(x, y);
        }
        g2d.draw(path);
    }
}

class KochPanel extends JPanel {
    private final int depth;

    public KochPanel(int depth) {
        this.depth = depth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(1));

        Point p1 = new Point(200, 400);
        Point p2 = new Point(400, 53);
        Point p3 = new Point(600, 400);

        drawKochLine(g2d, p1, p2, depth);
        drawKochLine(g2d, p2, p3, depth);
        drawKochLine(g2d, p3, p1, depth);
    }

    private void drawKochLine(Graphics2D g, Point start, Point end, int k) {
        if (k == 0) {
            g.drawLine(start.x, start.y, end.x, end.y);
        } else {
            double dx = end.x - start.x;
            double dy = end.y - start.y;

            Point pA = new Point((int)(start.x + dx / 3), (int)(start.y + dy / 3));
            Point pB = new Point((int)(start.x + 2 * dx / 3), (int)(start.y + 2 * dy / 3));

            double angle = Math.toRadians(-60);
            double xC = pA.x + (pB.x - pA.x) * Math.cos(angle) - (pB.y - pA.y) * Math.sin(angle);
            double yC = pA.y + (pB.x - pA.x) * Math.sin(angle) + (pB.y - pA.y) * Math.cos(angle);
            Point pC = new Point((int)xC, (int)yC);

            drawKochLine(g, start, pA, k - 1);
            drawKochLine(g, pA, pC, k - 1);
            drawKochLine(g, pC, pB, k - 1);
            drawKochLine(g, pB, end, k - 1);
        }
    }
}