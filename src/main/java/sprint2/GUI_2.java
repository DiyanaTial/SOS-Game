package sprint2;

import javax.swing.*;
import java.awt.*;

public class GUI_2 extends JFrame{
    private JTextField boardSize;
    private JPanel boardPanel;
    private JLabel turnLabel;
    private JRadioButton simpleGame, generalGame;
    private GameLogic gameLogic;
    private JRadioButton blueS, blueO, greenS, greenO;

    public GUI_2() {
        setTitle("SOS GAME (Sprint 2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Top Part //
        JPanel topPanel = new JPanel();
        simpleGame = new JRadioButton("Simple Game");
        generalGame = new JRadioButton("General Game");

        ButtonGroup gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(simpleGame);
        gameTypeGroup.add(generalGame);

        topPanel.add(new JLabel("SOS Game"));
        topPanel.add(simpleGame);
        topPanel.add(generalGame);
        topPanel.add(new JLabel("Board size"));
        boardSize = new JTextField("8", 3);
        topPanel.add(boardSize);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);

        // Board panel
        boardPanel = new JPanel();
        // boardPanel.setPreferredSize(new Dimension(400, 400));
        add(boardPanel, BorderLayout.CENTER);

        // east and west panels
        JPanel leftPanel = createPlayerPanel("Blue");
        JPanel rightPanel = createPlayerPanel("Green");
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // bottom turn label
        turnLabel = new JLabel("Turn: Blue");
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(turnLabel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private JPanel createPlayerPanel(String color){
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel(color + " Player"));
        JRadioButton letterS = new JRadioButton("S", true);
        JRadioButton letterO = new JRadioButton("O");
        ButtonGroup group = new ButtonGroup();
        group.add(letterS);
        group.add(letterO);

        panel.add(letterS);
        panel.add(letterO);

        if (color.equals("Blue")) {
            blueS = letterS;
            blueO = letterO;
        }
        else {
            greenS = letterS;
            greenO = letterO;
        }

        return panel;
    }

    public void startGame(){
        try {
            int size = Integer.parseInt(boardSize.getText());
            // get game mode
            String mode = simpleGame.isSelected() ? "Simple Game" : "General Game";
            gameLogic = new GameLogic(size, mode);

            boardPanel.removeAll();
            boardPanel.setLayout(new GridLayout(size, size));

            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    JButton cell = new JButton();
                    int row = r, col = c;

                    cell.addActionListener(e -> {
                                char letter = gameLogic.isBlueTurn()
                                        ? (blueS.isSelected() ? 'S' : 'O')
                                        : (greenS.isSelected() ? 'S' : 'O');

                                if (gameLogic.makeMove(row, col, letter)) {
                                    cell.setText(String.valueOf(letter));
                                    cell.setForeground(gameLogic.isBlueTurn() ? Color.GREEN : Color.BLUE);
                                    turnLabel.setText("Turn: " + gameLogic.getTurnText());
                                } else {
                                    JOptionPane.showMessageDialog(this, "Invalid move");
                                }
                            });

                    boardPanel.add(cell);
                }
            }

            revalidate();
            repaint();
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Please enter a number > 2");

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI_2::new);
    }
}
