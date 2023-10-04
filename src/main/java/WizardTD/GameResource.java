package WizardTD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Monsters.Beetle;
import Monsters.Gremlin;
import Monsters.Monster;
import Monsters.Wave;
import Monsters.Worm;
import Navigation.Navigation;
import Navigation.Tuple;
import Navigation.Vector;
import PathFinder.PathFinder;
import Tiles.GrassTile;
import Tiles.ManipulatedTile;
import Tiles.PathTile;
import Tiles.ShrubTile;
import Tiles.Tile;
import Tiles.WizardHouseTile;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.sound.SoundFile;

public class GameResource {
  private App app;
  public PImage beetle;
  public PImage fireball;
  public PImage grass;
  public PImage gremlin;
  public PImage gremlin1;
  public PImage gremlin2;
  public PImage gremlin3;
  public PImage gremlin4;
  public PImage gremlin5;
  public PImage path0;
  public PImage path1;
  public PImage path2;
  public PImage path3;
  public PImage shrub;
  public PImage tower0;
  public PImage tower1;
  public PImage tower2;
  public PImage wizardHouse;
  public PImage worm;
  public SoundFile shoot;
  public SoundFile shoot1;
  public SoundFile shoot2;
  public SoundFile dead;
  public SoundFile music;

  private JSONObject json;

  private Tile[][] tiles;
  private WizardHouseTile wizardHouseTile;
  private List<PathTile> spawnTiles;
  private List<Wave> waves;

  public String layout;
  public float initialTowerRange;
  public float initialTowerFiringSpeed;
  public float initialTowerDamage;
  public float initialMana;
  public float initialManaCap;
  public float initialManaPerSecond;
  public float towerCost;
  public float manaPoolSpellInitialCost;
  public float manaPoolSpellCostIncreasePerUse;
  public float manaPoolSpellCapMultiplier;
  public float manaPoolSpellManaGainedMultiplier;

  public GameResource(App app, String configPath) {
    tiles = new Tile[App.BOARD_WIDTH][App.BOARD_WIDTH];
    spawnTiles = new ArrayList<PathTile>();
    waves = new ArrayList<>();

    this.app = app;

    beetle = app.loadImage("src/main/resources/WizardTD/beetle.png");
    fireball = app.loadImage("src/main/resources/WizardTD/fireball.png");
    grass = app.loadImage("src/main/resources/WizardTD/grass.png");
    gremlin = app.loadImage("src/main/resources/WizardTD/gremlin.png");
    gremlin1 = app.loadImage("src/main/resources/WizardTD/gremlin1.png");
    gremlin2 = app.loadImage("src/main/resources/WizardTD/gremlin2.png");
    gremlin3 = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
    gremlin4 = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
    gremlin5 = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
    path0 = app.loadImage("src/main/resources/WizardTD/path0.png");
    path1 = app.loadImage("src/main/resources/WizardTD/path1.png");
    path2 = app.loadImage("src/main/resources/WizardTD/path2.png");
    path3 = app.loadImage("src/main/resources/WizardTD/path3.png");
    shrub = app.loadImage("src/main/resources/WizardTD/shrub.png");
    tower0 = app.loadImage("src/main/resources/WizardTD/tower0.png");
    tower1 = app.loadImage("src/main/resources/WizardTD/tower1.png");
    tower2 = app.loadImage("src/main/resources/WizardTD/tower2.png");
    wizardHouse = app.loadImage("src/main/resources/WizardTD/wizard_house.png");
    worm = app.loadImage("src/main/resources/WizardTD/worm.png");

    shoot = new SoundFile(app, "src/main/resources/WizardTD/shoot.wav");
    shoot1 = new SoundFile(app, "src/main/resources/WizardTD/shoot1.wav");
    shoot2 = new SoundFile(app, "src/main/resources/WizardTD/shoot2.wav");
    dead = new SoundFile(app, "src/main/resources/WizardTD/dead.wav");
    music = new SoundFile(app, "src/main/resources/WizardTD/music.wav");
    shoot.rate(App.FPS);
    shoot1.rate(App.FPS);
    shoot2.rate(App.FPS);
    dead.rate(App.FPS);
    shoot.amp(0.5f);
    shoot1.amp(0.5f);
    shoot2.amp(0.5f);
    dead.amp(0.5f);
    music.amp(0.5f);
    music.play();
    music.loop();

    json = app.loadJSONObject(configPath);

    layout = json.getString("layout");
    initialTowerRange = json.getFloat("initial_tower_range");
    initialTowerFiringSpeed = json.getFloat("initial_tower_firing_speed");
    initialTowerDamage = json.getFloat("initial_tower_damage");
    initialMana = json.getFloat("initial_mana");
    initialManaCap = json.getFloat("initial_mana_cap");
    initialManaPerSecond = json.getFloat("initial_mana_gained_per_second");
    towerCost = json.getFloat("tower_cost");
    manaPoolSpellInitialCost = json.getFloat("mana_pool_spell_initial_cost");
    manaPoolSpellCostIncreasePerUse = json.getFloat("mana_pool_spell_cost_increase_per_use");
    manaPoolSpellCapMultiplier = json.getFloat("mana_pool_spell_cap_multiplier");
    manaPoolSpellManaGainedMultiplier = json.getFloat("mana_pool_spell_mana_gained_multiplier");

    setUpBoard();
    setUpWave();
  }

  private void setUpBoard() {
    Scanner sc = null;

    try {
      File f = new File(layout);
      sc = new Scanner(f);
      for (int i = 0; i < App.BOARD_WIDTH; i++) {
        String str = sc.nextLine();
        for (int j = 0; j < App.BOARD_WIDTH; j++) {
          if (j >= str.length() || str.charAt(j) == 32) {
            // grass
            tiles[i][j] = new GrassTile(app, App.CELLSIZE, App.CELLSIZE, j * App.CELLSIZE,
                i * App.CELLSIZE + App.TOPBAR,
                grass);
          } else if (str.charAt(j) == 83) {
            // shrub
            tiles[i][j] = new ShrubTile(app, App.CELLSIZE, App.CELLSIZE, j * App.CELLSIZE,
                i * App.CELLSIZE + App.TOPBAR,
                shrub);
          } else if (str.charAt(j) == 88) {
            // path
            tiles[i][j] = new PathTile(app, App.CELLSIZE, App.CELLSIZE, j * App.CELLSIZE, i * App.CELLSIZE + App.TOPBAR,
                path0);
          } else if (str.charAt(j) == 87) {
            // wizard's house
            tiles[i][j] = wizardHouseTile = new WizardHouseTile(app, App.CELLSIZE + 16, App.CELLSIZE + 16,
                j * App.CELLSIZE - 8,
                i * App.CELLSIZE - 8 + App.TOPBAR, wizardHouse, grass);
          } else
            throw new IllegalArgumentException("Invalid file input " + layout + " provided");
        }
      }
    } catch (FileNotFoundException e) {
      PApplet.println(e);
    } catch (IllegalArgumentException e) {
      PApplet.println(e);
    } finally {
      if (sc != null)
        sc.close();
    }

    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
        if (tiles[i][j] instanceof PathTile)
          calculateDirection(i, j);
      }
    }
  }

  private void calculateDirection(int i, int j) {
    boolean top = i - 1 >= 0 && tiles[i - 1][j] instanceof PathTile;
    boolean bottom = i + 1 < App.BOARD_WIDTH && tiles[i + 1][j] instanceof PathTile;
    boolean left = j - 1 >= 0 && tiles[i][j - 1] instanceof PathTile;
    boolean right = j + 1 < App.BOARD_WIDTH && tiles[i][j + 1] instanceof PathTile;

    PathTile t = (PathTile) tiles[i][j];

    if ((!top && !bottom && left && !right)) {
      t.setImage(path0);
      if (j == tiles.length - 1) {
        t.setStart(Vector.LEFT);
        spawnTiles.add((PathTile) tiles[i][j]);
      }
    } else if ((!top && !bottom && !left && right)) {
      t.setImage(path0);
      if (j == 0) {
        t.setStart(Vector.RIGHT);
        spawnTiles.add((PathTile) tiles[i][j]);
      }
    } else if ((top && !bottom && !left && !right)) {
      t.setImage(App.rotateImageByDegrees(path0, 90, app));
      if (i == tiles.length - 1) {
        t.setStart(Vector.UP);
        spawnTiles.add((PathTile) tiles[i][j]);
      }
    } else if ((!top && bottom && !left && !right)) {
      t.setImage(App.rotateImageByDegrees(path0, 90, app));
      if (i == 0) {
        t.setStart(Vector.DOWN);
        spawnTiles.add((PathTile) tiles[i][j]);
      }
    } else if (!top && !bottom && left && right) {
      t.setImage(path0);
    } else if (top && bottom && !left && !right) {
      t.setImage(App.rotateImageByDegrees(path0, 90, app));
    } else if (!top && bottom && left && !right) {
      t.setImage(path1);
    } else if (!top && bottom && !left && right) {
      t.setImage(App.rotateImageByDegrees(path1, -90, app));
    } else if (top && !bottom && left && !right) {
      t.setImage(App.rotateImageByDegrees(path1, 90, app));
    } else if (top && !bottom && !left && right) {
      t.setImage(App.rotateImageByDegrees(path1, 180, app));
    } else if (!top && bottom && left && right) {
      t.setImage(path2);
    } else if (top && !bottom && left && right) {
      t.setImage(App.rotateImageByDegrees(path2, 180, app));
    } else if (top && bottom && left && !right) {
      t.setImage(App.rotateImageByDegrees(path2, 90, app));
    } else if (top && bottom && !left && right) {
      t.setImage(App.rotateImageByDegrees(path2, -90, app));
    } else {
      t.setImage(path3);
    }
  }

  private void setUpWave() {
    JSONArray waveArray = json.getJSONArray("waves");
    for (int i = 0; i < waveArray.size(); i++) {
      JSONObject waveObject = waveArray.getJSONObject(i);
      float duration = waveObject.getFloat("duration");
      float preWavePause = waveObject.getFloat("pre_wave_pause");
      JSONArray monsterArray = waveObject.getJSONArray("monsters");
      Map<Monster, Integer> monsters = new HashMap<>();

      for (int j = 0; j < monsterArray.size(); j++) {
        JSONObject monsterObject = monsterArray.getJSONObject(j);
        float hp = monsterObject.getFloat("hp");
        float speed = monsterObject.getFloat("speed");
        float armour = monsterObject.getFloat("armour");
        float manaGainedOnKill = monsterObject.getFloat("mana_gained_on_kill");
        int quantity = monsterObject.getInt("quantity");

        Monster monster = categorizeMonster(monsterObject.getString("type"), hp, speed, armour, manaGainedOnKill);
        monsters.put(monster, quantity);
      }

      waves.add(new Wave(duration, preWavePause, monsters));
    }
  }

  private Monster categorizeMonster(String type, float hp, float speed, float amount, float manaGainedOnKill) {
    if (type.equals("gremlin"))
      return new Gremlin(app, this, gremlin, hp, speed, amount, manaGainedOnKill);
    if (type.equals("beetle"))
      return new Beetle(app, this, beetle, hp, speed, amount, manaGainedOnKill);
    if (type.equals("worm"))
      return new Worm(app, this, worm, hp, speed, amount, manaGainedOnKill);
    return null;
  }

  public Tile[][] getTiles() {
    Tile[][] newTiles = new Tile[App.BOARD_WIDTH][App.BOARD_WIDTH];
    for (int i = 0; i < App.BOARD_WIDTH; i++) {
      for (int j = 0; j < App.BOARD_WIDTH; j++) {
        newTiles[i][j] = tiles[i][j].duplicate();
      }
    }
    return newTiles;
  }

  public Tile getWizardHouseTile() {
    return wizardHouseTile;
  }

  public List<Navigation> getRoadForMonster() {
    List<Navigation> possibleRoad = new ArrayList<>();
    PathFinder pf = new PathFinder(tiles);
    for (PathTile spawnTile : spawnTiles) {
      List<ManipulatedTile> roadTiles = pf.getShortestPath(spawnTile, wizardHouseTile);
      List<Tuple<Vector, Float>> map = new ArrayList<>();
      Vector initialVector = Vector.get(roadTiles.get(0), null);
      float constrain = 26;
      for (int i = 0; i < roadTiles.size() - 1; i++) {
        Vector vector = Vector.get(roadTiles.get(i), roadTiles.get(i + 1));
        if (initialVector == vector)
          constrain += 32;
        else {
          map.add(new Tuple<Vector, Float>(initialVector, constrain));
          initialVector = vector;
          constrain = 32;
        }
      }
      possibleRoad.add(new Navigation(spawnTile, map));
    }
    return possibleRoad;
  }

  public List<Wave> getWaves() {
    return waves;
  }
}
