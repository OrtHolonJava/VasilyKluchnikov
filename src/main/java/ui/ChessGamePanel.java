package ui;

import boardgame.BoardPosition;
import boardgame.Chess;
import boardgame.GameResult;
import bots.ChessBot;
import configuration.GameConfigurationReader;
import configuration.OptionsConfigurationReader;
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
import java.util.Collection;

/**
 * Created by divided on 13.04.2018.
 */
public class ChessGamePanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = new Color(122, 145, 182);
    private static final String IMAGE_EXTENSION = ".png";
    private static final String IMAGES_PATH = ChessFrame.DIR_PATH + "\\src\\main\\resources\\pieceImages\\";
    private static final int NUMBER_OF_BUTTONS = 3;

    private static final double BOT_DRAW_ACCEPT_EVALUATION_THRESHOLD = -2;

    private static final Color SELECTED_HIGHLIGHT_COLOR = new Color(240, 238, 22);
    private static final Color LAST_MOVE_HIGHLIGHT_COLOR = new Color(202, 199, 38);
    private static final Color POSSIBLE_POSITION_HIGHLIGHT_COLOR = new Color(99, 229, 125);
    private static final Color CHECK_HIGHLIGHT_COLOR = Color.RED;

    private ChessFrame chessFrameContainer;

    private Chess chessGame;
    private ChessBot chessBot;
    private Player botPlayer;

    private Color lightTileColor, darkTileColor;
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

    public ChessGamePanel(ChessFrame chessFrameContainer)
    {
        super();
        setChessFrameContainer(chessFrameContainer);
        setBackground(BACKGROUND_COLOR);
        setSize(OptionsConfigurationReader.getAppResolution());

        initializeGameSettings();
        initializeBoard();

        revalidate();
        repaint();

        setListeningToUser(false);
    }

    public void startGame()
    {
        if(isBotPlaying() && getChessState().getPlayerToMove() == getBotPlayer())
        {
            makeBotMove();
        }
        else
        {
            setListeningToUser(true);
        }
    }

    private void initializeGameSettings()
    {
        setChessGame(GameConfigurationReader.getChessGame());
        setChessBot(GameConfigurationReader.getChessBot());
        setBotPlayer(GameConfigurationReader.getBotPlayer());
        setBotDepth(GameConfigurationReader.getBotSearchDepth());
    }

    private void initializeBoard()
    {
        setLightTileColor(OptionsConfigurationReader.getLightTileColor());
        setDarkTileColor(OptionsConfigurationReader.getDarkTileColor());
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

        GridLayout buttonLayout = new GridLayout(NUMBER_OF_BUTTONS, 1);
        buttonLayout.setVgap(getButtonsGap());
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

    private void askToQuitGame()
    {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to quit?","Quit game", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            getChessFrameContainer().openMainMenu();
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
            Player winner;
            if(isBotPlaying())
            {
                winner = getBotPlayer();
            }
            else
            {
                winner = Player.getOppositePlayer(getChessState().getPlayerToMove());
            }
            
            updateGameOnResult(new GameResult(true, winner));
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
                        if(isListeningToUser())
                        {
                            tilePressed(tile.getBoardPosition());

                            if(isBotPlaying() && getChessState().getPlayerToMove() == getBotPlayer())
                            {
                                makeBotMove();
                            }

                        }
                    }
                });
                tile.setBackground(getDefaultTileColorByPosition(new BoardPosition(x, y)));
                setTileByPosition(tile, x, y);
                getBoardPanel().add(tile);
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
                possiblePositionsForSelection = getChessState().getAllValidPositionsForPiece(selectedPosition);
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
                updateBoard();
                updateGameOnResult(getGameResult());
                updateLastMoveHighlight(startMovePosition, clickedTilePosition);
            }
        }
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

    private int getButtonsGap()
    {
        return (int)getSize().getHeight() / getBoard().length;
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
        getTileByPosition(selectedPosition).setBackground(getDefaultTileColorByPosition(selectedPosition));

        for(BoardPosition position : possiblePositionsForSelection)
        {
            getTileByPosition(position).setBackground(getDefaultTileColorByPosition(position));
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
        GameResult gameResult;
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
                    tile.setBackground(getDefaultTileColorByPosition(new BoardPosition(x, y)));
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

    private Color getDefaultTileColorByPosition(BoardPosition tilePosition)
    {
        if((tilePosition.getX() + tilePosition.getY() + 1) % 2 == 0)
        {
            return getLightTileColor();
        }
        else
        {
            return getDarkTileColor();
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
    private void outputWinner(GameResult gameResult)
    {
        String winnerOutput;
        if(gameResult.getWinner() == null)
        {
            winnerOutput = "Game has ended in a draw!";
        }
        else
        {
            winnerOutput = "Winner: " + gameResult.getWinner().name();
        }

        JOptionPane.showMessageDialog(null, winnerOutput, "Game Result" , JOptionPane.INFORMATION_MESSAGE);

        askForRematch();
    }

    private void askForRematch()
    {
        Object[] options = { "Rematch", "Quit To Menu" };
        int dialogResult = JOptionPane.showOptionDialog(null,
                "Would you like to rematch, or to quit to menu?",
                "Rematch suggestion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if(dialogResult == 0)
        {
            startRematch();
        }
        else
        {
            getChessFrameContainer().openMainMenu();
        }
    }

    private void startRematch()
    {
        initializeGameSettings();
        startGame();
        setAllTilesToDefaultColor();
        updateBoard();
    }

    private void setAllTilesToDefaultColor()
    {
        for(int x = 0; x < getTiles().length; x++)
        {
            for(int y = 0; y < getTiles()[0].length; y++)
            {
                BoardTile tile = getTiles()[x][y];
                tile.setBackground(getDefaultTileColorByPosition(new BoardPosition(x, y)));
            }
        }
    }


    /**
     * scale image
     *
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @return scaled image
     */
    private static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
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

    public ChessFrame getChessFrameContainer()
    {
        return chessFrameContainer;
    }

    public void setChessFrameContainer(ChessFrame chessFrameContainer)
    {
        this.chessFrameContainer = chessFrameContainer;
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

    private Color getLightTileColor()
    {
        return lightTileColor;
    }

    private void setLightTileColor(Color lightTileColor)
    {
        this.lightTileColor = lightTileColor;
    }

    private Color getDarkTileColor()
    {
        return darkTileColor;
    }

    private void setDarkTileColor(Color darkTileColor)
    {
        this.darkTileColor = darkTileColor;
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
}
