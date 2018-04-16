package ui;

import boardgame.Chess;
import boardgame.GameResult;
import bots.BoardGameBot;
import enums.Player;
import exceptions.BoardGameException;
import gameStates.ChessState;
import pieces.chessPieces.ChessPiece;

/**
 * Created by divided on 16.04.2018.
 */
public class ChessUI
{
    private Chess chessGame;

    public ChessUI()
    {
        setChessGame(new Chess());
    }

    /*
        Starts a player vs. player game and manages it
        Manages turn order, input and stops the game when its finished
     */
    public void playGame() throws BoardGameException
    {
        GameResult result = getChessGame().getGameResult();

        while(!result.isGameFinished())
        {
            ChessBoardUI.displayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
            ChessState newState = GameInputGetter.getChessStateInputFromUser((ChessState) getChessGame().getCurrentState());
            getChessGame().makeMove(newState);
            result = getChessGame().getGameResult();
        }
        ChessBoardUI.displayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
        ChessBoardUI.outputWinner(result);
    }

    /*
        Starts a player vs. bot game and manages it
        Manages turn order, input and stops the game when its finished
     */
    public void playBotGame(BoardGameBot bot, int searchDepth, Player player) throws BoardGameException
    {
        GameResult result = getChessGame().getGameResult();

        while(!result.isGameFinished())
        {
            ChessBoardUI.displayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
            ChessState newState;
            if(getChessGame().getCurrentState().getPlayerToMove() == player)
            {
                newState = GameInputGetter.getChessStateInputFromUser((ChessState)chessGame.getCurrentState());
            }
            else
            {
                ChessState currentState = (ChessState)getChessGame().getCurrentState();
                newState = (ChessState) bot.findBestNextState(currentState, searchDepth);
            }
            getChessGame().makeMove(newState);
            result = getChessGame().getGameResult();
        }
        ChessBoardUI.displayBoard((ChessPiece[][]) chessGame.getCurrentState().getBoard());
        ChessBoardUI.outputWinner(result);
    }

    private Chess getChessGame()
    {
        return chessGame;
    }

    private void setChessGame(Chess chessGame)
    {
        this.chessGame = chessGame;
    }
}
