package io.github.vkb24312.tictactoe;

import io.github.vkb24312.log.Log;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends Frame {
    private Log log = new Log();
    private int playTime = 0;
    private int[][] board = new int[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private boolean turn(){
        return playTime % 2 == 0;
    }

    public static void main(String[] args){
        new Main().main();
    }

    private void main(){
        this.setVisible(true);
        this.setSize(300, 300);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.log(e.getPoint().toString());

                int mouseX = (e.getX() - (e.getX()%100))/100;
                int mouseY = (e.getY() - (e.getY()%100))/100;

                log.log(mouseX + " " + mouseY);

                if(board[mouseX][mouseY]==0){
                    if(turn()){
                        board[mouseX][mouseY] = 1;
                    } else {
                        board[mouseX][mouseY] = 2;
                    }

                    playTime++;
                }

                repaint();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==27 /*The esc key*/) close();
            }
        });
        this.setTitle("Tic Tac Toe");

        this.repaint();
        log.log("Repainted");
    }

    @Override
    public void paint(Graphics g){
        if (testWinner(board)) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (!turn()) {
                        board[i][j] = 1;
                    } else {
                        board[i][j] = 2;
                    }
                }
            }
            if(turn()) log.log("Finished with victory for player " + 1);
            else log.log("Finished with victory for player " + 2);

        } else if (playTime > 8) {
            finishGame();
            log.log("Finished in a draw");
        }

        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    g2d.setPaint(Color.LIGHT_GRAY);
                    g2d.setColor(Color.LIGHT_GRAY);
                } else if (board[i][j] == 1) {
                    g2d.setPaint(Color.BLUE);
                    g2d.setColor(Color.BLUE);
                } else {
                    g2d.setPaint(Color.RED);
                    g2d.setColor(Color.RED);
                }


                g2d.drawRect(i * 100 + 1, j * 100 + 1, 100 - 2, 100 - 2);
                g2d.fillRect(i * 100 + 1, j * 100 + 1, 100 - 2, 100 - 2);
            }
        }
    } //This works as my main method, and updates every time a button is clicked

    private void closeFrame(){
        finishGame();
    }
    private void finishGame() {
        log.log("Opening new game");
        board = new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        playTime = 0;
        repaint();
    }
    private void close() {
        log.log("User pressed key 'esc'. Exiting");
        System.exit(0);
        log.log("This log message shouldn't be possible");
    }

    private boolean testWinner(int[][] board){
        for (int i = 1; i < 3; i++) {
            for (int[] aBoard : board) {
                if (aBoard[0] == i & aBoard[1] == i & aBoard[2] == i) return true;
            }

            for (int j = 0; j < board.length; j++){
                if (board[0][j] == i & board[1][j] == i & board[2][j] == i) return true;
            }

            if(board[0][0] == i & board[1][1] == i & board[2][2] == i) return true;
            if(board[0][2] == i & board[1][1] == i & board[2][0] == i) return true;
        }
        return false;
    }
}

