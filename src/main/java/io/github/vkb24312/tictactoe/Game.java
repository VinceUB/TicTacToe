package io.github.vkb24312.tictactoe;

import io.github.vkb24312.log.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private Board board = new Board();
    private Player[] players = new Player[]{new Player('X'), new Player('O')};
    static Log log = new Log();
    private Scanner userInput = new Scanner(System.in);

    public static void main(String args[]){
        Game game = new Game();
        game.mainMethod();
    }

    private int mainMethod(){
        int winner = -1;
        while(winner<0){
            for (int i = 0; i < players.length; i++) {
                print(board.printBoard());
                board.isWinForPlayer(players[i]);
                Move move = null;
                boolean isNull;

                //<editor-fold desc="Get move from player and register it">
                do{
                    try {
                        isNull = false;
                        move = players[i].chooseMove(board, userInput);
                        board.registerMove(move);
                    } catch (NullPointerException e) {
                        isNull = true;
                    }
                } while(isNull);

                board.moves.add(move);
                //</editor-fold>

                if(board.isWinForPlayer(players[i])){
                    winner = i;
                    finishGame(winner);
                    break;
                } else if(board.isDraw()) {
                    finishGame(-1);
                    break;
                }
            }
        }
        userInput.close();
        log.log(board.moves.toString());
        return winner;
    }

    private void finishGame(int winner){
        print(board.printBoard());
        if(winner<0){
            print("Draw");
            log.log("Game finished in draw");
        } else {
            print("Player " + winner + " has won! (Mark '" + players[winner].mark + "')");
            log.log("Game finished with winner " + winner);
        }
    }

    static void print(String s){
        System.out.println(s);
    }
}

class Player {
    Player(char mark){
        this.mark = mark;
    }

    char mark;

    Move chooseMove(Board board, Scanner s){
        Game.print("Please choose a position");
        String move = s.next();
        Game.log.log("Player chosen move " + move);

        int moveNum = Move.stringToMove(move);

        Move out = Move.moveParser(moveNum, this);

        for (int i : out.move) {
            if (i > 2) return null;
        }

        try {
            if (board.isValid(out)) {
                Game.log.log("Chosen move " + move);
                return out;
            } else {
                Game.log.log("Chosen move not valid");
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            Game.log.log("Chosen move not valid. \nStack trace:\n" + stackTrace);
            return null;
        }
    }
}

class Board {
    ArrayList<Move> moves = new ArrayList<>();

    Board(){
        board = new Player[3][3];

        for (int i = 0; i < board.length; i++) for (int j = 0; j < board[i].length; j++) board[i][j] = blankPlayer;
    }

    Board(Player[][] board){
        if(board.length!=3) throw new IllegalArgumentException();

        for (Player[] aBoard : board) {
            if (aBoard.length != 3) throw new IllegalArgumentException();
        }

        this.board = board;
    }

    private Player[][] board;

    String printBoard(){
        StringBuilder out = new StringBuilder(new String(new char[]{'\u0000'}));
        for (Player[] aBoard : board) {
            for (Player anABoard : aBoard) {
                out.append(anABoard.mark).append(" ");
            }
            out.append("\n");
        }

        return out.toString();
    }

    void registerMove(Move move){
        board[move.move[0]][move.move[1]] = move.player;
        Game.log.log("Registered move " + Arrays.toString(move.move) + "\nNew map:\n" + this.printBoard());
    }

    boolean isWinForPlayer(Player p) {
        for (Player[] aBoard : board) {
            if (aBoard[0].equals(p) & aBoard[1].equals(p) & aBoard[2].equals(p)) return true;
        }

        for (int i = 0; i < board.length; i++) {
            if (board[0][i].equals(p) & board[1][i].equals(p) & board[2][i].equals(p)) return true;
        }

        if (board[0][0].equals(p) & board[1][1].equals(p) & board[2][2].equals(p)) return true;
        return board[0][2].equals(p) & board[1][1].equals(p) & board[2][0].equals(p);

    }
    boolean isDraw(){
        return moves.size()>8;
    }

    boolean isValid(Move move){
        return board[move.move[0]][move.move[1]].equals(blankPlayer);
    }

    private Player blankPlayer = new Player('.');

}

class Move {
    private Move(int moveX, int moveY, Player p){
        move = new int[]{moveX, moveY};
        this.player = p;
    }

    Move(int[] move, Player p){
        this.move = move;
        player = p;
    }

    Player player;
    int[] move;

    static int stringToMove(String move){
        char moveChar = move.toCharArray()[0];

        switch(moveChar){
            case 'q':
                move = "0";
                break;
            case 'w':
                move = "1";
                break;
            case 'e':
                move = "2";
                break;
            case 'a':
                move = "10";
                break;
            case 's':
                move = "11";
                break;
            case 'd':
                move = "12";
                break;
            case 'z':
                move = "20";
                break;
            case 'x':
                move = "21";
                break;
            case 'c':
                move = "22";
                break;
        }

        try{
            return Integer.parseInt(move);
        } catch(NumberFormatException e){
            return -1;
        }
    }

    static Move moveParser(int move, Player p){
        return new Move(move/10, move%10, p);
    }
}