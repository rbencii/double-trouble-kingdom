package DTK_model;

import DTK_persistence.DBRecord;
import DTK_persistence.Database;
import DTK_view.GameField;
import DTK_view.GameFieldRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * GameState class implementation for Double Trouble Kingdom game, contains the Players, playerNumbers, the roundState, and other fields and methods for managing the state of the Game.
 */
public class GameState {
    private static final int KILL_GOLD = 6;
    public static double deltaTime = 1;
    public static ArrayList<Animator> animBuffer = new ArrayList<>();
    /**
     * Timer to increment elapsedTime.
     */
    private final Timer elapsedTimer;
    /**
     * ArrayList containing the Players.
     */
    private final ArrayList<Player> players;
    private final long startTime;
    /**
     * The current Player.
     */
    private Player currentPlayer;
    /**
     * The current Player's number.
     */
    private int playerNumber;

    private long prevTime = 1;
    /**
     * The current round phase.
     */
    private RoundState roundState;
    /**
     * GameField instance reference.
     */
    private GameField gameFieldReference;
    /**
     * Frames per second.
     */
    private final int fps = 60;
    /**
     * DB reference.
     */
    private Database databaseReference;
    /**
     * The elapsed time in seconds.
     */
    private int elapsedTime;
    private boolean pathVisualization = false;

    /**
     * Constructs a class containing the Players, roundState, and elapsedTimer.
     *
     * @param p1Name name of Player1
     * @param p2Name name of Player2
     */
    public GameState(String p1Name, String p2Name) {
        this.elapsedTimer = new Timer((int) (1000.0 / fps), (e) -> tickEvent());
        startElapsedTimer();
        this.players = new ArrayList<>(Arrays.asList(new Player(p1Name), new Player(p2Name)));
        decideStarter();
        this.roundState = RoundState.BUILDING;
        startTime = System.currentTimeMillis();
    }

    public GameState() {
        this.elapsedTimer = new Timer((int) (1000.0 / fps), (e) -> tickEvent());
        startElapsedTimer();
        this.players = new ArrayList<>(Arrays.asList(new Player(""), new Player("")));
        decideStarter();
        this.roundState = RoundState.BUILDING;
        startTime = System.currentTimeMillis();
    }






    /**
     * Link the GameField to the GameState.
     *
     * @param gameFieldReference the GameField
     */
    public void linkGameField(GameField gameFieldReference) {
        this.gameFieldReference = gameFieldReference;
    }

    /**
     * Link the Database to the GameState.
     *
     * @param db the Database
     */
    public void linkDBRef(Database db) {
        databaseReference = db;
    }

    /**
     * The tick function for the elapsedTimer.
     */
    private void tickEvent() {
        gameLoop();
        calculateTimes();
        animateBuffer();
        if(gameFieldReference != null)
            updateUI();
    }

    private void calculateTimes() {
        long curTime = System.currentTimeMillis();
        deltaTime = (double) curTime - prevTime;
        prevTime = curTime;
        elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    private void animateBuffer() {
        for (Animator animator : animBuffer) {
            if (animator.getEntity().isAnimated) {
                animator.animate();
            }
            if (animator.getPath().isEmpty()) {
                animator.stopAnimation();
            }
        }
    }

    private void updateUI() { 
            gameFieldReference.updateUIState();
    }

    /**
     * The game loop for the GameState.
     */
    private void gameLoop() {
        if (roundState.equals(RoundState.ATTACKING)) {
            if (animBuffer.stream().noneMatch(e -> e.getEntity().isAnimated())) {
                calculatePaths();
                setTowerTargets();
                setSpecialTargets();
                towerAttack();
                soldierAttack();

                if (checkAlive()) {
                    elapsedTimer.stop();
                    saveScore();
                }
                nextRoundState();
            }
        }
    }

    /**
     * Loads the buildings from a map file after load.
     *
     * @param map the map file
     */
    public void loadBuildings(Map map) {
        HashSet<Building> buildings = new HashSet<>();
        for (Terrain[] terrainRow : map.getTiles()) {
            for (Terrain terrain : terrainRow) {
                if (terrain.getEntities().size() > 0) {
                    buildings.add((Building) terrain.getEntities().get(0));
                }
            }
        }
        for (Building building : buildings) {
            players.get(building.side.equals(Sides.BLUE) ? 0 : 1).addSavedEntity(building);
        }
    }

    /**
     * Returns the Players in an ArrayList.
     *
     * @return the Players in an ArrayList
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the roundState.
     *
     * @return the roundState
     */
    public RoundState getRoundState() {
        return roundState;
    }

    /**
     * Switches the current roundState to the next one. Switches Player after Training roundState phase.
     */
    public void nextRoundState() {
        switch (this.roundState) {
            case BUILDING -> this.roundState = RoundState.TRAINING;
            case TRAINING -> {
                this.roundState = RoundState.ATTACKING;
                for (Animator animator : animBuffer) {
                    animator.startAnimation();
                }
            }
            case ATTACKING -> {
                nextPlayer();
                this.roundState = RoundState.BUILDING;
                currentPlayer.calculateGoldAtRound();
            }
        }

    }

    boolean isEnded = false;

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public boolean getEnded() {
        return isEnded;
    }

    /**
     * Returns if the Game is ended.
     *
     * @return if the Game is ended
     */
    public boolean checkAlive() {
        return players.get(0).getCastle().getHealthPoints() <= 0 && players.get(0).getSoldierCount() == 0 ||
                players.get(1).getCastle().getHealthPoints() <= 0 && players.get(1).getSoldierCount() == 0;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the winner of the game
     */
    public Player getWinner() {
        if (players.get(0).getCastle().getHealthPoints() <= 0 && players.get(0).getSoldierCount() == 0) {
            elapsedTimer.stop();
            return players.get(1);
        } else

            return players.get(0);

    }

    /**
     * Saves a DBRecord to the Game's Database.
     */
    private void saveScore() {
        databaseReference.saveRecord(new DBRecord(getPlayers().get(0).getName(), getPlayers().get(1).getName(), getWinner().getPlayerNumber(), getElapsedTime()));
    }

    /**
     * Determines the starting Player.
     */
    public void decideStarter() {
        Random rnd = new Random();
        this.playerNumber = rnd.nextInt(2);
        this.currentPlayer = players.get(this.playerNumber);
    }

    /**
     * Ends a Player's turn. Switches the Player and playerNumber to next Player.
     */
    private void nextPlayer() {
        this.playerNumber = this.playerNumber == 0 ? 1 : 0;
        this.currentPlayer = players.get(this.playerNumber);
    }

    /**
     * Returns the current Player.
     *
     * @return the current Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player number.
     *
     * @param p the current player number
     */
    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
        playerNumber = p.getPlayerNumber() == 1 ? 0 : 1;
    }

    /**
     * Starts the elapsedTimer.
     */
    public void startElapsedTimer() {
        elapsedTimer.start();
    }

    /**
     * Stops the elapsedTimer.
     */
    public void stopElapsedTimer() {
        elapsedTimer.stop();
    }

    /**
     * Restarts the elapsedTimer.
     */
    public void restartElapsedTimer() {
        elapsedTimer.restart();
    }

    /**
     * Returns the enemy's Castle.
     *
     * @param playerNumber the Player's number
     * @return the enemy's Castle
     */
    public Building getEnemyCastle(int playerNumber) {
        return players.get(playerNumber == 1 ? 1 : 0).getCastle();
    }

    /**
     * Returns the enemy's Soldiers.
     *
     * @param playerNumber the Player's number
     * @return the enemy's Soldiers
     */
    public ArrayList<Soldier> getEnemySoldiers(int playerNumber) {
        return players.get(playerNumber == 1 ? 1 : 0).getSoldiers();
    }

    /**
     * Sets the soldier's target.
     */
    public void setTargets() {
        for (Player player : players) {
            for (Entity entity : player.getEntities()) {
                if (Types.getSoldierTypes().contains(entity.getType())) {
                    ((Soldier) entity).selectTarget(getEnemyCastle(player.getPlayerNumber()));
                }
            }
        }
    }

    /**
     * Attack with each Soldier.
     */
    private void soldierAttack() {
        for (Player player : players) {
            for (Soldier soldier : player.getSoldiers()) {
                if (soldier.isAlive) {
                    soldier.attack();
                }
            }
        }
        removeDeadSoldiers();
    }

    /**
     * Attack with each Tower.
     */
    private void towerAttack() {
        for (Player player : players) {
            for (Tower tower : player.getTowers()) {
                if (tower.isAlive) {
                    tower.attack();
                }
            }
        }
        removeDeadSoldiers();
    }

    /**
     * Removes dead Soldiers.
     */
    private void removeDeadSoldiers() {
        for (Player player : players) {
            for (Soldier soldier : player.getSoldiers()) {
                if (!soldier.isAlive()) {
                    player.removeSoldier(soldier);
                    players.get(player.getPlayerNumber() == 1 ? 1 : 0).addGold(KILL_GOLD);
                }
            }
        }
    }


    /**
     * Sets the Towers' targets.
     */
    private void setTowerTargets() {
        for (Player player : players) {
            for (Tower tower : player.getTowers()) {
                tower.selectTargets(getEnemySoldiers(player.getPlayerNumber()));
            }
        }
    }

    /**
     * Sets the special Soldiers' targets.
     */
    private void setSpecialTargets() {
        for (Player player : players) {
            for (Soldier soldier : player.getSoldiers()) {
                if (soldier instanceof Assassin assassin) {
                    assassin.selectTargets(getEnemySoldiers(player.getPlayerNumber()));
                }
                if (soldier instanceof Kamikaze kamikaze) {
                    kamikaze.selectTargets(getEnemyTowers(player.getPlayerNumber()));
                }
            }
        }
    }

    /**
     * Returns the enemy's Towers.
     *
     * @param playerNumber the Player's number
     * @return the enemy's Towers
     */
    public ArrayList<Tower> getEnemyTowers(int playerNumber) {
        return players.get(playerNumber == 1 ? 1 : 0).getTowers();

    }

    /**
     * Returns elapsed time.
     *
     * @return elapsed time
     */
    public int getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Sets the elapsed time.
     *
     * @param elapsedTime the elapsed time
     */
    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * Calculates the soldier' path.
     */
    public void calculatePaths() {
        Pathfinder.setMap(GameFieldRenderer.getMapRef());
        for (Player player : players) {
            for (Soldier soldier : player.getSoldiers()) {
                soldier.calculatePath();
                soldier.animObj.setPath(soldier.getPath());
            }
        }
    }

    /**
     * Returns the waypoints.
     *
     * @return the waypoints
     */
    public ArrayList<Point> getWayPoints() {
        if (gameFieldReference != null) {
            return gameFieldReference.getWayPoints();
        }
        return null;
    }

    /**
     * Toggles if path visualization is enabled.
     */
    public void togglePathVisualization() {
        pathVisualization = !pathVisualization;
    }

    /**
     * Returns if path visualization is enabled.
     *
     * @return if path visualization is enabled
     */
    public boolean isPathVisualization() {
        return pathVisualization;
    }
}
