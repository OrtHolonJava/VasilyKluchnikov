package ui;

import boardgame.BoardPosition;
import pieces.chessPieces.ChessPiece;

import javax.swing.*;
import java.awt.*;

/**
 * Created by divided on 17.04.2018.
 */
public class BoardTile extends JPanel
{
    private BoardPosition boardPosition;
    private ChessPiece piece;
    private JLabel imageLabel;
    
    public BoardTile(BoardPosition boardPosition)
    {
        super();
        setBoardPosition(boardPosition);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setImageLabel(new JLabel());
    }

    public ChessPiece getPiece()
    {
        return piece;
    }

    public void setPiece(ChessPiece piece)
    {
        this.piece = piece;
    }

    public JLabel getImageLabel()
    {
        return imageLabel;
    }

    private void setImageLabel(JLabel imageLabel)
    {
        this.imageLabel = imageLabel;
        add(imageLabel, BorderLayout.CENTER);
    }

    public BoardPosition getBoardPosition()
    {
        return boardPosition;
    }

    private void setBoardPosition(BoardPosition boardPosition)
    {
        this.boardPosition = boardPosition;
    }
}
