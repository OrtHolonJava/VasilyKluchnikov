package ui;

import boardgame.Chess;
import bots.ChessBot;
import enums.Player;
import exceptions.BoardGameException;
import pieces.chessPieces.ChessPiece;

import javax.swing.*;
import java.awt.*;

/**
 * Created by divided on 17.04.2018.
 */
public class ChessFrame extends JFrame
{
    public static final String DIR_PATH = System.getProperty("user.dir");


    // TODO: 18.04.2018 All these constants should be gotten from the config options file
    public static final Dimension RESOLUTION = new Dimension(900, 600);
    public static final Color WHITE_TILE_COLOR = Color.WHITE;
    public static final Color BLACK_TILE_COLOR = Color.GRAY;
    public static final Chess CHESS_GAME = new Chess();

    public static final ChessBot CHESS_BOT = new ChessBot();
    public static final Player BOT_PLAYER = Player.BLACK;
    public static final int BOT_DEPTH = 3;

    private ChessGamePanel chessGamePanel;

    public ChessFrame()
    {
        setSize(RESOLUTION);

        // TODO: 18.04.2018 This part should be launched from a menu panel on pressing 'Start' depending on the options config
        ChessGamePanel chessGamePanel = new ChessGamePanel(CHESS_GAME, CHESS_BOT, BOT_PLAYER, BOT_DEPTH, RESOLUTION, WHITE_TILE_COLOR, BLACK_TILE_COLOR);
        setChessGamePanel(chessGamePanel);
        add(getChessGamePanel());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public ChessGamePanel getChessGamePanel()
    {
        return chessGamePanel;
    }

    public void setChessGamePanel(ChessGamePanel chessGamePanel)
    {
        this.chessGamePanel = chessGamePanel;
    }
}
