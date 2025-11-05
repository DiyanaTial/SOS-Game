package sprint3;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GUI_3 extends JFrame{
    private JTextField boardSize;
    private BoardPanel boardPanel;
    private JLabel turnLabel;
    private JRadioButton simpleMode, generalMode;
    private JRadioButton blueS, blueO, greenS, greenO;
    private GameLogic gameLogic;
    private int size;

    public GUI_3() {
        setTitle("SOS GAME (Sprint 3)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Top
        JPanel topPanel = new JPanel();
        simpleMode = new JRadioButton("Simple Game");
        generalMode = new JRadioButton("General Game");
        ButtonGroup gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(simpleMode);
        gameTypeGroup.add(generalMode);

        topPanel.add(new JLabel("SOS Game"));
        topPanel.add(simpleMode);
        topPanel.add(generalMode);

        topPanel.add(new JLabel("Board size"));
        boardSize = new JTextField("3", 3);
        topPanel.add(boardSize);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);

        // Side panels
        JPanel leftPanel = createPlayerPanel("Blue");
        JPanel rightPanel = createPlayerPanel("Green");
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Board panel
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Bottom
        turnLabel = new JLabel("Current turn: Blue", SwingConstants.CENTER);
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
            blueS = letterS; blueO = letterO;
        } else {
            greenS = letterS; greenO = letterO;
        }
        return panel;
    }

    public void startGame(){
        try {
            size = Integer.parseInt(boardSize.getText());
            if (size <= 2) {
                JOptionPane.showMessageDialog(this, "Please enter a number greater than 2.");
                return;
            }
            if (!simpleMode.isSelected() && !generalMode.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please select a game mode before starting.");
                return;
            }

            gameLogic = simpleMode.isSelected() ? new SimpleMode(size) : new GeneralMode(size);
            gameLogic.clearLastSOSes();

            boardPanel.removeAll();
            boardPanel.lines.clear();
            boardPanel.setLayout(new GridLayout(size, size));

            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    JButton cell = new JButton();
                    int row = r, col = c;
                    cell.setFont(new Font("Arial", Font.BOLD, 18));

                    cell.addActionListener(e -> {
                        // capture who is moving BEFORE mutate state
                        boolean blueTurnBefore = gameLogic.isBlueTurn();

                        char letter = blueTurnBefore
                                ? (blueS.isSelected() ? 'S' : 'O')
                                : (greenS.isSelected() ? 'S' : 'O');

                        if (gameLogic.makeMove(row, col, letter)) {
                            cell.setText(String.valueOf(letter));
                            // color by the player who JUST moved
                            cell.setForeground(blueTurnBefore ? Color.BLUE : Color.GREEN);

                            // Draw all SOS lines created by this move
                            List<int[][]> segs = gameLogic.getLastSOSes();
                            if (!segs.isEmpty()) {
                                Color lineColor = blueTurnBefore ? Color.BLUE : Color.GREEN;
                                for (int[][] s : segs) {
                                    Point start = getCellCenter(s[0][0], s[0][1]);
                                    Point end   = getCellCenter(s[2][0], s[2][1]);
                                    boardPanel.addLine(start, end, lineColor);
                                }
                            }

                            // check if end of game
                            if (gameLogic.gameOver()){
                                String message = gameLogic.getWinner().equals("Draw")
                                        ? "It's a Draw. No winners."
                                        : "Winner: " + gameLogic.getWinner();
                                JOptionPane.showMessageDialog(this, message);
                                turnLabel.setText("Game over");
                            } else {
                                // show next player's turn
                                turnLabel.setText("Current turn: " + gameLogic.getTurnText());
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Oops. Invalid move!");
                        }
                    });
                    boardPanel.add(cell);
                }
            }

            revalidate();
            repaint();
            turnLabel.setText("Current turn: Blue");
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Please enter a number > 2");
        }
    }

    // Convert (row,col) to center pixel inside the grid
    private Point getCellCenter(int row, int col){
        Component comp = boardPanel.getComponent(row * size + col);
        Rectangle bounds = comp.getBounds();
        int x = bounds.x + bounds.width / 2;
        int y = bounds.y + bounds.height / 2;
        return new Point(x, y);
    }

    // panel that draws lines over the grid
    class BoardPanel extends JPanel {
        List<SOSLine> lines = new ArrayList<>();

        public void addLine(Point start, Point end, Color color){
            lines.add(new SOSLine(start, end, color));
            repaint();
        }

        @Override
        // TODO: Fix the lines to be over instead of under.
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            // draw ad children
            for (SOSLine line : lines){
                g2.setColor(line.color);
                g2.draw(new Line2D.Double(line.start, line.end));
            }
        }
    }

    static class SOSLine{
        Point start, end;
        Color color;
        public SOSLine(Point start, Point end, Color color){
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI_3::new);
    }
}
// TODO: Fix the lines to be over instead of under.
// TODO: Make the game to not be able to click any lines after game over
// TODO: Add point counter gui for general game
