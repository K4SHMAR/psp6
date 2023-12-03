import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BalloonGame {
    private JFrame frame;
    private JPanel panel;
    private JButton startButton;
    private JButton stopButton;
    private JLabel hitsLabel;
    private JLabel missesLabel;

    private int numBalloons = 2;
    private Balloon[] balloons;
    private int hits = 0;
    private int misses = 0;

    public static void main(String[] args) {
        BalloonGame game = new BalloonGame();
        game.initialize();
    }

    private void initialize() {
        frame = new JFrame("Balloon Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        startButton = new JButton("Start");
        startButton.addActionListener(e -> startGame());

        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopGame());
        stopButton.setEnabled(false);

        hitsLabel = new JLabel("Hits: 0");
        missesLabel = new JLabel("Misses: 0");

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(hitsLabel);
        panel.add(missesLabel);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void startGame() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        hits = 0;
        misses = 0;
        hitsLabel.setText("Hits: 0");
        missesLabel.setText("Misses: 0");

        balloons = new Balloon[numBalloons];
        for (int i = 0; i < numBalloons; i++) {
            balloons[i] = new Balloon();
            panel.add(balloons[i]);
        }

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point clickPoint = e.getPoint();
                for (Balloon balloon : balloons) {
                    if (balloon.contains(clickPoint)) {
                        balloon.explode();
                        hits++;
                        hitsLabel.setText("Hits: " + hits);
                    }
                }
                misses++;
                missesLabel.setText("Misses: " + misses);
            }
        });

        panel.repaint();
    }

    private void stopGame() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        panel.removeMouseListener(panel.getMouseListeners()[0]);

        for (Balloon balloon : balloons) {
            panel.remove(balloon);
        }

        panel.repaint();
    }

    private class Balloon extends JComponent {
        private static final int SIZE = 30;
        private int speed;
        private int y;

        public Balloon() {
            speed = (int) (Math.random() * 5 + 1);
            y = panel.getHeight();
            setPreferredSize(new Dimension(SIZE, SIZE));
            startAnimation();
        }

        private void startAnimation() {
            new Thread(() -> {
                while (y > 0) {
                    y -= speed;
                    setLocation(getX(), y);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void explode() {
            Color color = new Color((int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256));
            setBackground(color);
            setSize(SIZE * 2, SIZE * 2);
            setLocation(getX() - SIZE / 2, y - SIZE / 2);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillOval(0, 0, SIZE, SIZE);
        }
    }
}