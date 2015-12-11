import java.io.*;
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
    private int[][] bestScores;
    private double version = 1.0;


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
        bestScores = new int[3][3];
        loadScores();
    }

    private void loadScores() {
        File saveLow = new File("./saveLow.txt");
        File saveMedium = new File("./saveMedium.txt");
        File saveBig = new File("./saveBig.txt");
        BufferedInputStream stream;
        try{
            //Chargement des scores en 4x4
            stream = new BufferedInputStream(new FileInputStream(saveLow));
            for (int i = 0; i < 3; i++){
                boolean end = false;
                String add = "";
                while(!end){
                    int toAdd = stream.read();
                    if (toAdd == 10 || toAdd == 32)
                        end = true;
                    else
                        add += String.valueOf((char)toAdd);
                }
                bestScores[0][i] = Integer.parseInt(add);
            }
            //Chargement des scores en 5x5
            stream = new BufferedInputStream(new FileInputStream(saveMedium));
            for (int i = 0; i < 3; i++){
                boolean end = false;
                String add = "";
                while(!end){
                    int toAdd = stream.read();
                    if (toAdd == 10 || toAdd == 32)
                        end = true;
                    else
                        add += String.valueOf((char)toAdd);
                    System.out.println(add);
                }
                bestScores[1][i] = Integer.parseInt(add);
            }
            //Chargement des scores en 6x6
            stream = new BufferedInputStream(new FileInputStream(saveBig));
            for (int i = 0; i < 3; i++){
                boolean end = false;
                String add = "";
                while(!end){
                    int toAdd = stream.read();
                    if (toAdd == 10 || toAdd == 32)
                        end = true;
                    else
                        add += String.valueOf((char)toAdd);
                }
                bestScores[2][i] = Integer.parseInt(add);
            }
            stream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erreur lors de la création du stream de lecture.");
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    bestScores[i][j] = 0;
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des scores. ");
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    bestScores[i][j] = 0;
                }
            }
        }
    }

    private void writeScores(){
        File saveLow = new File("./saveLow.txt");
        File saveMedium = new File("./saveMedium.txt");
        File saveBig = new File("./saveBig.txt");
        DataOutputStream stream;
        try{
            //Ecriture des scores en 4x4
            stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveLow)));
            for(int score: bestScores[0]){
                stream.writeInt(score);
            }
            //Ecriture des scores en 5x5
            stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveMedium)));
            for(int score: bestScores[1]){
                stream.writeInt(score);
            }
            //Ecriture des scores en 6x6
            stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveBig)));
            for(int score: bestScores[2]){
                stream.writeInt(score);
            }
            stream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Impossible de créer le stream d'écriture");
        } catch (IOException e) {
            System.out.println("Impossible d'écrire les scores");
        }
    }
    /**
     * @return gridSize : taille actuelle de la grille
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * @param i : int - 1er indice de la case à récupérer
     * @param j :int - 2ème indice de la case à récupérer
     * @return int - La valeur de la case désirée
     */
    public int getCase(int i, int j){
        return grid[i][j];
    }

    /**
     * Effectue le bon déplacement en fonction de la direction demandée puis ajoute une case
     * @param direction : String - up, down, right ou left
     */
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

    /**
     * Ajoute une case dans la grille - 90% de chance que ce soit un 2, sinon un 4 (d'après les règles d'origine)
     */
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

    /**
     * Déplace vers la gauche
     * @return true si on doit ajouter une case par la suite
     */
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

    /**
     * Déplace vers la droite
     * @return true si on doit ajouter une case par la suite
     */
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

    /**
     * Déplace vers le haut
     * @return true si on doit ajouter une case par la suite
     */
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

    /**
     * Déplace vers le bas
     * @return true si on doit ajouter une case par la suite
     */
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

    /**
     * Vérifie la grille et détecte si il y a encore des possibilités de déplacement
     * @return true si des mouvements sont encore possibles
     */
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

    /**
     * Set automatiquement playable à l'aide de la méthode checkGrid()
     */
    private void autoSetPlayable(){
        playable = checkGrid();
    }

    /**
     * true si il y a encore possibilité de jouer
     * @return playable : boolean
     */
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

    public void setBestScores() {
        switch (gridSize){
            case 4:
                for (int i = 0; i < 3; i++){
                    if (bestScores[0][i] < score){
                        int tmp = bestScores[0][i];
                        bestScores[0][i] = score;
                        score = tmp;
                        setBestScores();
                    }
                }
                break;
            case 5:
                for (int i = 0; i < 3; i++){
                    if (bestScores[1][i] < score){
                        int tmp = bestScores[1][i];
                        bestScores[1][i] = score;
                        score = tmp;
                        setBestScores();
                    }
                }
                break;
            case 6:
                for (int i = 0; i < 3; i++){
                    if (bestScores[2][i] < score){
                        int tmp = bestScores[2][i];
                        bestScores[2][i] = score;
                        score = tmp;
                        setBestScores();
                    }
                }
                break;
        }
    }

    public int getScore(int i, int j){
        return bestScores[i][j];
    }
}
