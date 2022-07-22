import java.util.Scanner;

public class GameOfLife {

    /**
     * Creates a boolean matrix from an array of strings, which hold the states
     * @param cells the strings with the states
     * @param width width of the matrix
     * @param height height of the matrix
     * @return a boolean matrix with the states of the cells
     */
    public boolean[][] stateFromCells(String[] cells, int width, int height){
        if(cells.length != width || cells.length != height) {
            System.out.println("parameters are not in range");
            System.exit(0);
        }
        boolean[][] states = new boolean[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++) {
                states[i][j] = cells[i].charAt(j) == '1' ? true : false;
            }
        }
        return states;
    }

    /**
     * returns the amount of neighbours each cell of the matrix has
     * @param x the column of the matrix
     * @param y the row of the matrix
     * @param state the current state, or the cell at x and y
     * @param width of the matrix
     * @param height of the matrix
     * @return amount of neighbours for the current cell, -1 if parameters are too small
     */
    public int countNeighbours(int x, int y, boolean[][] state, int width, int height){
        if(height < 4 || width < 4)
            return -1;
        int count = 0;
        int a = y - 1;
        int b = y + 1;
        int c = x - 1;
        int d = x + 1;
        if(x == width - 1){
            c = x - 1;
            d = 0;
        }
        if(x == 0){
            c = width - 1;
            d = x + 1;
        }
        if(y == height - 1){
            a = y - 1;
            b = 0;
        }
        if(y == 0){
            b = y + 1;
            a = height - 1;
        }

        count = state[x][a] == true ? count + 1 : count;
        count = state[x][b] == true ? count + 1 : count;
        count = state[c][y] == true ? count + 1 : count;
        count = state[c][a] == true ? count + 1 : count;
        count = state[c][b] == true ? count + 1 : count;
        count = state[d][a] == true ? count + 1 : count;
        count = state[d][y] == true ? count + 1 : count;
        count = state[d][b] == true ? count + 1 : count;
        return count;
    }

    /**
     * returns the boolean of the next state, or the state of the next cell
     * @param cell the current cell of the matrix
     * @param neighbours the neighbours of the current cell
     * @return true if cell is alive(true) and neighbours are 2 or 3, true if cell is dead(false)
     *         and neighbours are exactly 3, false(dead) in any other case
     */
    public boolean next(boolean cell, int neighbours){
        if(cell && (neighbours == 3 || neighbours == 2))
            return true;
        else if(!cell && neighbours == 3)
            return true;

        return false;
    }

    /**
     * prints the boolean matrix
     * @param state the boolean matrix
     * @param width of the matrix
     * @param height of the matrix
     */
    private void printGame(boolean[][] state, int width, int height){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(state[i][j])
                    System.out.print("#");
                else
                    System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    /**
     * creates the next turn/matrix of the game of life
     * @param state the current matrix of the game
     * @param width width of the matrix
     * @param height height of the matrix
     * @return the next turn of the game
     */
    public boolean[][] gameOfLife(boolean[][] state, int width, int height){
        boolean[][] newState = new boolean[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int count = countNeighbours(i, j, state, width, height);
                if(count != -1)
                    newState[i][j] = next(state[i][j], countNeighbours(i, j, state, width, height));
                else {
                    System.out.println("Wrong parameters!");
                    System.exit(0);
                }
            }
        }
        return newState;
    }

    /**
     * simple game running method
     * @param state the first round of the game
     * @param width of the matrix
     * @param height of the matrix
     * @param count start of the generation count
     */
    public void run(boolean[][] state, int width, int height, int count){
        System.out.println("Continue? If yes, enter number 1");
        Scanner scan = new Scanner(System.in);
        int yes = scan.nextInt();
        if(yes == 1){
            printGame(state, width, height);
            System.out.println("Generation: " + count);
            state = gameOfLife(state, width, height);
            run(state, width, height, count + 1);
        }
        else
            System.exit(0);
    }
    public static void main(String args[]){
        int width = 5;
        int height = 5;
        String[] cells = {"00000","00100", "00010", "01110", "00000"};
        GameOfLife game = new GameOfLife();
        game.run(game.stateFromCells(cells, width, height), width, height, 1);
    }
}
