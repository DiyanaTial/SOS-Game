package sprint0.partb;
import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    public GUI(){
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
        JTextField boardSizeField = new JTextField("8", 3);
        topPanel.add(boardSizeField);
        add(topPanel,BorderLayout.NORTH);

        // Center //
        JPanel boardPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                int size = 8; // Default size of board
                int cellSize = getWidth()/size;
                for (int i = 0; i < size; i++){
                    g.drawLine(i * cellSize, 0, i * cellSize, getHeight());
                    g.drawLine(0, i * cellSize, getWidth(), i * cellSize);
                }
            }
        };
        boardPanel.setPreferredSize(new Dimension(400,400));
        add(boardPanel,BorderLayout.CENTER);

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

        add(greenPanel,BorderLayout.WEST);

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
        add(bluePanel,BorderLayout.EAST);


        setVisible(true);



    }


    public static void main(String[] args){
       SwingUtilities.invokeLater(GUI::new);
    }
}
