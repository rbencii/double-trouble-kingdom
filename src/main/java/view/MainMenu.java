package view;

import model.Map;
import model.Pair;
import model.Pathfinder;
import persistence.FileHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Implementation of MainMenu panel.
 */
public class MainMenu extends JPanel {
    /**
     * The default map name.
     */
    private String mapName;
    BufferedImage background;
    /**
     * The label to show the player names.
     */
    private final JLabel playersLabel;
    /**
     * The name of player 1.
     */
    private String p1Name;
    /**
     * The name of player 2.
     */
    private String p2Name;
    /**
     * The loaded Map instance.
     */
    private Pair<Map, File> map;
    /**
     * The label to the loaded map's name.
     */
    private final JLabel mapNameLabel;

    /**
     * Constructs a new MainMenu instance.
     *
     * @param wnd the parent window
     */
    public MainMenu(MainWindow wnd) {
        try {
            background = ImageIO.read(new File("./src/main/resources/MainMenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        FileDialog fileDialog = new FileDialog();

        JPanel topBar = new JPanel();

        topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        p1Name = "Játékos1";
        p2Name = "Játékos2";
        map = FileHandler.loadMap(new File("Test.dtk")) != null ? FileHandler.loadMapAndFile(new File("Test.dtk")) : fileDialog.loadMapDialog();
        assert map != null;
        mapName = map.getMap().getName();

        setOpaque(false);
        topBar.setOpaque(false);
        this.playersLabel = new JLabel(p1Name + " vs " + p2Name, SwingConstants.CENTER);
        this.playersLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        this.playersLabel.setForeground(Color.WHITE);

        topBar.add(playersLabel);
        this.mapNameLabel = new JLabel(map != null ? map.getMap().getName() : mapName, SwingConstants.CENTER);
        this.mapNameLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        this.mapNameLabel.setForeground(Color.WHITE);
        topBar.add(mapNameLabel);
        this.add(topBar);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        //centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        //JLabel title = new JLabel("Double Trouble Kingdom");
        //title.setAlignmentX(Component.CENTER_ALIGNMENT);
        //centerPanel.add(title);

        JPanel centerButtons = new JPanel();
        centerButtons.setOpaque(false);
        centerButtons.setLayout(new FlowLayout());
        JRoundedButton start = new JRoundedButton("Start", 100, 50);
        centerButtons.add(start);
        start.addActionListener(e -> wnd.startGame(64, 32));

        JRoundedButton editor = new JRoundedButton("Térképszerkesztő", 200, 50);
        centerButtons.add(editor);
        centerPanel.add(centerButtons);
        this.add(centerPanel);
        editor.addActionListener(e -> wnd.startEditor(64, 32));

        JPanel bottomButtons = new JPanel(new FlowLayout());
        bottomButtons.setOpaque(false);
        JRoundedButton rename = new JRoundedButton("Átnevezés", 110, 50);
        rename.addActionListener(e -> {
            this.p1Name = (String) JOptionPane.showInputDialog(
                    null,
                    "1. Játékos neve",
                    "Átnevezés",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "név1");

            this.p2Name = (String) JOptionPane.showInputDialog(
                    null,
                    "2. Játékos neve",
                    "Átnevezés",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "név2");

            updateName();
            repaint();

        });
        bottomButtons.add(rename);
        JRoundedButton mapSelect = new JRoundedButton("Pályaválasztás", 150, 50);
        mapSelect.addActionListener(e -> {
            Pair<Map, File> loaded = fileDialog.loadMapDialog();
            if (map != null) {
                this.map = loaded;
                this.mapName = loaded.getMap().getName();
                Pathfinder.setMap(map.getMap());
                updateMapName();
                repaint();
            }
        });
        bottomButtons.add(mapSelect);
        this.add(bottomButtons);
        setColors(start, new Color(116, 231, 4));
        setColors(editor, new Color(180, 26, 26));
        setColors(rename, new Color(208, 198, 10));
        setColors(mapSelect, new Color(124, 7, 201));
        Pathfinder.setMap(map.getMap());
    }

    public Pair<Map, File> getMap() {
        return map;
    }

    /**
     * Returns player 1's name.
     *
     * @return player 1's name
     */
    public String getP1Name() {
        return p1Name;
    }

    /**
     * Returns player 2's name.
     *
     * @return player 2's name
     */
    public String getP2Name() {
        return p2Name;
    }

    /**
     * Updates the players' name
     */
    public void updateName() {
        this.playersLabel.setText("" + p1Name + " vs " + p2Name);
        repaint();
    }

    /**
     * Changes the Map to be loaded.
     */
    public void updateMapName() {
        this.mapNameLabel.setText("Pálya: " + map.getMap().getName());
        repaint();
    }

    /**
     * Draws the background.
     *
     * @param g graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);

    }

    /**
     * Sets the colors of the buttons.
     *
     * @param button the button to be changed
     * @param color  the color to be set
     */
    private void setColors(JRoundedButton button, Color color) {
        button.setColors(new Color[]{new Color(Math.min(color.getRed() + 30, 255), Math.min(color.getGreen() + 30, 255), Math.min(color.getBlue() + 30, 255)), new Color(0, 0, 0, 0), color});
    }
}
