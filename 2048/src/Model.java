import java.util.*;

/**
 * Created by evanzyker on 24/11/15.
 */
public class Model {

    private int[][] grid;
    private int gridSize;
    private int[] valuesCase = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};
    private boolean playable;
    private int score;
    private double version = 0.9;


    /**
     * Unique constructeur nécessitant une taille de grille
     * @param gridSize Taille de la grille
     */
    public Model(int gridSize){
        playable = true;
        this.gridSize = gridSize;
        grid = new int[this.gridSize][this.gridSize];
        for (int i = 0; i < this.gridSize; i++){
            for (int j = 0; j < this.gridSize; j++){
                grid[i][j] = 0;
            }
        }
        addOne();
    }

    /**
     * @return gridSize : taille actuelle de la grille
     */
    public int getGridSize() {
        return gridSize;
    }

    /*
    public int[] getValuesCase() {
        return valuesCase;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setCase(int i, int j, int val){
        grid[i][j] = val;
    }*/

    public int getCase(int i, int j){
        return grid[i][j];
    }

    public void move(String direction) {
        switch (direction){
            case "up":
                if (moveUp()){
                    addOne();
                }
                break;
            case "down":
                if (moveDown()){
                    addOne();
                }
                break;
            case "right":
                if (moveRight()){
                    addOne();
                }
                break;
            case "left":
                if (moveLeft()){
                    addOne();
                }
                break;
        }
        autoSetPlayable();
    }

    private void addOne() {
        Random rng = new Random();
        int i, j;
        boolean add = false;
        while (!add){
            i = rng.nextInt(gridSize);
            j = rng.nextInt(gridSize);
            if (grid[i][j] == 0){
                int random = rng.nextInt(100);
                if (random < 90){
                    grid[i][j] = 2;
                    add = true;
                }else{
                    grid[i][j] = 4;
                    add = true;
                }
            }
        }
    }

    private boolean moveLeft() {
        boolean still;
        boolean mustAdd = false;
        do{
            still = false;
            for (int r = 0; r < gridSize; r++){
                for (int c = 1; c < gridSize; c++){
                    if (grid[r][c] != 0){
                        if ((grid[r][c - 1] == grid[r][c])|| grid[r][c - 1] == 0){
                            //Si on effectue une fusion et non un simple déplacement on augmente le score
                            if (grid[r][c - 1] != 0)
                                score += grid[r][c];
                            grid[r][c - 1] += grid[r][c];
                            grid[r][c] = 0;
                            still = true;
                            mustAdd = true;
                        }
                    }
                }
            }
        }while(still);
        return mustAdd;
    }

    private boolean moveRight() {
        boolean still;
        boolean mustAdd = false;
        do{
            still = false;
            for (int r = 0; r < gridSize; r++){
                for (int c = 0; c < gridSize - 1; c++){
                    if (grid[r][c] != 0){
                        if ((grid[r][c + 1] == grid[r][c])|| grid[r][c + 1] == 0){
                            //Si on effectue une fusion et non un simple déplacement on augmente le score
                            if (grid[r][c + 1] != 0)
                                score += grid[r][c];
                            grid[r][c + 1] += grid[r][c];
                            grid[r][c] = 0;
                            still = true;
                            mustAdd = true;
                        }
                    }
                }
            }
        }while(still);
        return mustAdd;
    }

    private boolean moveUp() {
        boolean still;
        boolean mustAdd = false;
        do{
            still = false;
            for (int r = 1; r < gridSize; r++){
                for (int c = 0; c < gridSize; c++){
                    if (grid[r][c] != 0){
                        if ((grid[r - 1][c] == grid[r][c])|| grid[r - 1][c] == 0){
                            //Si on effectue une fusion et non un simple déplacement on augmente le score
                            if (grid[r - 1][c] != 0)
                                score += grid[r][c];
                            grid[r - 1][c] += grid[r][c];
                            grid[r][c] = 0;
                            still = true;
                            mustAdd = true;
                        }
                    }
                }
            }
        }while(still);
        return mustAdd;
    }

    private boolean moveDown(){
        boolean still;
        boolean mustAdd = false;
        do{
            still = false;
            for (int r = 0; r < gridSize - 1; r++){
                for (int c = 0; c < gridSize; c++){
                    if (grid[r][c] != 0){
                        if ((grid[r + 1][c] == grid[r][c])|| grid[r + 1][c] == 0){
                            //Si on effectue une fusion et non un simple déplacement on augmente le score
                            if (grid[r + 1][c] != 0)
                                score += grid[r][c];
                            grid[r + 1][c] += grid[r][c];
                            grid[r][c] = 0;
                            still = true;
                            mustAdd = true;
                        }
                    }
                }
            }
        }while (still);
        return mustAdd;
    }

    private boolean checkGrid(){
        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){
                if (grid[r][c] == 0){
                    return true;
                }else if (r - 1 > 0 && grid[r - 1][c] == grid[r][c]){
                    return true;
                }else if (r + 1 < gridSize && grid[r + 1][c] == grid[r][c]){
                    return true;
                }else if (c - 1 > 0 && grid[r][c - 1] == grid[r][c]) {
                    return true;
                }else if (c + 1 < gridSize && grid[r][c + 1] == grid[r][c]){
                    return true;
                }
            }
        }
        return false;
    }

    private void autoSetPlayable(){
        playable = checkGrid();
    }

    public boolean isPlayable(){
        return playable;
    }

    /**
     * Change la taille de la grille et  remets les cases et le score à 0.
     * @param size - Nouvelle taille de la grille
     */
    public void setGridSize(int size) {
        gridSize = size;
        grid = new int[this.gridSize][this.gridSize];
        for (int i = 0; i < this.gridSize; i++){
            for (int j = 0; j < this.gridSize; j++){
                grid[i][j] = 0;
            }
        }
        addOne();
        score = 0;
    }


    /**
     * Ajoute une case 2048 afin de tester la victoire du joueur. Désactivé par défault.
     */
    public void triggerWin(){
        Random rng = new Random();
        int i, j;
        boolean add = false;
        while (!add){
            i = rng.nextInt(gridSize);
            j = rng.nextInt(gridSize);
            if (grid[i][j] == 0){
                switch (gridSize){
                    case 4:
                        grid[i][j] = 2048;
                        add = true;
                        break;
                    case 5:
                        grid[i][j] = 4096;
                        add = true;
                        break;
                    case 6:
                        grid[i][j] = 8192;
                        add = true;
                        break;
                }
            }
        }
    }


    /**
     * Trouve la plus grande case et détermine si le joueur a gagné grâce à celle-ci.
     * @return true si le joueur a gagné
     */
    public boolean hasWon() {
        int higher = 0;
        //On cherche la case la plus grande
        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){
                if (grid[r][c] > higher)
                    higher = grid[r][c];
            }
        }
        int condition = 0;
        //Set de la condition en fonction de la taille de la grille
        switch (gridSize){
            case 4:
                condition = 2048;
                break;
            case 5:
                condition = 4096;
                break;
            case 6:
                condition = 8192;
                break;
        }
        //Si la condition est remplie alors renvoie true
        return (higher >= condition);
    }


    /**
     * @return Le score actuel en String
     */
    public String getScoreString() {
        return String.valueOf(score);
    }


    /**
     * @return Le numéro de version en String
     */
    public String getversion() {
        return String.valueOf(version);
    }
}
