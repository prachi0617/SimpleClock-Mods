// package SimpleClock;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SimpleClock extends JFrame {

    private JLabel titleLabel;
    private JLabel timeLabel;
    private JLabel dayLabel;
    private JLabel dateLabel;
    private JLabel zoneLabel;

    private JButton formatButton;
    private JButton zoneButton;

    private boolean is24HourFormat = false;
    private boolean isGMT = false;

    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dayFormat;
    private SimpleDateFormat dateFormat;

    public SimpleClock() {
        setTitle("Floral Digital Clock");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        FloralBackgroundPanel backgroundPanel = new FloralBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        RoundedCardPanel cardPanel = new RoundedCardPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        cardPanel.setPreferredSize(new Dimension(460, 360));

        titleLabel = new JLabel("🌸 Digital Clock 🌸");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(153, 51, 102));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 45));
        timeLabel.setForeground(new Color(102, 0, 102));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dayLabel = new JLabel();
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        dayLabel.setForeground(new Color(204, 51, 153));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        dateLabel.setForeground(new Color(90, 70, 120));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        zoneLabel = new JLabel("Local Time");
        zoneLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        zoneLabel.setForeground(new Color(0, 102, 102));
        zoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formatButton = createStyledButton("Switch to 24 Hour");
        zoneButton = createStyledButton("Switch to GMT");

        formatButton.addActionListener(e -> {
            is24HourFormat = !is24HourFormat;

            if (is24HourFormat) {
                formatButton.setText("Switch to 12 Hour");
                System.out.println("Time format changed to 24-hour format.");
            } else {
                formatButton.setText("Switch to 24 Hour");
                System.out.println("Time format changed to 12-hour format.");
            }

            updateFormats();
        });

        zoneButton.addActionListener(e -> {
            isGMT = !isGMT;

            if (isGMT) {
                zoneButton.setText("Switch to Local Time");
                zoneLabel.setText("GMT Time");
                System.out.println("Timezone changed to GMT.");
            } else {
                zoneButton.setText("Switch to GMT");
                zoneLabel.setText("Local Time");
                System.out.println("Timezone changed to Local Time.");
            }

            updateFormats();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setMaximumSize(new Dimension(450, 80));
        buttonPanel.add(formatButton);
        buttonPanel.add(zoneButton);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(timeLabel);
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(dayLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(dateLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(zoneLabel);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(buttonPanel);

        backgroundPanel.add(cardPanel);
        add(backgroundPanel);

        updateFormats();
        startClockThread();

        setVisible(true);

    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(219, 112, 147));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 40));

        return button;
    }

    private void updateFormats() {
        if (is24HourFormat) {
            timeFormat = new SimpleDateFormat("HH:mm:ss");
        } else {
            timeFormat = new SimpleDateFormat("hh:mm:ss a");
        }

        dayFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

        TimeZone selectedZone;

        if (isGMT) {
            selectedZone = TimeZone.getTimeZone("GMT");
        } else {
            selectedZone = TimeZone.getDefault();
        }

        timeFormat.setTimeZone(selectedZone);
        dayFormat.setTimeZone(selectedZone);
        dateFormat.setTimeZone(selectedZone);
    }

    private void startClockThread() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                Calendar calendar = Calendar.getInstance();

                String time = timeFormat.format(calendar.getTime());
                String day = dayFormat.format(calendar.getTime());
                String date = dateFormat.format(calendar.getTime());

                SwingUtilities.invokeLater(() -> {
                    timeLabel.setText(time);
                    dayLabel.setText(day);
                    dateLabel.setText(date);
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Clock thread interrupted.");
                    break;
                }
            }
        });

        clockThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleClock());
    }
}

class FloralBackgroundPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 240, 245),
                getWidth(), getHeight(), new Color(230, 230, 250));

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawFlower(g2, 70, 70, 30, new Color(255, 182, 193), new Color(255, 255, 153));
        drawFlower(g2, getWidth() - 100, 80, 32, new Color(221, 160, 221), new Color(255, 250, 205));
        drawFlower(g2, 90, getHeight() - 100, 34, new Color(255, 192, 203), new Color(255, 245, 157));
        drawFlower(g2, getWidth() - 120, getHeight() - 110, 36, new Color(255, 160, 200), new Color(255, 255, 180));

        g2.dispose();
    }

    private void drawFlower(Graphics2D g2, int x, int y, int size, Color petalColor, Color centerColor) {
        g2.setColor(petalColor);

        g2.fillOval(x, y - size, size, size);
        g2.fillOval(x, y + size / 2, size, size);
        g2.fillOval(x - size, y, size, size);
        g2.fillOval(x + size / 2, y, size, size);

        g2.setColor(centerColor);
        g2.fillOval(x, y, size, size);
    }
}

class RoundedCardPanel extends JPanel {

    public RoundedCardPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);

        g2.setColor(new Color(230, 180, 210));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);

        g2.dispose();

        super.paintComponent(g);
    }
}