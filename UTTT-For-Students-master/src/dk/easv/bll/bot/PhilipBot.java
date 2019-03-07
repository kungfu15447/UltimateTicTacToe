/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.easv.bll.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static dk.easv.bll.field.IField.AVAILABLE_FIELD;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

/**
 * @author Philip
 */
public class PhilipBot implements IBot {

    private String botName = "Terminator";
    private String[][] board;
    private String[][] macroBoard;
    private List<IMove> availableMoves;
    private IMove moveToDo;
    private int playerId;
    boolean setPlayerId = false;
    private final String[][] copyBoard = new String[9][9];
    private final String[][] copyMacro = new String[3][3];
    private final String[][] copyCopyBoard = new String[9][9];
    private final String[][] copyCopyMacro = new String[3][3];
    private final ArrayList<String[]> microBoards = new ArrayList<>();
    private int activeMicroBoard = 0;
    private String[] badMoves;
    private String[] otherBadMoves;

    @Override
    public IMove doMove(IGameState state) {

        setBadMoves();
        setOtherBadMoves();
        setMicroboardCoordinates();
        board = state.getField().getBoard();
        macroBoard = state.getField().getMacroboard();
        availableMoves = state.getField().getAvailableMoves();
        setPlayerId();

        boolean gotADrawMove = false;
        if (!checkIfItsPossibleToWin()) {
            // starting defense mode
            int otherPlayer;
            if (playerId == 1) {
                otherPlayer = 0;
            } else {
                otherPlayer = 1;
            }
            for (IMove x : availableMoves) {
                copyBoards();
                if (checkMove(x, otherPlayer)) {
                    moveToDo = x;
                    gotADrawMove = true;
                    break;
                }
            }
        }

        if (gotADrawMove == false) {
            calculateMove();
        }
        return moveToDo;

    }

    private void setPlayerId() {
        if (setPlayerId == true) {
            return;
        }

        int numberOfEmptyFields = 0;

        //Checks if the board is empty or not
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                if (board[i][k] == AVAILABLE_FIELD) {
                    numberOfEmptyFields++;
                }
            }
        }

        if (numberOfEmptyFields == 81) {
            playerId = 0;

        } else {
            playerId = 1;

        }
        setPlayerId = true;
    }

    private void calculateMove() {
        boolean foundValidMove = false;
        // Attack move
        List<IMove> attackMoves = new ArrayList<>();
        for (IMove x : availableMoves) {
            copyBoards();
            if (checkMove(x, playerId)) {
                foundValidMove = true;
                attackMoves.add(x);
            }
        }
        // Check if available attackmoves results in a macro win
        IMove winningMove = checkForAMacroWin(attackMoves);
        if (winningMove != null) {
            moveToDo = winningMove;
            return;
        }
        // if attackmoves has been found we here choose one here. We choose one which doesn't give a free line to the opponent next round.
        if (foundValidMove == true) {
            boolean bingo = findGoodMove(attackMoves);
            if (bingo == false) {
                Double rNum = Math.random() * attackMoves.size();
                int rNumInt = rNum.intValue();
                moveToDo = attackMoves.get(rNumInt);
            }
        }
        // Defense move
        if (foundValidMove == false) {
            int otherPlayer;
            if (playerId == 1) {
                otherPlayer = 0;
            } else {
                otherPlayer = 1;
            }
            for (IMove x : availableMoves) {
                copyBoards();
                if (checkMove(x, otherPlayer)) {
                    foundValidMove = true;
                    moveToDo = x;

                    break;
                }
            }
        }
        // Make sure the next move doesn't give the opponent a free line
        if (foundValidMove == false) {

            if (findGoodMove(availableMoves) == true) {
                foundValidMove = true;
            }
        }
        // Random - yet good - move
        if (foundValidMove == false) {
            setRandomMove();
        }
    }

    /**
     * Returns true if the given move gives a line to the specified player
     *
     * @param move
     * @param player
     * @return
     */
    private boolean checkMove(IMove move, int player) {
        int x = move.getX();
        int y = move.getY();

        boolean givesLine = false;

        copyBoard[x][y] = "" + player;

        checkForWinInMicro(player);
        // Checks if the move gives a new line

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {

                if (copyMacro[i][k].equals(macroBoard[i][k])) {
                    // do nothings
                } else {
                    givesLine = true;

                }
            }
        }
        if (givesLine) {
            return true;
        }
        return false;

    }

    private void setRandomMove() {

        moveToDo = setBestMove(availableMoves);
    }

    private void copyBoards() {
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                copyBoard[i][k] = board[i][k];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                copyMacro[i][k] = macroBoard[i][k];
            }
        }
    }

    private void checkForWinInMicro(int player) {
        checkForHorizontalWin(player);
        checkForVerticalWin(player);
        checkForDiagonalWin(player);

    }

    private boolean findGoodMove(List<IMove> moves) {
        boolean foundMove = false;
        int otherPlayer;
        if (playerId == 1) {
            otherPlayer = 0;
        } else {
            otherPlayer = 1;
        }
        // Tager hver move objekt som er tilgængeligt
        List<IMove> allGoodMoves = new ArrayList<>();

        for (IMove x : moves) {
            // laver en kopi af banen (både micro og macro)
            copyBoards();
            // Laver en kopi af kopien
            copyCopyBoard();
            // Sætter det pågældende move på microboardKopien
            copyBoard[x.getX()][x.getY()] = "" + playerId;

            // Udregn de næste gyldige træk
            List<IMove> newAvailableMoves = new ArrayList<>();
            //ActiveMicroBoard in the moment
            activeMicroBoard = findMicroBoard(x.getX(), x.getY());

            String[] curBoard = microBoards.get(activeMicroBoard - 1);

            String coordinate = "" + x.getX() + "." + x.getY();

            int indexOfCoordinateInMicroBoard = 100;

            for (int i = 0; i < 9; i++) {

                if (curBoard[i].equals(coordinate)) {
                    indexOfCoordinateInMicroBoard = i;
                }
            }
            // ActiveMicroBoard after a specific move
            activeMicroBoard = indexOfCoordinateInMicroBoard + 1;
            //Check if activeMicroBoard should be the entire field

            if (checkForWinOrDrawOnMicro(activeMicroBoard)) {

                continue;
            }
            String[] currentMicroBoard = microBoards.get(activeMicroBoard - 1);
            // Laver alle moves til det nye activeMicroBoard
            for (int i = 0; i < 9; i++) {
                String coor = currentMicroBoard[i];

                char xCor = coor.charAt(0);
                char yCor = coor.charAt(2);

                int xCoord = Character.getNumericValue(xCor);
                int yCoord = Character.getNumericValue(yCor);

                Move move = new Move(xCoord, yCoord);
                if (copyBoard[xCoord][yCoord] == "-1" || copyBoard[xCoord][yCoord] == ".") {
                    newAvailableMoves.add(move);
                }

            }

            // Tjek om nogle af de nye træk vil kunne give 3 på stribe for modstanderen senere
            boolean opponentGotLine = false;
            for (IMove y : newAvailableMoves) {
                makeCopyOriginalAgain();
                if (checkMove(y, otherPlayer)) {
                    opponentGotLine = true;
                    break;
                }
            }

            if (opponentGotLine == false) {
                allGoodMoves.add(x);
                foundMove = true;
            }
        }
        if (allGoodMoves.size() > 0) {

            moveToDo = setBestMove(allGoodMoves);
        }
        return foundMove;
    }

    private void checkForDiagonalWin(int playerId) {
        // checker for en diagonal sejr
        String player = "" + playerId;
        // microboard 1
        if (copyBoard[0][0].equals(player) && copyBoard[1][1].equals(player) && copyBoard[2][2].equals(player)) {
            copyMacro[0][0] = player;
        }

        if (copyBoard[0][2].equals(player) && copyBoard[1][1].equals(player) && copyBoard[2][0].equals(player)) {
            copyMacro[0][0] = player;
        }
        // microboard 2
        if (copyBoard[0][3].equals(player) && copyBoard[1][4].equals(player) && copyBoard[2][5].equals(player)) {
            copyMacro[0][1] = player;
        }

        if (copyBoard[0][5].equals(player) && copyBoard[1][4].equals(player) && copyBoard[2][3].equals(player)) {
            copyMacro[0][1] = player;
        }
        // microboard 3
        if (copyBoard[0][6].equals(player) && copyBoard[1][7].equals(player) && copyBoard[2][8].equals(player)) {
            copyMacro[0][2] = player;
        }

        if (copyBoard[0][8].equals(player) && copyBoard[1][7].equals(player) && copyBoard[2][6].equals(player)) {
            copyMacro[0][2] = player;
        }
        // microboard 4
        if (copyBoard[3][0].equals(player) && copyBoard[4][1].equals(player) && copyBoard[5][2].equals(player)) {
            copyMacro[1][0] = player;
        }

        if (copyBoard[3][2].equals(player) && copyBoard[4][1].equals(player) && copyBoard[5][0].equals(player)) {
            copyMacro[1][0] = player;
        }
        // microboard 5
        if (copyBoard[3][3].equals(player) && copyBoard[4][4].equals(player) && copyBoard[5][5].equals(player)) {
            copyMacro[1][1] = player;
        }

        if (copyBoard[3][5].equals(player) && copyBoard[4][4].equals(player) && copyBoard[5][3].equals(player)) {
            copyMacro[1][1] = player;
        }
        // microboard 6
        if (copyBoard[3][6].equals(player) && copyBoard[4][7].equals(player) && copyBoard[5][8].equals(player)) {
            copyMacro[1][2] = player;
        }

        if (copyBoard[3][8].equals(player) && copyBoard[4][7].equals(player) && copyBoard[5][6].equals(player)) {
            copyMacro[1][2] = player;
        }
        // microboard 7
        if (copyBoard[6][0].equals(player) && copyBoard[7][1].equals(player) && copyBoard[8][2].equals(player)) {
            copyMacro[2][0] = player;
        }

        if (copyBoard[6][2].equals(player) && copyBoard[7][1].equals(player) && copyBoard[8][0].equals(player)) {
            copyMacro[2][0] = player;
        }
        // microboard 8
        if (copyBoard[6][3].equals(player) && copyBoard[7][4].equals(player) && copyBoard[8][5].equals(player)) {
            copyMacro[2][1] = player;
        }

        if (copyBoard[6][5].equals(player) && copyBoard[7][4].equals(player) && copyBoard[8][3].equals(player)) {
            copyMacro[2][1] = player;
        }
        // microboard 9
        if (copyBoard[6][6].equals(player) && copyBoard[7][7].equals(player) && copyBoard[8][8].equals(player)) {
            copyMacro[2][2] = player;
        }

        if (copyBoard[6][8].equals(player) && copyBoard[7][7].equals(player) && copyBoard[8][6].equals(player)) {
            copyMacro[2][2] = player;
        }

    }

    private void checkForVerticalWin(int playerId) {

        // checker for en lodret sejr
        String player = "" + playerId;
        for (int i = 0; i < 3; i++) {
            // microboard 1
            if (copyBoard[0][i].equals(player) && copyBoard[1][i].equals(player) && copyBoard[2][i].equals(player)) {
                copyMacro[0][0] = player;
            }
            // microboard 2
            if (copyBoard[0][i + 3].equals(player) && copyBoard[1][i + 3].equals(player) && copyBoard[2][i + 3].equals(player)) {
                copyMacro[0][1] = player;
            }
            // microboard 3
            if (copyBoard[0][i + 6].equals(player) && copyBoard[1][i + 6].equals(player) && copyBoard[2][i + 6].equals(player)) {
                copyMacro[0][2] = player;
            }
            // microboard 4
            if (copyBoard[3][i].equals(player) && copyBoard[4][i].equals(player) && copyBoard[5][i].equals(player)) {
                copyMacro[1][0] = player;
            }
            // microboard 5
            if (copyBoard[3][i + 3].equals(player) && copyBoard[4][i + 3].equals(player) && copyBoard[5][i + 3].equals(player)) {
                copyMacro[1][1] = player;
            }
            // microboard 6
            if (copyBoard[3][i + 6].equals(player) && copyBoard[4][i + 6].equals(player) && copyBoard[5][i + 6].equals(player)) {
                copyMacro[1][2] = player;
            }
            // microboard 7
            if (copyBoard[6][i].equals(player) && copyBoard[7][i].equals(player) && copyBoard[8][i].equals(player)) {
                copyMacro[2][0] = player;
            }
            // microboard 8
            if (copyBoard[6][i + 3].equals(player) && copyBoard[7][i + 3].equals(player) && copyBoard[8][i + 3].equals(player)) {
                copyMacro[2][1] = player;
            }
            // microboard 9
            if (copyBoard[6][i + 6].equals(player) && copyBoard[7][i + 6].equals(player) && copyBoard[8][i + 6].equals(player)) {
                copyMacro[2][2] = player;
            }
        }
    }

    private void checkForHorizontalWin(int playerId) {
        //        checker for en vandret  sejr

        String player = "" + playerId;
        for (int i = 0; i < 3; i++) {
            // microboard 1
            if (copyBoard[i][0].equals(player) && copyBoard[i][1].equals(player) && copyBoard[i][2].equals(player)) {
                copyMacro[0][0] = player;
            }
            // microboard 2
            if (copyBoard[i][3].equals(player) && copyBoard[i][4].equals(player) && copyBoard[i][5].equals(player)) {
                copyMacro[0][1] = player;
            }
            // microboard 3
            if (copyBoard[i][6].equals(player) && copyBoard[i][7].equals(player) && copyBoard[i][8].equals(player)) {
                copyMacro[0][2] = player;
            }

            // microboard 4
            if (copyBoard[i + 3][0].equals(player) && copyBoard[i + 3][1].equals(player) && copyBoard[i + 3][2].equals(player)) {
                copyMacro[1][0] = player;
            }
            // microboard 5
            if (copyBoard[i + 3][3].equals(player) && copyBoard[i + 3][4].equals(player) && copyBoard[i + 3][5].equals(player)) {
                copyMacro[1][1] = player;
            }
            // microboard 6
            if (copyBoard[i + 3][6].equals(player) && copyBoard[i + 3][7].equals(player) && copyBoard[i + 3][8].equals(player)) {
                copyMacro[1][2] = player;
            }
            // microboard 7
            if (copyBoard[i + 6][0].equals(player) && copyBoard[i + 6][1].equals(player) && copyBoard[i + 6][2].equals(player)) {
                copyMacro[2][0] = player;
            }
            // microboard 8
            if (copyBoard[i + 6][3].equals(player) && copyBoard[i + 6][4].equals(player) && copyBoard[i + 6][5].equals(player)) {
                copyMacro[2][1] = player;
            }
            // microboard 9
            if (copyBoard[i + 6][6].equals(player) && copyBoard[i + 6][7].equals(player) && copyBoard[i + 6][8].equals(player)) {
                copyMacro[2][2] = player;
            }
        }
    }

    @Override
    public String getBotName() {
        return botName;
    }

    public int findMicroBoard(int x, int y) {
        String coordinate = "" + x + "." + y;
        int counter = 0;
        for (String[] k : microBoards) {
            counter++;
            for (int i = 0; i < 9; i++) {
                if (k[i].equals(coordinate)) {
                    return counter;
                }
            }
        }
        //Couldn't find coordinate - returns 100 - shouldn't happen
        return 100;
    }

    public void setMicroboardCoordinates() {
        String[] m1
                = {
                    "0.0", "0.1", "0.2", "1.0", "1.1", "1.2", "2.0", "2.1", "2.2"
                };
        String[] m2
                = {
                    "0.3", "0.4", "0.5", "1.3", "1.4", "1.5", "2.3", "2.4", "2.5"
                };
        String[] m3
                = {
                    "0.6", "0.7", "0.8", "1.6", "1.7", "1.8", "2.6", "2.7", "2.8"
                };
        String[] m4
                = {
                    "3.0", "3.1", "3.2", "4.0", "4.1", "4.2", "5.0", "5.1", "5.2"
                };
        String[] m5
                = {
                    "3.3", "3.4", "3.5", "4.3", "4.4", "4.5", "5.3", "5.4", "5.5"
                };
        String[] m6
                = {
                    "3.6", "3.7", "3.8", "4.6", "4.7", "4.8", "5.6", "5.7", "5.8"
                };
        String[] m7
                = {
                    "6.0", "6.1", "6.2", "7.0", "7.1", "7.2", "8.0", "8.1", "8.2"
                };
        String[] m8
                = {
                    "6.3", "6.4", "6.5", "7.3", "7.4", "7.5", "8.3", "8.4", "8.5"
                };
        String[] m9
                = {
                    "6.6", "6.7", "6.8", "7.6", "7.7", "7.8", "8.6", "8.7", "8.8"
                };

        microBoards.add(m1);
        microBoards.add(m2);
        microBoards.add(m3);
        microBoards.add(m4);
        microBoards.add(m5);
        microBoards.add(m6);
        microBoards.add(m7);
        microBoards.add(m8);
        microBoards.add(m9);
    }

    private void copyCopyBoard() {
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                copyCopyBoard[i][k] = copyBoard[i][k];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                copyCopyMacro[i][k] = copyMacro[i][k];
            }
        }
    }

    private void makeCopyOriginalAgain() {
        {
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    copyBoard[i][k] = copyCopyBoard[i][k];
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 3; k++) {
                    copyMacro[i][k] = copyCopyMacro[i][k];
                }
            }
        }
    }

    private boolean checkForWinOrDrawOnMicro(int activeMicroBoard) {
        boolean foundWinOrDraw = false;

        switch (activeMicroBoard) {
            case 1:
                if (!copyMacro[0][0].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 2:
                if (!copyMacro[0][1].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 3:
                if (!copyMacro[0][2].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 4:
                if (!copyMacro[1][0].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 5:
                if (!copyMacro[1][1].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 6:
                if (!copyMacro[1][2].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 7:
                if (!copyMacro[2][0].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 8:
                if (!copyMacro[2][1].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;

            case 9:
                if (!copyMacro[2][2].equals("-1")) {
                    foundWinOrDraw = true;
                }
                break;
        }
        return foundWinOrDraw;
    }

    /**
     * Removes moves which is in the middle of a microboard
     *
     * @param allGoodMoves
     * @return
     */
    private IMove setBestMove(List<IMove> moves) {
        List<IMove> bestMoves = new ArrayList<>();
        for (IMove move : moves) {
            boolean isBad = false;
            for (int i = 0; i < 9; i++) {
                String badMove = badMoves[i];
                char xCor = badMove.charAt(0);
                char yCor = badMove.charAt(2);
                int xCoord = Character.getNumericValue(xCor);
                int yCoord = Character.getNumericValue(yCor);
                if (move.getX() == xCoord && move.getY() == yCoord) {
                    isBad = true;
                }
            }
            if (isBad == false) {
                bestMoves.add(move);
            }
        }
        List<IMove> bestOfTheBest = new ArrayList<>();
        if (bestMoves.size() > 0) {
            for (IMove move : bestMoves) {
                boolean isBad = false;
                for (int i = 0; i < 36; i++) {
                    String badMove = otherBadMoves[i];
                    char xCor = badMove.charAt(0);
                    char yCor = badMove.charAt(2);
                    int xCoord = Character.getNumericValue(xCor);
                    int yCoord = Character.getNumericValue(yCor);
                    if (move.getX() == xCoord && move.getY() == yCoord) {
                        isBad = true;
                    }
                }
                if (isBad == false) {
                    bestOfTheBest.add(move);
                }

            }
        }

        //Måske tilføje lidt tilfældighed her?
        if (bestOfTheBest.size() > 0) {
            Double rNum = Math.random() * bestOfTheBest.size();
            int rNumInt = rNum.intValue();
            return bestOfTheBest.get(rNumInt);
        }

        if (bestMoves.size() > 0) {
            Double rNum = Math.random() * bestMoves.size();
            int rNumInt = rNum.intValue();
            return bestMoves.get(rNumInt);
        }

        // Hvis det ikke lykkes så bare retur en tilfældig fra den oprindelige liste
        Double rNum = Math.random() * moves.size();
        int rNumInt = rNum.intValue();
        return moves.get(rNumInt);
    }

    private void setBadMoves() {
        badMoves = new String[9];
        badMoves[0] = "" + 1 + "." + 1;
        badMoves[1] = "" + 1 + "." + 4;
        badMoves[2] = "" + 1 + "." + 7;
        badMoves[3] = "" + 4 + "." + 1;
        badMoves[4] = "" + 4 + "." + 4;
        badMoves[5] = "" + 4 + "." + 7;
        badMoves[6] = "" + 7 + "." + 1;
        badMoves[7] = "" + 7 + "." + 4;
        badMoves[8] = "" + 7 + "." + 7;

    }

    private void setOtherBadMoves() {
        otherBadMoves = new String[36];
        otherBadMoves[0] = "" + 1 + "." + 0;
        otherBadMoves[1] = "" + 0 + "." + 1;
        otherBadMoves[2] = "" + 1 + "." + 2;
        otherBadMoves[3] = "" + 2 + "." + 1;

        otherBadMoves[4] = "" + 3 + "." + 1;
        otherBadMoves[5] = "" + 4 + "." + 0;
        otherBadMoves[6] = "" + 4 + "." + 2;
        otherBadMoves[7] = "" + 5 + "." + 1;

        otherBadMoves[8] = "" + 6 + "." + 1;
        otherBadMoves[9] = "" + 7 + "." + 2;
        otherBadMoves[10] = "" + 8 + "." + 1;
        otherBadMoves[11] = "" + 7 + "." + 0;

        otherBadMoves[12] = "" + 0 + "." + 4;
        otherBadMoves[13] = "" + 1 + "." + 3;
        otherBadMoves[14] = "" + 2 + "." + 4;
        otherBadMoves[15] = "" + 1 + "." + 5;

        otherBadMoves[16] = "" + 3 + "." + 4;
        otherBadMoves[17] = "" + 4 + "." + 5;
        otherBadMoves[18] = "" + 5 + "." + 4;
        otherBadMoves[19] = "" + 4 + "." + 3;

        otherBadMoves[20] = "" + 6 + "." + 4;
        otherBadMoves[21] = "" + 7 + "." + 5;
        otherBadMoves[22] = "" + 8 + "." + 4;
        otherBadMoves[23] = "" + 7 + "." + 3;

        otherBadMoves[24] = "" + 0 + "." + 7;
        otherBadMoves[25] = "" + 1 + "." + 6;
        otherBadMoves[26] = "" + 1 + "." + 8;
        otherBadMoves[27] = "" + 2 + "." + 7;

        otherBadMoves[28] = "" + 3 + "." + 7;
        otherBadMoves[29] = "" + 4 + "." + 8;
        otherBadMoves[30] = "" + 5 + "." + 7;
        otherBadMoves[31] = "" + 4 + "." + 6;

        otherBadMoves[32] = "" + 6 + "." + 7;
        otherBadMoves[33] = "" + 7 + "." + 8;
        otherBadMoves[34] = "" + 8 + "." + 7;
        otherBadMoves[35] = "" + 7 + "." + 6;

    }

    /**
     * Check if it is possible to even win
     */
    private boolean checkIfItsPossibleToWin() {

        String[][] grid = this.macroBoard;

        String player = "" + playerId;
        String empty = "-1";
        List<String> check = Arrays.asList(player, empty);

        // checker om det er muligt at få en diagonal sejr
        if (check.contains(grid[0][0]) && check.contains(grid[1][1]) && check.contains(grid[2][2])) {
            return true;
        }

        if (check.contains(grid[0][2]) && check.contains(grid[1][1]) && check.contains(grid[2][0])) {
            return true;
        }

        // checker om det er muligt at få en vandret sejr
        for (int i = 0; i < 3; i++) {
            if (check.contains(grid[i][0]) && check.contains(grid[i][1]) && check.contains(grid[i][2])) {
                return true;
            }
        }

        // checker om det er muligt at få en lodret sejr
        for (int i = 0; i < grid.length; i++) {
            if (check.contains(grid[0][i]) && check.contains(grid[1][i]) && check.contains(grid[2][i])) {
                return true;
            }
        }
        return false;
    }

    private IMove checkForAMacroWin(List<IMove> attackMoves) {
        IMove toWin = null;
        for (IMove x : attackMoves) {
            copyBoards();
            copyBoard[x.getX()][x.getY()] = "" + playerId;
            checkForWinInMicro(playerId);
            if (checkForWin(copyMacro, playerId) == true) {
                toWin = x;
                break;
            }
            copyBoards();
        }
        return toWin;
    }

    private boolean checkForWin(String[][] macro, int playerId) {

        String[][] grid = macro;
        // checker for en vandret sejr
        String player = "" + playerId;
        for (int i = 0; i < 3; i++) {
            if (grid[i][0].equals(player) && grid[i][1].equals(player) && grid[i][2].equals(player)) {
                return true;
            }
        }
        // checker for en lodret sejr
        for (int i = 0; i < grid.length; i++) {
            if (grid[0][i].equals(player) && grid[1][i].equals(player) && grid[2][i].equals(player)) {
                return true;
            }
        }
        // checker for en diagonal sejr
        if (grid[0][0].equals(player) && grid[1][1].equals(player) && grid[2][2].equals(player)) {
            return true;
        }

        if (grid[0][2].equals(player) && grid[1][1].equals(player) && grid[2][0].equals(player)) {
            return true;
        }
        return false;
    }
}
