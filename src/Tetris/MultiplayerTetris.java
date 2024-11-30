package Tetris;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MultiplayerTetris extends JFrame {
    private Board playerOneBoard;
    private Board playerTwoBoard;

    public MultiplayerTetris() {
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(1, 2));

        playerOneBoard = new Board();
        playerTwoBoard = new Board();

        // Add custom key bindings for Player 1 (WASD + F)
        addPlayerOneKeyBindings();

        // Add custom key bindings for Player 2 (Arrow keys + Alt)
        addPlayerTwoKeyBindings();

        add(playerOneBoard);
        add(playerTwoBoard);
        
        setTitle("Multiplayer Tetris");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void addPlayerOneKeyBindings() {
        InputMap im = playerOneBoard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = playerOneBoard.getActionMap();

        // A - Move Left
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeft");
        // D - Move Right
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveRight");
        // W - Rotate Right
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "rotateRight");
        // S - Move Down
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "oneLineDown");
        // F - Rotate Left
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "rotateLeft");
        // Space - Drop Down
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "dropDown");

        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.tryMoveExternal(playerOneBoard.getCurPiece(), 
                    playerOneBoard.getCurX() - 1, playerOneBoard.getCurY());
            }
        });

        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.tryMoveExternal(playerOneBoard.getCurPiece(), 
                    playerOneBoard.getCurX() + 1, playerOneBoard.getCurY());
            }
        });

        am.put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.tryMoveExternal(playerOneBoard.getCurPiece().rotateRight(), 
                    playerOneBoard.getCurX(), playerOneBoard.getCurY());
            }
        });

        am.put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.tryMoveExternal(playerOneBoard.getCurPiece().rotateLeft(), 
                    playerOneBoard.getCurX(), playerOneBoard.getCurY());
            }
        });

        am.put("oneLineDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.oneLineDownExternal();
            }
        });

        am.put("dropDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOneBoard.dropDownExternal();
            }
        });
    }

    private void addPlayerTwoKeyBindings() {
        InputMap im = playerTwoBoard.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = playerTwoBoard.getActionMap();

        // Left Arrow - Move Left
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        // Right Arrow - Move Right
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        // Up Arrow - Rotate Right
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "rotateRight");
        // Down Arrow - Move Down
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "oneLineDown");
        // Alt - Rotate Left
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0), "rotateLeft");
        // Ctrl - Drop Down
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0), "dropDown");

        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.tryMoveExternal(playerTwoBoard.getCurPiece(), 
                    playerTwoBoard.getCurX() - 1, playerTwoBoard.getCurY());
            }
        });

        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.tryMoveExternal(playerTwoBoard.getCurPiece(), 
                    playerTwoBoard.getCurX() + 1, playerTwoBoard.getCurY());
            }
        });

        am.put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.tryMoveExternal(playerTwoBoard.getCurPiece().rotateRight(), 
                    playerTwoBoard.getCurX(), playerTwoBoard.getCurY());
            }
        });

        am.put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.tryMoveExternal(playerTwoBoard.getCurPiece().rotateLeft(), 
                    playerTwoBoard.getCurX(), playerTwoBoard.getCurY());
            }
        });

        am.put("oneLineDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.oneLineDownExternal();
            }
        });

        am.put("dropDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerTwoBoard.dropDownExternal();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MultiplayerTetris game = new MultiplayerTetris();
            game.setVisible(true);
            game.playerOneBoard.start();
            game.playerTwoBoard.start();
        });
    }
}