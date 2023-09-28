package Monsters;

import java.util.Map;

public class Wave {
  private float duration;
  private float preWavePause;
  private Map<Monster, Integer> monsters;

  public Wave(float duration, float preWavePause, Map<Monster, Integer> monsters) {
    this.duration = duration;
    this.preWavePause = preWavePause;
    this.monsters = monsters;
  }

  public float getDuration() {
    return duration;
  }

  public float getPreWavePause() {
    return preWavePause;
  }

  public Map<Monster, Integer> getMonsters() {
    return monsters;
  }

}
