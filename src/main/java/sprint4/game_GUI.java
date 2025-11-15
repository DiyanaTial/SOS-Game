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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 1000));
        setLayout(new BorderLayout());

        /*
         *** Make UI Look Prettier **
         * This was done with the help of ChatGPT - Mainly 75%
         */
        Font uiFont = new Font("SansSerif", Font.PLAIN, 14);
        UIManager.put("Label.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("RadioButton.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        // add padding around main content
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Top
        JPanel topPanel = new JPanel(new BorderLayout());

        // For Title
        JLabel titleLabel = new JLabel("SOS Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        simpleMode = new JRadioButton("Simple Game");
        generalMode = new JRadioButton("General Game");
        ButtonGroup gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(simpleMode);
        gameTypeGroup.add(generalMode);

        controlsPanel.add(simpleMode);
        controlsPanel.add(generalMode);

        controlsPanel.add(new JLabel("Board size"));
        boardSize = new JTextField("3", 3);
        controlsPanel.add(boardSize);

        JButton startButton = new JButton("Start New Game");
        startButton.addActionListener(e -> startGame());
        controlsPanel.add(startButton);
        topPanel.add(controlsPanel, BorderLayout.CENTER);

        // Progress bar
        turnLabel = new JLabel("Current Turn: Blue", SwingConstants.CENTER);
        topPanel.add(turnLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Side panels
        JPanel leftPanel = createPlayerPanel("Blue");
        JPanel rightPanel = createPlayerPanel("Green");
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Board panel
        boardPanel = new game_GUI.BoardPanel();
        boardPanel.setBackground(new Color(245, 245, 245)); // light gray
        add(boardPanel, BorderLayout.CENTER);

        // Bottom
        blueScoreLabel = new JLabel("Blue Score: 0");
        greenScoreLabel = new JLabel("Green Score: 0");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(blueScoreLabel, BorderLayout.WEST);
        bottomPanel.add(greenScoreLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // center window
        setVisible(true);
    }

    private JPanel createPlayerPanel(String color){
        JPanel panel = new JPanel(new GridLayout(0,1,0,5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                color + " Player",
                0, 0,
                panel.getFont().deriveFont(Font.BOLD)
        ));

        JRadioButton letterS = new JRadioButton("S", true);
        JRadioButton letterO = new JRadioButton("O");
        ButtonGroup group = new ButtonGroup();
        group.add(letterS);
        group.add(letterO);

        panel.add(new JLabel("Letter:"));
        panel.add(letterS);
        panel.add(letterO);

        // human/computer choices
        JRadioButton human = new JRadioButton("Human", true);
        JRadioButton computer = new JRadioButton("Computer");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(human);
        typeGroup.add(computer);

        panel.add(new JLabel("Player Type:"));
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
        // Clearing old game logic when user clicks Start New Game
        gameLogic = null;

        boardPanel.removeAll();
        boardPanel.lines.clear();
        boardPanel.revalidate();
        boardPanel.repaint();

        blueScoreLabel.setText("Blue Score: 0");
        greenScoreLabel.setText("Green Score: 0");
        blueScoreLabel.setVisible(false);
        greenScoreLabel.setVisible(false);

        turnLabel.setText("Current Turn: -");
        turnLabel.setForeground(Color.BLACK);

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

            boardPanel.setLayout(new GridLayout(size, size));

            // initializing the cells for computer
            cells = new JButton[size][size];

            // create all buttons
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    JButton cell = new JButton();
                    int row = r, col = c;

                    //tile styling
                    cell.setFont(new Font("SansSerif", Font.BOLD, 28));
                    cell.setFocusPainted(false);
                    cell.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
                    cell.setBackground(Color.WHITE);
                    cell.setOpaque(true);

                    cells[row][col] = cell;
                    cell.addActionListener(e -> handleHumanClick(row, col, cell));
                    boardPanel.add(cell);
                }
            }

            // refresh GUI
            boardPanel.revalidate();
            boardPanel.repaint();

            updateTurnLabel();
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
            if (boardPanel != null){
                boardPanel.repaint();
            }
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
                int r1 = s[0][0], c1 = s[0][1];
                int r2 = s[2][0], c2 = s[2][1];
                boardPanel.addLine(r1, c1, r2, c2, lineColor);
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
            turnLabel.setForeground(Color.BLACK);

            boardPanel.repaint();

        }
        else{
            // show next player's turn
            updateTurnLabel();
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

        int delay = 1500; // 1.5 seconds
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
        List<SOSLine> lines = new ArrayList<>();

        public void addLine(int r1, int c1, int r2, int c2, Color color){
            lines.add(new SOSLine(r1, c1, r2, c2, color));
            repaint();
        }

        @Override
        protected void paintChildren(Graphics g){
            super.paintChildren(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            // draw ad children
            for (SOSLine line : lines){
                Point start = getCellCenter(line.r1, line.c1);
                Point end = getCellCenter(line.r2, line.c2);

                g2.setColor(line.color);
                g2.draw(new Line2D.Double(start, end));
            }
        }
    }

    static class SOSLine{
        int r1, c1;
        int r2, c2;
        Color color;

        public SOSLine(int r1, int c1, int r2, int c2, Color color){
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
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

    // helper to update turn label
    private void updateTurnLabel(){
        if (gameLogic == null) {
            turnLabel.setText("Current turn: -");
            turnLabel.setForeground(Color.BLACK);
            return;
        }

        String text = "Current turn: " + gameLogic.getTurnNext();
        turnLabel.setText(text);

        if (gameLogic.isBlueTurn()){
            turnLabel.setForeground(new Color(0, 90, 200));
        }
        else {
            turnLabel.setForeground(new Color(0, 140, 0));
        }
    }
}


