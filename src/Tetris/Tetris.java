package Tetris;

import javax.swing.*;

public class Tetris extends JFrame {

    private Board board;

    public Tetris() {
        initUI();
    }

    private void initUI() {
        board = new Board();
        add(board);
        
        setTitle("Tetris");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
            game.board.start();
        });
    }
}