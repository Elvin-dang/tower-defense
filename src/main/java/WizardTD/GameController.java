package WizardTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Button.Button;
import Button.FastForwardButton;
import Button.PauseButton;
import Button.BuildTowerButton;
import Button.UpgradeRangeButton;
import Button.UpgradeSpeedButton;
import Button.UpgradeDamageButton;
import Button.ManaPoolButton;
import Fireball.Fireball;
import Monsters.Monster;
import Monsters.Wave;
import Navigation.Navigation;
import Navigation.Tuple;
import Tiles.GrassTile;
import Tiles.Tile;
import Towers.Tower;

public class GameController {
  private App app;
  private GameResource gr;

  private int gameSpeed = 1;
  private boolean towerMode = false;
  private int isUpgradeRange = 0;
  private int isUpgradeFiringSpeed = 0;
  private int isUpgradeDamage = 0;

  private float manaCap;
  private float mana;
  private float manaGainPerSecond;
  private float additionalManaGainMultiplier = 0;
  private float poolManaCost;

  private Tile[][] tiles;
  private List<Tuple<Long, Monster>> monsters;
  private List<Tower> towers;
  private List<Fireball> fireballs;
  private List<Tuple<Integer, Long>> waveInfo;
  private List<Button> buttons;

  private long timer = 0;
  private long countDown = 0;
  private boolean isLose = false;

  public GameController(App app, GameResource gr) {
    this.app = app;
    this.gr = gr;

    tiles = gr.getTiles();
    monsters = new ArrayList<>();
    towers = new ArrayList<>();
    fireballs = new ArrayList<>();
    waveInfo = new ArrayList<>();
    buttons = new ArrayList<>();

    mana = gr.initialMana;
    manaCap = gr.initialManaCap;
    manaGainPerSecond = gr.initialManaPerSecond;
    poolManaCost = gr.manaPoolSpellInitialCost;

    generateMonsters();
    generateButtons();
  }

  private void generateMonsters() {
    List<Wave> waves = gr.getWaves();
    List<Navigation> navigationList = gr.getRoadForMonster();
    long totalDelay = 0;
    long totalMonster = 0;

    for (int i = 0; i < waves.size(); i++) {
      Wave wave = waves.get(i);
      totalDelay += wave.getPreWavePause() * 1000;
      Map<Monster, Integer> map = wave.getMonsters();
      List<Monster> waveMonsters = new ArrayList<>();
      Random random = new Random();

      for (Map.Entry<Monster, Integer> entry : map.entrySet()) {
        totalMonster += entry.getValue();
        for (int j = 0; j < entry.getValue(); j++) {
          int randomPosition = random.nextInt(waveMonsters.size() + 1);
          int randomNavigationIndex = random.nextInt(navigationList.size());
          Monster newMonster = entry.getKey().duplicate();
          newMonster.setNavigation(navigationList.get(randomNavigationIndex));
          newMonster.setGameController(this);
          waveMonsters.add(randomPosition, newMonster);
        }
      }

      long delayPerUnit = (long) wave.getDuration() * 1000 / totalMonster;

      for (int j = 0; j < waveMonsters.size(); j++) {
        monsters.add(new Tuple<Long, Monster>(totalDelay + j * delayPerUnit, waveMonsters.get(j)));
      }

      totalDelay += wave.getDuration() * 1000;

      if (i == 0) {
        waveInfo.add(new Tuple<Integer, Long>(i + 1, (long) (wave.getPreWavePause() * 1000)));
      } else {
        waveInfo.add(
            new Tuple<Integer, Long>(i + 1, (long) ((wave.getPreWavePause() + waves.get(i - 1).getDuration()) * 1000)));
      }
    }

    waveInfo.add(new Tuple<Integer, Long>(-1, 0L));
  }

  public List<Tuple<Long, Monster>> getMonsters() {
    return monsters;
  }

  private void generateButtons() {
    buttons.add(new FastForwardButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 10, "2x speed", "FF",
        'f', false));
    buttons.add(
        new PauseButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 60, "PAUSE", "P", 'p', false));
    buttons.add(new BuildTowerButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 110, "Build tower",
        "T", 't', true));
    buttons.add(new UpgradeRangeButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 160,
        "Upgrade range", "U1", '1', false));
    buttons.add(new UpgradeSpeedButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 210,
        "Upgrade speed", "U2", '2', false));
    buttons.add(new UpgradeDamageButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 260,
        "Upgrade damage", "U3", '3', false));
    buttons.add(new ManaPoolButton(app, this, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 310,
        "Mana pool cost: " + (int) poolManaCost, "M", 'm', true));
  }

  public List<Button> getButtons() {
    return buttons;
  }

  public Tile[][] getTiles() {
    return tiles;
  }

  public void setTowerMode(boolean towerMode) {
    this.towerMode = towerMode;
  }

  public void setIsUpgradeRange(int isUpgradeRange) {
    this.isUpgradeRange = isUpgradeRange;
  }

  public void setIsUpgradeFiringSpeed(int isUpgradeFiringSpeed) {
    this.isUpgradeFiringSpeed = isUpgradeFiringSpeed;
  }

  public void setIsUpgradeDamage(int isUpgradeDamage) {
    this.isUpgradeDamage = isUpgradeDamage;
  }

  public int getGameSpeed() {
    return gameSpeed;
  }

  public void setCountDown(long countDown) {
    this.countDown = countDown;
  }

  public long getCountDown() {
    return countDown;
  }

  public List<Tuple<Integer, Long>> getWaveInfo() {
    return waveInfo;
  }

  public void increaseTimer() {
    long timeIncreaseAmount = gameSpeed * 1000 / App.FPS;
    float manaIncreaseAmount = gameSpeed * manaGainPerSecond * (1 + additionalManaGainMultiplier) / App.FPS;
    timer += timeIncreaseAmount;
    mana += manaIncreaseAmount;
    if (mana > manaCap)
      mana = manaCap;
    if (countDown > 0)
      countDown -= timeIncreaseAmount;
  }

  public long getTimer() {
    return timer;
  }

  public void changeGameSpeed(int value) {
    gameSpeed = value;
    for (Tuple<Long, Monster> tuple : monsters) {
      tuple.getValue().setSpeed(gameSpeed);
    }
    for (Tower tower : towers) {
      tower.setSpeed(gameSpeed);
    }
    for (Fireball fireball : fireballs) {
      fireball.setSpeed(gameSpeed);
    }
  }

  public float getManaCap() {
    return manaCap;
  }

  public void setManaCap(float manaCap) {
    this.manaCap = manaCap;
  }

  public float getMana() {
    return mana;
  }

  public void gainMana(float amount) {
    mana += amount * (1 + additionalManaGainMultiplier);
    if (mana > manaCap) {
      mana = manaCap;
    }
  }

  public void dropManaByMonster(float amount) {
    mana -= amount;
    if (mana <= 0) {
      isLose = true;
      mana = 0;
      changeGameSpeed(0);
    }
  }

  public void dropMana(float amount) {
    mana -= amount;
  }

  public float getPoolManaCost() {
    return poolManaCost;
  }

  public void setPoolManaCost(float poolManaCost) {
    this.poolManaCost = poolManaCost;
  }

  public float getAdditionalManaGainMultiplier() {
    return additionalManaGainMultiplier;
  }

  public void setAdditionalManaGainMultiplier(float additionalManaGainMultiplier) {
    this.additionalManaGainMultiplier = additionalManaGainMultiplier;
  }

  public void onMouseClickedOnTheBoard() {
    int boardSize = App.BOARD_WIDTH * App.CELLSIZE;
    int towerCost = (int) gr.towerCost + isUpgradeRange * 20 + isUpgradeDamage * 20 + isUpgradeFiringSpeed * 20;

    if (towerMode && mana >= towerCost && app.mouseX >= 0 && app.mouseX < boardSize && app.mouseY >= App.TOPBAR
        && app.mouseY < App.TOPBAR + boardSize) {
      int i = (app.mouseY - App.TOPBAR) / App.CELLSIZE;
      int j = app.mouseX / App.CELLSIZE;

      if (tiles[i][j] instanceof GrassTile) {
        mana -= towerCost;
        float range = gr.initialTowerRange;
        float damage = gr.initialTowerDamage;
        float firingSpeed = gr.initialTowerFiringSpeed;
        Tower newTower = new Tower(app, this, App.CELLSIZE, App.CELLSIZE, tiles[i][j].getX(), tiles[i][j].getY(),
            gr.tower0, gr.tower1, gr.tower2, range, damage, firingSpeed, isUpgradeRange, isUpgradeFiringSpeed,
            isUpgradeDamage);
        newTower.setSpeed(gameSpeed);
        towers.add(newTower);
        tiles[i][j] = newTower;
      }
    }
  }

  public void onMouseOnTheBoard() {
    int boardSize = App.BOARD_WIDTH * App.CELLSIZE;
    int towerCost = (int) gr.towerCost + isUpgradeRange * 20 + isUpgradeDamage * 20 + isUpgradeFiringSpeed * 20;

    if (towerMode && app.mouseX >= 0 && app.mouseX < boardSize && app.mouseY >= App.TOPBAR
        && app.mouseY < App.TOPBAR + boardSize) {
      int i = (app.mouseY - App.TOPBAR) / App.CELLSIZE;
      int j = app.mouseX / App.CELLSIZE;

      if (tiles[i][j] instanceof GrassTile) {
        if (mana >= towerCost) {
          app.fill(67, 255, 143, 200);
          app.noStroke();
          app.rect(tiles[i][j].getX(), tiles[i][j].getY(), tiles[i][j].getWidth(), tiles[i][j].getHeight(), 4);
        } else {
          app.fill(255, 67, 80, 200);
          app.noStroke();
          app.rect(tiles[i][j].getX(), tiles[i][j].getY(), tiles[i][j].getWidth(), tiles[i][j].getHeight(), 4);
        }
      }
    }
  }

  public List<Tower> getTowers() {
    return towers;
  }

  public List<Fireball> getFireballs() {
    return fireballs;
  }

  public boolean getIsLose() {
    return isLose;
  }

  public boolean getIsWin() {
    if (monsters.size() == 0) {
      changeGameSpeed(0);
      return true;
    } else {
      return false;
    }
  }

  public int getIsUpgradeRange() {
    return isUpgradeRange;
  }

  public int getIsUpgradeFiringSpeed() {
    return isUpgradeFiringSpeed;
  }

  public int getIsUpgradeDamage() {
    return isUpgradeDamage;
  }

  public void resetState() {
    monsters = new ArrayList<>();
    towers = new ArrayList<>();
    fireballs = new ArrayList<>();
    waveInfo = new ArrayList<>();
    buttons = new ArrayList<>();

    gameSpeed = 1;
    towerMode = false;
    isUpgradeRange = 0;
    isUpgradeDamage = 0;
    isUpgradeFiringSpeed = 0;

    tiles = gr.getTiles();
    mana = gr.initialMana;
    manaCap = gr.initialManaCap;
    manaGainPerSecond = gr.initialManaPerSecond;
    additionalManaGainMultiplier = 0;
    poolManaCost = gr.manaPoolSpellInitialCost;

    timer = 0;
    countDown = 0;
    isLose = false;

    generateMonsters();
    generateButtons();
  }
}
