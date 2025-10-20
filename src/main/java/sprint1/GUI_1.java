package sprint1;
import javax.swing.*;
import java.awt.*;

public class GUI_1 extends JFrame{
    private JTextField boardSize;
    private JPanel boardPanel;
    public GUI_1() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Top Part //
        JPanel topPanel = new JPanel();
        JRadioButton simpleGame = new JRadioButton("Simple Game");
        JRadioButton generalGame = new JRadioButton("General Game");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(simpleGame);
        buttonGroup.add(generalGame);
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
        boardPanel.setPreferredSize(new Dimension(400, 400));
        add(boardPanel, BorderLayout.CENTER);

        // Left side player (Green) //
        JPanel greenPanel = new JPanel(new GridLayout(0, 1));
        greenPanel.add(new JLabel("Green Player"));

        // Human option with SOS under
        JPanel greenHumanPanel = new JPanel(new GridLayout(0, 1));
        JRadioButton greenHuman = new JRadioButton("Human", true);
        greenHumanPanel.add(greenHuman);

        JRadioButton greenS = new JRadioButton("S", true);
        JRadioButton greenO = new JRadioButton("O");
        ButtonGroup greenLetters = new ButtonGroup();
        greenLetters.add(greenS);
        greenLetters.add(greenO);
        greenHumanPanel.add(greenS);
        greenHumanPanel.add(greenO);

        // Computer option
        JRadioButton greenComputer = new JRadioButton("Computer");
        ButtonGroup greenGroup = new ButtonGroup();
        greenGroup.add(greenHuman);
        greenGroup.add(greenComputer);

        // Add both human and computer
        greenPanel.add(greenHumanPanel); // Human & SO
        greenPanel.add(greenComputer);

        add(greenPanel, BorderLayout.WEST);

        // Right Side Player (Blue) //
        JPanel bluePanel = new JPanel(new GridLayout(0, 1));
        bluePanel.add(new JLabel("Blue Player"));

        // Human & SOS Letter
        JPanel blueHumanPanel = new JPanel(new GridLayout(0, 1));
        JRadioButton blueHuman = new JRadioButton("Human", true);
        blueHumanPanel.add(blueHuman);

        JRadioButton blueS = new JRadioButton("S", true);
        JRadioButton blueO = new JRadioButton("O");
        ButtonGroup blueLetters = new ButtonGroup();
        blueLetters.add(blueS);
        blueLetters.add(blueO);
        blueHumanPanel.add(blueS);
        blueHumanPanel.add(blueO);

        // Computer
        JRadioButton blueComputer = new JRadioButton("Computer");
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHuman);
        blueGroup.add(blueComputer);

        // Add Everything
        bluePanel.add(blueHumanPanel);
        bluePanel.add(blueComputer);
        add(bluePanel, BorderLayout.EAST);

        setVisible(true);

    }

    public void startGame(){
        try {
            // get the int that user typed
            int size = Integer.parseInt(boardSize.getText());

            if (size > 2){
                remove(boardPanel); // get rid of old board
                boardPanel = new JPanel(new GridLayout(size, size)){
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        int cellWidth = getWidth() / size;
                        int cellHeight = getHeight() / size;

                        //vertical
                        for (int i = 0; i < size; i++){
                            g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
                        }

                        // horizontal
                        for (int i = 0; i < size; i++){
                            g.drawLine(0, i*cellHeight, getWidth(), i*cellHeight);
                        }

                    }
                };

                for (int i = 0; i < size * size; i++){
                    JButton square = new JButton();

                    // clickable button but with no borders in each (doesn't look like separate squares)
                    square.setBorderPainted(false);
                    square.setContentAreaFilled(false);
                    square.setFocusPainted(false);

                    //example click action , can change later
                    square.addActionListener(e -> {
                        square.setText("S"); // for now
                    });

                    boardPanel.add(square);
                }

                add (boardPanel,BorderLayout.CENTER);
                revalidate();
                repaint();
            }
            else {
                JOptionPane.showMessageDialog(null,"Invalid board size.");
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Invalid board size.");
        }
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(GUI_1::new);
    }
}




