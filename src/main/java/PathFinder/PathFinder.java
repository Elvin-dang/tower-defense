package PathFinder;

import java.util.ArrayList;
import java.util.List;

import Tiles.ManipulatedTile;
import Tiles.Tile;

public class PathFinder {
  private Tile[][] tiles;

  public PathFinder(Tile[][] tiles) {
    this.tiles = tiles;
  }

  public List<ManipulatedTile> getShortestPath(ManipulatedTile start, ManipulatedTile end) {
    List<ManipulatedTile> visitedTile = new ArrayList<>();
    List<ManipulatedTile> openSet = new ArrayList<>();

    ManipulatedTile startTile = (ManipulatedTile) start;
    startTile.g = 0;
    startTile.f = heuristic(start, end);
    openSet.add(startTile);

    while (openSet.size() > 0) {
      ManipulatedTile current = getMin(openSet);

      if (current == end)
        break;

      visitedTile.add(current);
      openSet.remove(current);

      List<ManipulatedTile> neighbors = current.getNeighborTiles(tiles);

      for (ManipulatedTile neighbor : neighbors) {
        if (!visitedTile.contains(neighbor)) {
          int tempG = neighbor.g + 1;
          if (openSet.contains(neighbor)) {
            if (tempG < neighbor.g) {
              neighbor.g = tempG;
            }
          } else {
            neighbor.g = tempG;
            openSet.add(neighbor);
          }
          neighbor.f = neighbor.g + heuristic(neighbor, end);
          neighbor.setPrevTile(current);
        }
      }
    }

    return getTilesInShortestPath(end);
  }

  private int heuristic(Tile start, Tile end) {
    return Math.abs(start.row() - end.row()) + Math.abs(start.col() - start.col());
  }

  private ManipulatedTile getMin(List<ManipulatedTile> openSet) {
    int min = 0;
    for (int i = 1; i < openSet.size(); i++) {
      if (openSet.get(i).f < (openSet.get(min).f))
        min = i;
    }
    return openSet.get(min);
  }

  private List<ManipulatedTile> getTilesInShortestPath(ManipulatedTile end) {
    List<ManipulatedTile> path = new ArrayList<>();
    ManipulatedTile current = end;
    while (current != null) {
      path.add(0, current);
      current = current.getPrevTile();
    }
    return path;
  }
}
