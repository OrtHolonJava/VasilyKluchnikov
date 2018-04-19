package ui;

import boardgame.BoardPosition;
import boardgame.Chess;
import boardgame.GameResult;
import bots.BoardGameBot;
import bots.ChessBot;
import enums.Player;
import exceptions.BoardGameException;
import exceptions.botExceptions.BotEvaluateException;
import exceptions.botExceptions.BotMoveSearchException;
import gameStates.ChessState;
import pieces.chessPieces.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by divided on 13.04.2018.
 */
public class ChessGamePanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = new Color(122, 145, 182);
    private static final String IMAGE_EXTENSION = ".png";
    private static final String IMAGES_PATH = ChessFrame.DIR_PATH + "\\src\\main\\resources\\pieceImages\\";
    private static final double BOT_DRAW_ACCEPT_EVALUATION_THRESHOLD = -3;

    private static final Color SELECTED_HIGHLIGHT_COLOR = new Color(240, 238, 22);
    private static final Color LAST_MOVE_HIGHLIGHT_COLOR = new Color(202, 199, 38);
    private static final Color POSSIBLE_POSITION_HIGHLIGHT_COLOR = new Color(99, 229, 125);
    private static final Color CHECK_HIGHLIGHT_COLOR = Color.RED;

    private Chess chessGame;
    private ChessBot chessBot;
    private Player botPlayer;

    private Color firstTileColor, secondTileColor;
    private BoardTile tiles[][];
    private JPanel boardPanel;
    private int botDepth;
    private int tileSize;
    private boolean isListeningToUser;

    private JPanel buttonsPanel;
    private GameButton resignButton;
    private GameButton offerDrawButton;
    private GameButton quitButton;

    private BoardPosition selectedPosition;
    private Collection<BoardPosition> possiblePositionsForSelection;

    private static Map<String, Character> pieceNameToCharMap;
    static
    {
        pieceNameToCharMap = new HashMap<String, Character>();
        pieceNameToCharMap.put(Pawn.class.getSimpleName(), 'P');
        pieceNameToCharMap.put(Knight.class.getSimpleName(), 'N');
        pieceNameToCharMap.put(Bishop.class.getSimpleName(), 'B');
        pieceNameToCharMap.put(Rook.class.getSimpleName(), 'R');
        pieceNameToCharMap.put(Queen.class.getSimpleName(), 'Q');
        pieceNameToCharMap.put(King.class.getSimpleName(), 'K');
    }

    /*
        Initializes the game for Player vs Bot game
     */
    public ChessGamePanel(Chess chessGame, ChessBot chessBot, Player botPlayer, int botDepth, Dimension panelSize, Color firstTileColor, Color secondTileColor)
    {
        setListeningToUser(true);
        setBackground(BACKGROUND_COLOR);

        setChessGame(chessGame);
        setChessBot(chessBot);
        setBotPlayer(botPlayer);
        setBotDepth(botDepth);
        setSize(panelSize);
        setFirstTileColor(firstTileColor);
        setSecondTileColor(secondTileColor);

        initializeBoard();
    }

    /*
        Initializes the game for Player vs. Player game
     */
    public ChessGamePanel(Chess chessGame, Dimension panelSize, Color firstTileColor, Color secondTileColor)
    {
        this(chessGame, null, null, 0, panelSize, firstTileColor, secondTileColor);
    }

    private void initializeBoard()
    {
        setBoardPanel(new JPanel());
        initializeButtons();
        scaleBoard();
        getBoardPanel().setLayout(new GridLayout(getBoard().length, getBoard()[0].length));
        getBoardPanel().setBackground(BACKGROUND_COLOR);
        add(getBoardPanel(), BorderLayout.CENTER);
        initializeTiles();
        updateBoard();
    }

    /*
        Initializes all of the game buttons
     */
    private void initializeButtons()
    {
        setButtonsPanel(new JPanel());
        getButtonsPanel().setBackground(BACKGROUND_COLOR);

        GridLayout buttonLayout = new GridLayout(3, 1);
        int gapSize = (int)getSize().getHeight() / getBoard().length;
        buttonLayout.setVgap(gapSize);
        getButtonsPanel().setLayout(buttonLayout);

        setResignButton(new GameButton("Resign"));
        getResignButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                askForResign();

            }
        });
        setOfferDrawButton(new GameButton("Offer Draw"));
        getOfferDrawButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                askForDraw();
            }
        });
        setQuitButton(new GameButton("Quit"));
        getQuitButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                askToQuitGame();
            }
        });
        getButtonsPanel().add(getResignButton());
        getButtonsPanel().add(getOfferDrawButton());
        getButtonsPanel().add(getQuitButton());
        add(getButtonsPanel(), BorderLayout.WEST);
    }

    private void quitToMenu()
    {
        // TODO: 19.04.2018 Finish this function
    }

    private void askToQuitGame()
    {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to draw?","Draw", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            quitToMenu();
        }
    }

    private void askForDraw()
    {
        boolean isDrawAccepted = false;
        if(isBotPlaying())
        {
           isDrawAccepted = doesBotAcceptDraw();
        }
        else
        {
            int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to draw?","Draw", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                isDrawAccepted = true;
            }
        }

        if(isDrawAccepted)
        {
            updateGameOnResult(new GameResult(true, null));
        }
        else
        {
            JOptionPane.showMessageDialog(null, "A draw offer has been rejected", "Draw", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean doesBotAcceptDraw()
    {
        double botEvaluation;
        try
        {
            botEvaluation = getChessBot().evaluate(getChessState());
        }
        catch (BotEvaluateException e)
        {
            e.printStackTrace();
            System.out.println("Failed to evaluate current position for a draw");
            return false;
        }

        if(getBotPlayer() == Player.BLACK)
        {
            botEvaluation *= -1;
        }

        return botEvaluation < BOT_DRAW_ACCEPT_EVALUATION_THRESHOLD;
    }

    private void askForResign()
    {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to resign?","Resign", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            updateGameOnResult(new GameResult(true, Player.getOppositePlayer(getChessState().getPlayerToMove())));
        }

    }

    private void initializeTiles()
    {
        setTiles(new BoardTile[getBoard().length][getBoard()[0].length]);
        for(int x = 0; x < getBoard().length; x++)
        {
            for(int y = 0; y < getBoard()[0].length; y++)
            {
                BoardTile tile = new BoardTile(new BoardPosition(x, y));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(isListeningToUser)
                        {
                            tilePressed(tile.getBoardPosition());

                            if(isBotPlaying() && getChessState().getPlayerToMove() == getBotPlayer())
                            {
                                makeBotMove();
                            }

                        }
                    }
                });
                tile.setBackground(getTileColorByPosition(new BoardPosition(x, y)));
                setTileByPosition(tile, x, y);
                getBoardPanel().add(tile);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for(int x = 0; x < getTiles().length; x++)
        {
            for(int y = 0; y < getTiles()[0].length; y++)
            {
                getTiles()[x][y].revalidate();
                getTiles()[x][y].repaint();
            }
        }
    }

    private void tilePressed(BoardPosition clickedTilePosition)
    {
        if(selectedPosition == null && pieceCanBeMoved(clickedTilePosition))
        {
            try
            {
                selectedPosition = clickedTilePosition;
                possiblePositionsForSelection = getAllPossiblePositionsForPiece(selectedPosition);
            }
            catch (BoardGameException e)
            {
                e.printStackTrace();
                System.out.println("Error getting possible positions for a piece");
            }

            addTileSelections();
        }
        else if(selectedPosition != null )
        {
            BoardPosition startMovePosition = new BoardPosition(selectedPosition);
            boolean moveWasMade = false;
            try
            {
                if(possiblePositionsForSelection.contains(clickedTilePosition))
                {
                    getChessGame().makeMove(getChessState().getStateAfterMove(startMovePosition, clickedTilePosition));
                    moveWasMade = true;
                }
            }
            catch (BoardGameException e)
            {
                System.out.println("Error in making a move");
            }

            removeTileSelections();
            updateKingCheckHighlight();

            if(moveWasMade)
            {
                updateGameOnResult(getGameResult());
                updateLastMoveHighlight(startMovePosition, clickedTilePosition);
            }
        }
        revalidate();
        updateUI();
        repaint();
    }

    private void makeBotMove()
    {
        setListeningToUser(false);

        ChessState currentState = getChessState(), newState = null;
        try
        {
            newState = (ChessState)getChessBot().findBestNextState(currentState, getBotDepth());
        }
        catch (BotMoveSearchException e)
        {
            e.printStackTrace();
            System.out.println("Failure to get a move from the bot");
        }
        getChessGame().makeMove(newState);

        updateBoard();
        updateKingCheckHighlight();

        //updateHighlightsOnChangesInStates((ChessPiece[][]) currentState.getBoard(), (ChessPiece[][])newState.getBoard());
        updateGameOnResult(getGameResult());
        setListeningToUser(true);
    }

    private void updateGameOnResult(GameResult gameResult)
    {
        if(gameResult.isGameFinished())
        {
            setListeningToUser(false);
            outputWinner(gameResult);
        }
    }

    private void updateHighlightsOnChangesInStates(ChessPiece[][] oldBoard, ChessPiece[][] newBoard)
    {
        for(int x = 0; x < oldBoard.length; x++)
        {
            for(int y = 0; y < oldBoard[0].length; y++)
            {
                BoardTile tile = getTileByPosition(x, y);
                ChessPiece oldPiece = oldBoard[x][y], newPiece = newBoard[x][y];
                if((oldPiece == null && newPiece != null) || (oldPiece != null && newPiece == null)
                        || !oldBoard[x][y].equals(newBoard[x][y]))
                {
                    tile.setBackground(LAST_MOVE_HIGHLIGHT_COLOR);
                }
            }
        }
    }

    private void addTileSelections()
    {
        getTileByPosition(selectedPosition).setBackground(SELECTED_HIGHLIGHT_COLOR);
        for(BoardPosition position : possiblePositionsForSelection)
        {
            getTileByPosition(position).setBackground(POSSIBLE_POSITION_HIGHLIGHT_COLOR);
        }
    }

    private void removeTileSelections()
    {
        getTileByPosition(selectedPosition).setBackground(getTileColorByPosition(selectedPosition));

        for(BoardPosition position : possiblePositionsForSelection)
        {
            getTileByPosition(position).setBackground(getTileColorByPosition(position));
        }

        selectedPosition = null;
        possiblePositionsForSelection = null;
    }

    private void updateLastMoveHighlight(BoardPosition positionMovedFrom, BoardPosition positionMovedTo)
    {
        replaceBackgroundColorToDefault(LAST_MOVE_HIGHLIGHT_COLOR);
        getTileByPosition(positionMovedFrom).setBackground(LAST_MOVE_HIGHLIGHT_COLOR);
        getTileByPosition(positionMovedTo).setBackground(LAST_MOVE_HIGHLIGHT_COLOR);
    }

    private GameResult getGameResult()
    {
        GameResult gameResult = null;
        try
        {
            gameResult = getChessGame().getGameResult();
        }
        catch (BoardGameException e)
        {
            e.printStackTrace();
            System.out.println("Failure to get game result");
            return new GameResult(false, null);
        }
        return gameResult;
    }

    private void replaceBackgroundColorToDefault(Color colorToReplace)
    {
        for(int x = 0; x < getTiles().length; x++)
        {
            for(int y = 0; y < getTiles()[0].length; y++)
            {
                BoardTile tile = getTiles()[x][y];
                if(tile.getBackground() == colorToReplace)
                {
                    tile.setBackground(getTileColorByPosition(new BoardPosition(x, y)));
                }
            }
        }
    }

    private void updateKingCheckHighlight()
    {
        replaceBackgroundColorToDefault(CHECK_HIGHLIGHT_COLOR);
        try
        {
            Player currentPlayer = getChessState().getPlayerToMove();
            boolean isKingUnderCheck = getChessState().kingIsUnderCheck(currentPlayer);
            if (isKingUnderCheck)
            {
                BoardPosition kingPosition = getChessState().getKingPosition(currentPlayer);
                getTiles()[kingPosition.getX()][kingPosition.getY()].setBackground(CHECK_HIGHLIGHT_COLOR);
            }
        }
        catch (BoardGameException e)
        {
            e.printStackTrace();
            System.out.println("Failed to update king check highlight");
        }
    }

    private Color getTileColorByPosition(BoardPosition tilePosition)
    {
        if((tilePosition.getX() + tilePosition.getY() + 1) % 2 == 0)
        {
            return getFirstTileColor();
        }
        else
        {
            return getSecondTileColor();
        }
    }

    private boolean pieceCanBeMoved(BoardPosition piecePosition)
    {
        ChessPiece piece = getBoard()[piecePosition.getX()][piecePosition.getY()];
        return piece != null &&
                piece.getPlayer() == getChessState().getPlayerToMove();
    }

    private boolean isBotPlaying()
    {
        return getChessBot() != null;
    }

    // TODO: 18.04.2018 This entire function is possible a code duplicate from ChessState inside the getPossibleStates function, need to make 1 filter moves function probably
    private Collection<BoardPosition> getAllPossiblePositionsForPiece(BoardPosition piecePosition) throws BoardGameException
    {
        Collection<BoardPosition> possiblePositionsForPiece = new ArrayList<>();
        ChessPiece piece = getBoard()[piecePosition.getX()][piecePosition.getY()];
        if(piece != null && piece.getPlayer() == getChessState().getPlayerToMove())
        {
            Collection<BoardPosition> uncheckedPositionsForPiece = getChessState().getPossiblePositionsForPiece(piecePosition);
            for(BoardPosition uncheckedPosition : uncheckedPositionsForPiece)
            {
                ChessState newState = getChessState().getStateAfterMove(piecePosition, uncheckedPosition);
                if(getChessState().isMoveLegal(newState, piecePosition, uncheckedPosition))
                {
                    possiblePositionsForPiece.add(uncheckedPosition);
                }
            }
        }

        return possiblePositionsForPiece;
    }

    private void scaleBoard()
    {
        Dimension currentSize = getSize();

        int boardWidth = (int)(currentSize.getWidth() * 0.7);
        int boardHeight = (int)(currentSize.getHeight() * 0.9);

        if(boardHeight != boardWidth)
        {
            int min = Math.min(boardHeight, boardWidth);
            boardWidth = min;
            boardHeight = min;
        }
        getBoardPanel().setPreferredSize(new Dimension(boardWidth, boardHeight));
        tileSize = boardWidth / (getBoard().length + 1);
    }

    private void updateBoard()
    {
        for(int x = 0; x < getBoard().length; x++)
        {
            for(int y = 0; y < getBoard()[0].length; y++)
            {
                ChessPiece piece = getBoard()[x][y];
                BoardTile tile = getTileByPosition(x, y);
                if(piece != tile.getPiece())
                {
                    try
                    {
                        updateTileWithPiece(piece, getTileByPosition(x, y));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        System.out.println("Failed to update the board");
                        return;
                    }
                }
            }
        }
    }

    private void updateTileWithPiece(ChessPiece piece, BoardTile tile) throws IOException
    {
        tile.setPiece(piece);

        if(piece == null)
        {
            tile.getImageLabel().setIcon(null);
            return;
        }

        String imagePath = IMAGES_PATH +
                piece.getPlayer().name().toLowerCase() + piece.getClass().getSimpleName() +
                IMAGE_EXTENSION;


        BufferedImage pieceImage = ImageIO.read(new File(imagePath));
        pieceImage = scale(pieceImage, tileSize, tileSize);
        tile.getImageLabel().setIcon(new ImageIcon(pieceImage));
    }

    /*
      Outputs the winner of the game accordingly to the game result
   */
    private static void outputWinner(GameResult gameResult)
    {
        String outputString;
        if(gameResult.getWinner() == null)
        {
            outputString = "Game has ended in a draw!";
        }
        else
        {
            outputString = "Winner: " + gameResult.getWinner().name();
        }

        JOptionPane.showMessageDialog(null, outputString, "Game Result" , JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * scale image
     *
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
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
            ChessGamePanel.textuallyDisplayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
            ChessState newState = GameInputGetter.getChessStateInputFromUser((ChessState) getChessGame().getCurrentState());
            getChessGame().makeMove(newState);
            result = getChessGame().getGameResult();
        }
        ChessGamePanel.textuallyDisplayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
        ChessGamePanel.outputWinner(result);
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
            ChessGamePanel.textuallyDisplayBoard((ChessPiece[][]) getChessGame().getCurrentState().getBoard());
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
        ChessGamePanel.textuallyDisplayBoard((ChessPiece[][]) chessGame.getCurrentState().getBoard());
        ChessGamePanel.outputWinner(result);
    }

    private JPanel getButtonsPanel()
    {
        return buttonsPanel;
    }

    private void setButtonsPanel(JPanel buttonsPanel)
    {
        this.buttonsPanel = buttonsPanel;
    }

    private JButton getOfferDrawButton()
    {
        return offerDrawButton;
    }

    private void setOfferDrawButton(GameButton offerDrawButton)
    {
        this.offerDrawButton = offerDrawButton;
    }

    private GameButton getQuitButton()
    {
        return quitButton;
    }

    private void setQuitButton(GameButton quitButton)
    {
        this.quitButton = quitButton;
    }

    private GameButton getResignButton()
    {
        return resignButton;
    }

    private void setResignButton(GameButton resignButton)
    {
        this.resignButton = resignButton;
    }

    private Player getBotPlayer()
    {
        return botPlayer;
    }

    private void setBotPlayer(Player botPlayer)
    {
        this.botPlayer = botPlayer;
    }

    private boolean isListeningToUser()
    {
        return isListeningToUser;
    }

    private void setListeningToUser(boolean listeningToUser)
    {
        isListeningToUser = listeningToUser;
    }

    private ChessState getChessState()
    {
        return (ChessState)getChessGame().getCurrentState();
    }

    private ChessPiece[][] getBoard()
    {
        return (ChessPiece[][])getChessGame().getCurrentState().getBoard();
    }

    private Chess getChessGame()
    {
        return chessGame;
    }

    private void setChessGame(Chess chessGame)
    {
        this.chessGame = chessGame;
    }

    private BoardTile getTileByPosition(BoardPosition position)
    {
        return getTileByPosition(position.getX(), position.getY());
    }

    private BoardTile getTileByPosition(int x, int y)
    {
        return getTiles()[x][y];
    }

    private void setTileByPosition(BoardTile tilePanel, BoardPosition position)
    {
        setTileByPosition(tilePanel, getX(), getY());
    }

    private JPanel getBoardPanel()
    {
        return boardPanel;
    }

    private void setTileByPosition(BoardTile tilePanel, int x, int y)
    {
        getTiles()[x][y] = tilePanel;
    }

    private BoardTile[][] getTiles()
    {
        return tiles;
    }

    private void setTiles(BoardTile[][] tiles)
    {
        this.tiles = tiles;
    }

    private Color getFirstTileColor()
    {
        return firstTileColor;
    }

    private void setFirstTileColor(Color firstTileColor)
    {
        this.firstTileColor = firstTileColor;
    }

    private Color getSecondTileColor()
    {
        return secondTileColor;
    }

    private void setSecondTileColor(Color secondTileColor)
    {
        this.secondTileColor = secondTileColor;
    }

    private void setBoardPanel(JPanel boardPanel)
    {
        this.boardPanel = boardPanel;
    }

    private ChessBot getChessBot()
    {
        return chessBot;
    }

    private void setChessBot(ChessBot chessBot)
    {
        this.chessBot = chessBot;
    }

    private int getBotDepth()
    {
        return botDepth;
    }

    private void setBotDepth(int botDepth)
    {
        this.botDepth = botDepth;
    }

    /*
                            Textually outputs the current chess board
                        */
    public static void textuallyDisplayBoard(ChessPiece[][] board)
    {
        System.out.println();
        for (int j = 0; j < board[0].length; j++)
        {
            System.out.printf("%5d", j);
        }
        System.out.println();

        for(int x = 0; x < board.length; x++)
        {
            for(int y = 0; y < board[0].length; y++)
            {
                char charToPrint = getCharRepresentationFromPiece(board[x][y]);
                System.out.printf("%5s", charToPrint);
            }
            System.out.printf("%5d\n", x);
        }
        System.out.println();
    }

    /*
            Gets char representation for the chess piece
    */
    private static char getCharRepresentationFromPiece(ChessPiece piece)
    {
        if(piece == null)
            return '-';

        Character ch = pieceNameToCharMap.get(piece.getClass().getSimpleName());

        if(piece.getPlayer() == Player.BLACK)
        {
            ch = Character.toLowerCase(ch);
        }
        return ch;
    }
}
