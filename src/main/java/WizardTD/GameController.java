package WizardTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Button.Button;
import Monsters.Monster;
import Monsters.Wave;
import Navigation.Navigation;
import Navigation.Tuple;
import Tiles.GrassTile;
import Tiles.Tile;
import Towers.Tower;

public class GameController {
  private final App app;
  private final GameResource gr;

  private int gameSpeed = 1;
  private boolean towerMode = false;
  private int isUpgradeRange = 0;
  private int isUpgradeDamage = 0;
  private int isUpgradeFiringSpeed = 0;

  private float manaCap;
  private float mana;
  private float manaGainPerSecond;
  private float additionalManaGainMultiplier = 0;
  private float poolManaCost;
  private long countDown;

  private List<Tuple<Long, Monster>> monsters;
  private List<Tower> towers;
  private List<Tuple<Integer, Long>> waveInfo;
  private List<Button> buttons;

  private long timer = 0;

  public GameController(App app, GameResource gr) {
    this.app = app;
    this.gr = gr;

    monsters = new ArrayList<>();
    towers = new ArrayList<>();
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
          waveMonsters.add(randomPosition, newMonster);
        }
      }

      long delayPerUnit = (long) wave.getDuration() * 1000 / totalMonster;

      for (int j = 0; j < waveMonsters.size(); j++) {
        monsters.add(new Tuple<Long, Monster>(totalDelay + j * delayPerUnit, waveMonsters.get(j)));
      }

      totalDelay += wave.getDuration() * 1000;

      if (i == 0)
        waveInfo.add(new Tuple<Integer, Long>(i + 1, (long) (wave.getPreWavePause() * 1000)));
      else
        waveInfo.add(
            new Tuple<Integer, Long>(i + 1, (long) ((wave.getPreWavePause() + waves.get(i - 1).getDuration()) * 1000)));
    }

    waveInfo.add(new Tuple<Integer, Long>(-1, 0L));
  }

  public List<Tuple<Long, Monster>> getMonsters() {
    return monsters;
  }

  private void generateButtons() {
    Button fastForward = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 10, "2x speed", "FF", 'f',
        false) {

      @Override
      public void toggleOn() {
        changeGameSpeed(2);
      }

      @Override
      public void toggleOff() {
        if (gameSpeed == 2)
          changeGameSpeed(1);
      }

      @Override
      public void onHover() {
      }

    };
    buttons.add(fastForward);

    Button pause = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 60, "PAUSE", "P", 'p', false) {

      @Override
      public void toggleOn() {
        changeGameSpeed(0);
      }

      @Override
      public void toggleOff() {
        if (gameSpeed == 0)
          changeGameSpeed(1);
      }

      @Override
      public void onHover() {
      }

    };
    buttons.add(pause);

    Button buildTower = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 110, "Build tower", "T",
        't', true) {

      @Override
      public void toggleOn() {
        towerMode = true;
      }

      @Override
      public void toggleOff() {
        towerMode = false;
      }

      @Override
      public void onHover() {
        cost = (int) gr.towerCost + isUpgradeRange * 20 + isUpgradeDamage * 20 + isUpgradeFiringSpeed * 20;
      }

    };
    buttons.add(buildTower);

    Button upgradeRange = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 160, "Upgrade range", "U1",
        '1', false) {

      @Override
      public void toggleOn() {
        isUpgradeRange = 1;
      }

      @Override
      public void toggleOff() {
        isUpgradeRange = 0;
      }

      @Override
      public void onHover() {
      }

    };
    buttons.add(upgradeRange);

    Button upgradeSpeed = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 210, "Upgrade speed", "U2",
        '2', false) {

      @Override
      public void toggleOn() {
        isUpgradeFiringSpeed = 1;
      }

      @Override
      public void toggleOff() {
        isUpgradeFiringSpeed = 0;
      }

      @Override
      public void onHover() {
      }

    };
    buttons.add(upgradeSpeed);

    Button upgradeDamage = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 260, "Upgrade damage",
        "U3", '3', false) {

      @Override
      public void toggleOn() {
        isUpgradeDamage = 1;
      }

      @Override
      public void toggleOff() {
        isUpgradeDamage = 0;
      }

      @Override
      public void onHover() {
      }

    };
    buttons.add(upgradeDamage);

    Button manaPool = new Button(app, App.BOARD_WIDTH * App.CELLSIZE + 10, App.TOPBAR + 310,
        "Mana pool cost: " + (int) poolManaCost,
        "M", 'm', true) {

      @Override
      public void toggleOn() {
        if (mana >= poolManaCost) {
          mana -= poolManaCost;
          manaCap *= gr.manaPoolSpellCapMultiplier;
          additionalManaGainMultiplier += gr.manaPoolSpellManaGainedMultiplier - 1;
          poolManaCost += gr.manaPoolSpellCostIncreasePerUse;
          description = "Mana pool cost: " + (int) poolManaCost;
        }
        isClicked = false;
      }

      @Override
      public void toggleOff() {
      }

      @Override
      public void onHover() {
        cost = (int) poolManaCost;
      }

    };
    buttons.add(manaPool);
  }

  public List<Button> getButtons() {
    return buttons;
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

  private void changeGameSpeed(int value) {
    gameSpeed = value;
    for (Tuple<Long, Monster> tuple : monsters) {
      tuple.getValue().setSpeed(gameSpeed);
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

  public void setMana(float mana) {
    this.mana = mana;
  }

  public void onMouseClickedOnTheBoard() {
    int boardSize = App.BOARD_WIDTH * App.CELLSIZE;
    Tile[][] tiles = gr.getTiles();
    if (towerMode && app.mouseX >= 0 && app.mouseX <= boardSize && app.mouseY >= App.TOPBAR
        && app.mouseY <= App.TOPBAR + boardSize) {
      int i = (app.mouseY - App.TOPBAR) / App.CELLSIZE;
      int j = app.mouseX / App.CELLSIZE;

      if (tiles[i][j] instanceof GrassTile) {
        float range = gr.initialTowerRange;
        float damage = gr.initialTowerDamage;
        float firingSpeed = gr.initialTowerFiringSpeed;
        Tower newTower = new Tower(app, App.CELLSIZE, App.CELLSIZE, tiles[i][j].getX(), tiles[i][j].getY(), gr.tower0,
            gr.tower1, gr.tower2, range, damage, firingSpeed) {

          @Override
          public void toggleOn() {
            if (isUpgradeRange == 1 && mana >= (10 + (rangeLv + 1) * 10)) {
              mana -= 10 + (rangeLv + 1) * 10;
              rangeLv += 1;
            }
            if (isUpgradeFiringSpeed == 1 && mana >= (10 + (firingSpeedLv + 1) * 10)) {
              mana -= 10 + (firingSpeedLv + 1) * 10;
              firingSpeedLv += 1;
            }
            if (isUpgradeDamage == 1 && mana >= (10 + (damageLv + 1) * 10)) {
              mana -= 10 + (damageLv + 1) * 10;
              damageLv += 1;
            }
          }

          @Override
          public void toggleOff() {
          }

          @Override
          public void onHover() {
            shouldShowDamageUpgradeCost = isUpgradeDamage;
            shouldShowRangeUpgradeCost = isUpgradeRange;
            shouldShowFiringSpeedUpgradeCost = isUpgradeFiringSpeed;
          }
        };
        towers.add(newTower);
        tiles[i][j] = newTower;
      }
    }
  }

  public List<Tower> getTowers() {
    return towers;
  }
}
