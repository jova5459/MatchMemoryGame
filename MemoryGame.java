import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGame extends JFrame {
    private List<String> imagePaths;
    private List<String> cardImages;
    private JButton[] cardButtons;
    private int gridSize;
    private int numberOfMatches;
    private int firstCardIndex = -1;
    private int secondCardIndex;
    private int moves;
    private Timer timer;

    public MemoryGame(int gridSize) {
        this.gridSize = gridSize;
        setTitle("Picture Memory Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagePaths = new ArrayList<>();
        cardImages = new ArrayList<>();
        cardButtons = new JButton[gridSize];

        String[] allImagePaths = {
                "Apple.png",
                "Banana.png",
                "Blueberry.png",
                "Cherry.png",
                "Grapes.png",
                "Honeydew.png",
                "Kiwi.png",
                "Lemon.png",
                "Mango.png",
                "Pear.png",
                "Pineapple.png",
                "Watermelon.png"
        };

        if (gridSize == 12) {
            for (int i = 0; i < 6; i++) {
                imagePaths.add(allImagePaths[i]);
            }
        } else if (gridSize == 24) {
            Collections.addAll(imagePaths, allImagePaths);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid grid size. Please enter 12 or 24.");
            System.exit(1);
        }

        imagePaths.addAll(imagePaths);

        Collections.shuffle(imagePaths);
        Collections.shuffle(cardImages);

        JPanel cardPanel = new JPanel(new GridLayout(6, 4));

        for (int i = 0; i < cardButtons.length; i++) {
            final int index = i;
            cardButtons[i] = new JButton();
            cardButtons[i].setIcon(new ImageIcon("blank.png"));
            cardButtons[i].addActionListener(e -> handleCardClick(index));
            cardPanel.add(cardButtons[i]);
        }

        add(cardPanel);
        moves = 0;
        numberOfMatches = 0;
        timer = new Timer();
    }

    private void handleCardClick(int index) {
        if (cardButtons[index].getIcon() == null) {
            return;
        }

        if (firstCardIndex == -1) {
            firstCardIndex = index;
            cardButtons[firstCardIndex].setIcon(new ImageIcon(imagePaths.get(index)));
        } else {
            secondCardIndex = index;
            cardButtons[secondCardIndex].setIcon(new ImageIcon(imagePaths.get(index)));

            moves++;

            if (imagePaths.get(firstCardIndex).equals(imagePaths.get(secondCardIndex))) {
                numberOfMatches++;
                firstCardIndex = -1;
                secondCardIndex = -1;

                if (numberOfMatches == imagePaths.size() / 2) {
                    handleGameWin();
                }
            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        cardButtons[firstCardIndex].setIcon(new ImageIcon("blank.png"));
                        cardButtons[secondCardIndex].setIcon(new ImageIcon("blank.png"));
                        firstCardIndex = -1;
                        secondCardIndex = -1;
                    }
                }, 5000);
            }
        }
    }

    private void handleGameWin() {
        JOptionPane.showMessageDialog(this, "Congratulations! You won in " + moves + " moves!");
        resetGame();
    }

    private void resetGame() {
        for (int i = 0; i < cardButtons.length; i++) {
            cardButtons[i].setIcon(new ImageIcon("blank.png"));
        }
        firstCardIndex = -1;
        secondCardIndex = -1;
        moves = 0;
        numberOfMatches = 0;
        Collections.shuffle(imagePaths);
    }

    public static void main(String[] args) {
        int gridSize = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of the GUI matrix (12 or 24 cards):"));
        if (gridSize != 12 && gridSize != 24) {
            JOptionPane.showMessageDialog(null, "Invalid grid size. Please enter 12 or 24.");
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> new MemoryGame(gridSize).setVisible(true));
    }
}
