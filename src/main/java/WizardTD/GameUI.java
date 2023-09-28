package WizardTD;

import java.util.List;

import Button.Button;
import Monsters.Monster;
import Navigation.Tuple;
import Tiles.Tile;
import Tiles.WizardHouseTile;
import Towers.Tower;

public class GameUI {
  private final App app;
  private final GameResource gr;
  private final GameController gc;

  private int waveNumber = 0;

  public GameUI(App app, GameResource gr, GameController gc) {
    this.app = app;
    this.gr = gr;
    this.gc = gc;
  }

  public void displayTopBar() {
    app.fill(131, 115, 73);
    app.noStroke();
    app.rect(0, 0, App.WIDTH, App.TOPBAR);

    app.fill(255);
    app.stroke(0);
    app.strokeWeight(2);
    app.rect(375, 10, 350, 20);
    app.fill(0, 214, 213);
    app.rect((float) 375, (float) 10, gc.getMana() / gc.getManaCap() * 350, (float) 20);

    long currentCountDown = gc.getCountDown();
    List<Tuple<Integer, Long>> waveInfo = gc.getWaveInfo();
    if (waveInfo.size() > 0) {
      if (currentCountDown <= 0) {
        gc.setCountDown(waveInfo.get(0).getValue());
        waveNumber = waveInfo.get(0).getKey();
        waveInfo.remove(0);
      }
      if (waveNumber != -1) {
        app.fill(0);
        app.textSize(25);
        app.text("Wave " + waveNumber + " starts: " + gc.getCountDown() / 1000, 10, 30);
      }
    }

    app.fill(0);
    app.textSize(18);
    app.text("MANA:", 310, 27);
    app.text((long) gc.getMana() + " / " + (long) gc.getManaCap(), 500, 27);
  }

  public void displaySideBar() {
    app.fill(131, 115, 73);
    app.noStroke();
    app.rect(App.BOARD_WIDTH * App.CELLSIZE, App.TOPBAR, App.SIDEBAR, App.BOARD_WIDTH * App.CELLSIZE);

    List<Button> buttons = gc.getButtons();
    for (Button button : buttons) {
      button.display();
    }
  }

  public void displayBoard() {
    Tile[][] tiles = gr.getTiles();
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
        if (tiles[i][j] instanceof WizardHouseTile)
          continue;
        tiles[i][j].display();
      }
    }
  }

  public void displayMonster() {
    long currentTimer = gc.getTimer();
    List<Tuple<Long, Monster>> monsters = gc.getMonsters();
    for (Tuple<Long, Monster> tuple : monsters) {
      if (tuple.getKey() <= currentTimer)
        tuple.getValue().act();
    }
  }

  public void displayTower() {
    List<Tower> towers = gc.getTowers();
    for (Tower tower : towers) {
      tower.display();
    }
  }

  public void displayWizardHouse() {
    gr.getWizardHouseTile().display();
  }

  public void onMouseMoved() {
    List<Button> buttons = gc.getButtons();
    for (Button button : buttons) {
      button.mouseMoved();
    }
    List<Tower> towers = gc.getTowers();
    for (Tower tower : towers) {
      tower.mouseMoved();
    }
  }

  public void onMousePressed() {
    List<Button> buttons = gc.getButtons();
    for (Button button : buttons) {
      button.mousePressed();
    }
    List<Tower> towers = gc.getTowers();
    for (Tower tower : towers) {
      tower.mousePressed();
    }
    gc.onMouseClickedOnTheBoard();
  }

  public void onKeyPressed() {
    List<Button> buttons = gc.getButtons();
    for (Button button : buttons) {
      button.keyPressed();
    }
  }
}