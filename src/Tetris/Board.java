package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int PERIOD_INTERVAL = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = true;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private Shape curPiece;
    private Shape nextPiece;
    private Shape.Tetrominoes[] board;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setFocusable(true);
        curPiece = new Shape();
        nextPiece = new Shape();
        timer = new Timer(PERIOD_INTERVAL, this);
        timer.start();

        board = new Shape.Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        initKeyBindings();
    }

    private void initKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "rotateRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0), "rotateLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "dropDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "oneLineDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");

        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    tryMove(curPiece, curX - 1, curY);
                }
            }
        });

        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    tryMove(curPiece, curX + 1, curY);
                }
            }
        });

        am.put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    tryMove(curPiece.rotateRight(), curX, curY);
                }
            }
        });

        am.put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    tryMove(curPiece.rotateLeft(), curX, curY);
                }
            }
        });

        am.put("dropDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    dropDown();
                }
            }
        });

        am.put("oneLineDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStarted && !isPaused) {
                    oneLineDown();
                }
            }
        });

        am.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });
    }

    public void start() {
        if (isPaused) {
            return;
        }

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }

    private void pause() {
        if (!isStarted) {
            return;
        }

        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i) {
            board[i] = Shape.Tetrominoes.NoShape;
        }
    }

    private void newPiece() {
        curPiece = nextPiece;
        nextPiece = new Shape();
        nextPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Shape.Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            // Game Over
            JOptionPane.showMessageDialog(this, "Game Over", "Tetris", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Shape.Tetrominoes.NoShape) {
                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; ++j) {
                if (shapeAt(j, i) == Shape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
                    for (int j = 0; j < BOARD_WIDTH; ++j) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            isFallingFinished = true;
            curPiece.setShape(Shape.Tetrominoes.NoShape);
            repaint();
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoes shape) {
        Color colors[] = {
            new Color(0, 0, 0), new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int j = 0; j < BOARD_WIDTH; ++j) {
                Shape.Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Shape.Tetrominoes.NoShape) {
                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != Shape.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + numLinesRemoved, 10, 15);
    }
}
