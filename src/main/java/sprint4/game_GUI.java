package sprint4;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class game_GUI extends JFrame{
    private JTextField boardSize;
    private game_GUI.BoardPanel boardPanel;
    private JLabel turnLabel;
    private JRadioButton simpleMode, generalMode;
    private JRadioButton blueS, blueO, greenS, greenO;

    // for computer as well
    private JRadioButton blueHuman, blueComputer;
    private JRadioButton greenHuman, greenComputer;

    private GameLogic gameLogic;
    private int size;

    // for player class
    private Player bluePlayer;
    private Player greenPlayer;

    // store each button for computer moves later
    private JButton[][] cells;

    // to keep track of scores
    private JLabel blueScoreLabel;
    private JLabel greenScoreLabel;

    public game_GUI() {
        setTitle("SOS GAME");
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
        boardPanel = new game_GUI.BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Bottom
        blueScoreLabel = new JLabel("Blue Score: 0");
        greenScoreLabel = new JLabel("Green Score: 0");
        turnLabel = new JLabel("Current Turn: Blue", SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(blueScoreLabel, BorderLayout.WEST);
        bottomPanel.add(turnLabel, BorderLayout.CENTER);
        bottomPanel.add(greenScoreLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

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

        // human/computer choices
        JRadioButton human = new JRadioButton("Human", true);
        JRadioButton computer = new JRadioButton("Computer");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(human);
        typeGroup.add(computer);
        panel.add(human);
        panel.add(computer);

        if (color.equals("Blue")) {
            blueS = letterS;
            blueO = letterO;
            blueHuman = human;
            blueComputer = computer;
        } else {
            greenS = letterS;
            greenO = letterO;
            greenHuman = human;
            greenComputer = computer;
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

            // add the createPlayers function
            createPlayers();

            boardPanel.removeAll();
            boardPanel.lines.clear();
            boardPanel.setLayout(new GridLayout(size, size));

            // initializing the cells for computer
            cells = new JButton[size][size];

            // create all buttons
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    JButton cell = new JButton();
                    int row = r, col = c;
                    cell.setFont(new Font("Arial", Font.BOLD, 18));

                    cells[row][col] = cell;

                    cell.addActionListener(e -> handleHumanClick(row, col, cell));
                    boardPanel.add(cell);
                }
            }

            // refresh GUI
            revalidate();
            repaint();
            turnLabel.setText("Current turn: " + gameLogic.getTurnNext());

            updateScoreLabels();

            maybeComputerMove();
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a number greater than 2.");
        }
    }

    // make startGame more modular by extracting click logic into helper method
    private void handleHumanClick(int row, int col, JButton cell) {
        // if game isn't started or already over, ignore the clicks
        if (gameLogic == null || gameLogic.gameOver()){
            return;
        }

        // check whose turn it is
        boolean blueTurnNow = gameLogic.isBlueTurn();
        Player current = blueTurnNow ? bluePlayer : greenPlayer;

        // if human turn, proceed
        boolean blueTurnBefore = blueTurnNow;

        char letter = blueTurnBefore
                ? (blueS.isSelected() ? 'S' : 'O')
                : (greenS.isSelected() ? 'S' : 'O');

        if (gameLogic.makeMove(row, col, letter)) {
            applyMoveToUI(row, col, letter, blueTurnBefore);

            if (!gameLogic.gameOver()) {
                maybeComputerMove();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move!");
        }
    }

    // AI play
    private void applyMoveToUI(int row, int col, char letter, boolean blueTurnBefore) {
        // find button for this cell
        JButton cell = cells[row][col];

        // update button text & color
        cell.setText(String.valueOf(letter));
        cell.setForeground(blueTurnBefore ? Color.BLUE : Color.GREEN);

        // draw SOS lines created by this move
        List<int[][]> segs = gameLogic.getLastSOSes();
        if (!segs.isEmpty()) {
            Color lineColor = blueTurnBefore ? Color.BLUE : Color.GREEN;
            for (int[][] s : segs){
                Point start = getCellCenter(s[0][0], s[0][1]);
                Point end = getCellCenter(s[2][0], s[2][1]);
                boardPanel.addLine(start, end, lineColor);
            }
        }

        updateScoreLabels();

        // check game over
        if (gameLogic.gameOver()) {
            String message = gameLogic.getWinner().equals("Draw")
                    ? "The game has concluded in a draw."
                    : "Winner: " + gameLogic.getWinner();
            JOptionPane.showMessageDialog(this, message);
            turnLabel.setText("Game Over!");
        }
        else{
            // show next player's turn
            turnLabel.setText("Current turn: " + gameLogic.getTurnNext());
        }
    }

    private void maybeComputerMove() {
        if (gameLogic == null || gameLogic.gameOver()) {
            return;
        }

        // only start AI timer if its computer turn
        boolean blueTurnNow = gameLogic.isBlueTurn();
        Player current = blueTurnNow ? bluePlayer : greenPlayer;
        if (!(current instanceof ComputerPlayer)) {
            return;
        }

        int delay = 2000; // 1 second
        Timer aiTimer = new Timer(delay, e -> {
            if (gameLogic == null || gameLogic.gameOver()){
                ((Timer) e.getSource()).stop();
                return;
            }
            boolean turnIsBlue = gameLogic.isBlueTurn();
            Player currentPlayer = turnIsBlue ? bluePlayer : greenPlayer;

            if (!(currentPlayer instanceof ComputerPlayer)) {
                ((Timer) e.getSource()).stop();
                return;
            }

            // make ONE computer move
            handleComputerMove();
        });

        aiTimer.setInitialDelay(0);
        aiTimer.start();
    }

    private void handleComputerMove() {
        if (gameLogic == null || gameLogic.gameOver()) {
            return;
        }

        boolean blueTurnNow = gameLogic.isBlueTurn();
        Player current = blueTurnNow ? bluePlayer : greenPlayer;

        // if not computer player, do nothing
        if (!(current instanceof ComputerPlayer)){
            return;
        }

        ComputerPlayer computer = (ComputerPlayer) current;
        Move move = computer.chooseMove(gameLogic);

        // no possible move
        if (move == null){
            return;
        }

        boolean blueTurnBefore = blueTurnNow;

        // apply game logic
        if (gameLogic.makeMove(move.row, move.col, move.letter)){
            // update UI for this move
            applyMoveToUI(move.row, move.col, move.letter, blueTurnBefore);
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
        List<game_GUI.SOSLine> lines = new ArrayList<>();

        public void addLine(Point start, Point end, Color color){
            lines.add(new game_GUI.SOSLine(start, end, color));
            repaint();
        }

        @Override
        public void paint(Graphics g){
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            // draw ad children
            for (game_GUI.SOSLine line : lines){
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

    public void createPlayers(){
        // blue player human and computer
        if (blueComputer.isSelected()){
            bluePlayer = new ComputerPlayer("Blue Computer", true);
        }
        else{
            bluePlayer = new HumanPlayer("Blue Human", true);
        }

        // green player human and computer
        if (greenComputer.isSelected()) {
            greenPlayer = new ComputerPlayer("Green Computer", false);
        }
        else{
            greenPlayer = new HumanPlayer("Green Human", false);
        }
    }

    private void updateScoreLabels() {
        if (gameLogic instanceof GeneralMode) {
            GeneralMode gm = (GeneralMode) gameLogic;
            blueScoreLabel.setText("Blue Score: " + gm.getBlueScore());
            greenScoreLabel.setText("Green Score: " + gm.getGreenScore());

            blueScoreLabel.setVisible(true);
            greenScoreLabel.setVisible(true);
        }
        else{
            blueScoreLabel.setVisible(false);
            greenScoreLabel.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(game_GUI::new);
    }

}
// TODO: Fix the lines to be over instead of under.

