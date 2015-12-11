import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import static java.lang.StrictMath.sqrt;

/**
 * Created by evanzyker on 20/11/15.
 */
public class Vue extends JFrame {

    private final int dim = 650;
    public JLabel[][] grid;
    protected Model model;
    protected int row;
    protected int col;

    public JMenuBar menuBar;
    public JMenu option;
    public JMenu size;
    public JMenu scores;
    public JMenuItem scoreLow;
    public JMenuItem scoreMedium;
    public JMenuItem scoreBig;
    public JMenuItem sizeLow;
    public JMenuItem sizeMedium;
    public JMenuItem sizeBig;
    public JMenuItem restart;
    public JLabel score;


    public Vue(Model model){
        this.model = model;
        initAttributes(this.model.getGridSize());
        createWidget(this.model.getGridSize());
        placeWidget();
        refresh();
        setSize(new Dimension(dim, dim));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("2048 v" + this.model.getversion());
    }

    private void placeWidget() {
        getContentPane().removeAll();
        JPanel masterPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(model.getGridSize(), model.getGridSize()));

        for (int r = 0; r < row; r++){
            for (int c = 0; c < col; c++){
                gridPanel.add(grid[r][c]);
            }
        }

        size.add(sizeLow);
        size.add(sizeMedium);
        size.add(sizeBig);
        option.add(size);
        option.add(restart);
        scores.add(scoreLow);
        scores.add(scoreMedium);
        scores.add(scoreBig);

        menuBar.add(option);
        menuBar.add(scores);
        menuBar.add(score, RIGHT_ALIGNMENT);

        setJMenuBar(menuBar);


        masterPanel.add(gridPanel, BorderLayout.CENTER);
        setContentPane(masterPanel);
        getContentPane().doLayout();
        update(getGraphics());
    }

    private void createWidget(int gridSize) {
        //Initialisation de la génération de la grille
        row = gridSize;
        col = gridSize;
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++) {
                grid[r][c] = new JLabel("", SwingConstants.CENTER);
                grid[r][c].setBorder(new LineBorder(new Color(108, 108, 108), 6));
            }
        }
    }

    private void initAttributes(int gridSize) {
        grid = new JLabel[gridSize][gridSize];
        //On insère un JLabel dans chaque case du tableau dynamiquement
        for(int i = 0; i < gridSize; i++){
            for (int j = 0; j < gridSize; j++){
                grid[i][j] = new JLabel(String.valueOf(model.getCase(i, j)), SwingConstants.CENTER);
            }
        }

        menuBar = new JMenuBar();
        option = new JMenu("Options");
        size = new JMenu("Taille de la grille");
        sizeLow = new JMenuItem("Petite");
        sizeMedium = new JMenuItem("Moyenne");
        sizeBig = new JMenuItem("Grande");
        restart = new JMenuItem("Recommencer");
        score = new JLabel("Score: " + model.getScoreString());
        scores = new JMenu("Meilleurs scores");
        scoreLow = new JMenuItem("Petite");
        scoreMedium = new JMenuItem("Moyenne");
        scoreBig = new JMenuItem("Grande");
    }

    public void setControlKey(ControlKey controlkey) {
        this.addKeyListener(controlkey);
    }

    public void refresh(){
        for (int r = 0; r < row; r++){
            for (int c = 0; c < col; c++) {
                grid[r][c].setFont(new Font("Comic sans MS", Font.PLAIN, 20));
                if (model.getCase(r, c) == 0) {
                    grid[r][c].setText("");
                    grid[r][c].setBackground(new Color(139, 146, 138));
                    grid[r][c].setForeground(Color.BLACK);
                } else {
                    grid[r][c].setText(String.valueOf(model.getCase(r, c)));
                }
                if (!grid[r][c].getText().equals("")){
                    switch(Integer.parseInt(grid[r][c].getText())){
                        case 2:
                            grid[r][c].setBackground(new Color(223, 222, 167));
                            grid[r][c].setForeground(Color.BLACK);
                            break;
                        case 4:
                            grid[r][c].setBackground(new Color(225, 157, 57));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 8:
                            grid[r][c].setBackground(new Color(221, 131, 19));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 16:
                            grid[r][c].setBackground(new Color(206, 93, 49));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 32:
                            grid[r][c].setBackground(new Color(214, 65, 36));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 64:
                            grid[r][c].setBackground(new Color(198, 36, 9));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 128:
                            grid[r][c].setBackground(new Color(209, 204, 36));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 256:
                            grid[r][c].setBackground(new Color(215, 207, 27));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 512:
                            grid[r][c].setBackground(new Color(243, 246, 25));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 1024:
                            grid[r][c].setBackground(new Color(233, 206, 13));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 2048:
                            grid[r][c].setBackground(new Color(255, 75, 0));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 4096:
                            grid[r][c].setBackground(new Color(255, 45, 0));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                        case 8192:
                            grid[r][c].setBackground(new Color(255, 4, 0));
                            grid[r][c].setForeground(Color.WHITE);
                            break;
                    }
                }
                grid[r][c].setOpaque(true);
                score.setText("Score: " + model.getScoreString());
            }
        }
        if (!model.isPlayable()){
            JOptionPane message = new JOptionPane();
            if (model.hasWon()){
                message.showMessageDialog(this, "Vous avez gagné !", "VICTOIRE", JOptionPane.INFORMATION_MESSAGE);
                model.setBestScores();
            }else{
                message.showMessageDialog(this, "Vous avez perdu !", "DEFAITE", JOptionPane.INFORMATION_MESSAGE);
                model.setBestScores();
            }
        }
    }

    public void setControlMenu(ControlMenu cm) {
        sizeLow.addActionListener(cm);
        sizeMedium.addActionListener(cm);
        sizeBig.addActionListener(cm);
        restart.addActionListener(cm);
        scoreLow.addActionListener(cm);
        scoreMedium.addActionListener(cm);
        scoreBig.addActionListener(cm);
    }

    public void redraw() {
        initAttributes(this.model.getGridSize());
        createWidget(this.model.getGridSize());
        placeWidget();
        refresh();
        setSize(new Dimension(dim, dim));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void displayScore(String type) {
        String text = "";
        int ind;
        switch (type){
            case "low":
                ind = 0;
                break;
            case "medium":
                ind = 1;
                break;
            case "big":
                ind = 2;
                break;
            default:
                ind = 0;
                break;
        }
        for (int i = 0; i < 3; i++){
            text += String.valueOf(i + 1) + ". " + String.valueOf(model.getScore(ind, i)) + "\n";
        }
        JOptionPane message = new JOptionPane();
        message.showMessageDialog(this, text, "Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
