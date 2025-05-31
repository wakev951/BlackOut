package blackout;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BlackOut {

    private static boolean darkMode = true;

    public static void main(String[] args) {
        setLookAndFeel();
        SwingUtilities.invokeLater(BlackOut::createControlWindow);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(darkMode ? new FlatDarkLaf() : new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void createControlWindow() {
        JFrame controlFrame = new JFrame("BlackOut");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Blackout Buttons
        panel.add(createColorButton("Black Out", controlFrame, Color.BLACK));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createColorButton("White Out", controlFrame, Color.WHITE));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createColorButton("Red Out", controlFrame, Color.RED));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createColorButton("Green Out", controlFrame, Color.GREEN));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createColorButton("Blue Out", controlFrame, Color.BLUE));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));


        // Disco Mode
        JButton discoButton = createLargeButton("Disco Mode!");
        JTextField discoSpeedField = new JTextField("100%");
        discoSpeedField.setMaximumSize(new Dimension(200, 30));
        discoSpeedField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        discoSpeedField.setHorizontalAlignment(SwingConstants.CENTER);

        discoButton.addActionListener(e -> {
            float baseSpeed = 0.01f;
            String text = discoSpeedField.getText().trim().replace("%", "");
            try {
                float percentage = Float.parseFloat(text);
                float adjustedSpeed = baseSpeed * (percentage / 100f);
                showDiscoWindow(controlFrame, adjustedSpeed);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(controlFrame, "Invalid speed percentage!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(discoButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(discoSpeedField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Theme Toggle Button
        JButton toggleThemeButton = createLargeButton("Toggle Theme");
        toggleThemeButton.addActionListener(e -> {
            darkMode = !darkMode;
            setLookAndFeel();
            SwingUtilities.updateComponentTreeUI(controlFrame);
        });

        panel.add(toggleThemeButton);

        controlFrame.setContentPane(panel);
        controlFrame.setSize(400, 500); // Increased height for more buttons
        controlFrame.setLocationRelativeTo(null);
        controlFrame.setVisible(true);
    }

    private static JButton createColorButton(String label, JFrame controlFrame, Color color) {
        JButton button = createLargeButton(label);
        button.addActionListener(e -> showBlackoutWindow(controlFrame, color));
        return button;
    }

    private static JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private static void showBlackoutWindow(JFrame controlFrame, Color color) {
        Point controlLocation = controlFrame.getLocationOnScreen();
        GraphicsDevice targetDevice = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (GraphicsDevice gd : ge.getScreenDevices()) {
            Rectangle bounds = gd.getDefaultConfiguration().getBounds();
            if (bounds.contains(controlLocation)) {
                targetDevice = gd;
                break;
            }
        }

        if (targetDevice == null) {
            targetDevice = ge.getDefaultScreenDevice(); // Fallback
        }

        JFrame blackoutFrame = new JFrame(targetDevice.getDefaultConfiguration());
        blackoutFrame.setUndecorated(true);
        blackoutFrame.setBounds(targetDevice.getDefaultConfiguration().getBounds());
        blackoutFrame.getContentPane().setBackground(color);
        blackoutFrame.setAlwaysOnTop(true);

        blackoutFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                blackoutFrame.dispose();
            }
        });

        blackoutFrame.setVisible(true);
    }

    private static void showDiscoWindow(JFrame controlFrame, float speed) {
        Point controlLocation = controlFrame.getLocationOnScreen();
        GraphicsDevice targetDevice = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (GraphicsDevice gd : ge.getScreenDevices()) {
            Rectangle bounds = gd.getDefaultConfiguration().getBounds();
            if (bounds.contains(controlLocation)) {
                targetDevice = gd;
                break;
            }
        }

        if (targetDevice == null) {
            targetDevice = ge.getDefaultScreenDevice();
        }

        JFrame discoFrame = new JFrame(targetDevice.getDefaultConfiguration());
        discoFrame.setUndecorated(true);
        discoFrame.setBounds(targetDevice.getDefaultConfiguration().getBounds());
        discoFrame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        discoFrame.setContentPane(panel);

        Timer timer = new Timer(10, null); // Will be configured below

        final float[] hue = {0};

        timer.addActionListener(e -> {
            hue[0] += speed;
            if (hue[0] > 1) hue[0] = 0;
            Color c = Color.getHSBColor(hue[0], 1f, 1f);
            panel.setBackground(c);
        });

        discoFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timer.stop();
                discoFrame.dispose();
            }
        });

        discoFrame.setVisible(true);
        timer.start();
    }

}
