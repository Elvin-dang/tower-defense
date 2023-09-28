package Navigation;

import java.util.List;

import Tiles.ManipulatedTile;

public class Navigation {
  private ManipulatedTile spawnTile;
  private List<Tuple<Vector, Float>> road;

  public Navigation(ManipulatedTile spawnTile, List<Tuple<Vector, Float>> road) {
    this.spawnTile = spawnTile;
    this.road = road;
  }

  public ManipulatedTile getSpawnTile() {
    return spawnTile;
  }

  public List<Tuple<Vector, Float>> getRoad() {
    return road;
  }

  @Override
  public String toString() {
    return "Navigation [spawnTile=" + spawnTile + ", road=" + road + "]";
  }

}
