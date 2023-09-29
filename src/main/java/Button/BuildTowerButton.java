package Button;

import WizardTD.App;
import WizardTD.GameController;

public class BuildTowerButton extends Button {
  public BuildTowerButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.setTowerMode(true);
  }

  @Override
  public void toggleOff() {
    gc.setTowerMode(false);
  }

  @Override
  public void onHover() {
    cost = (int) app.gr.towerCost + gc.getIsUpgradeRange() * 20 + gc.getIsUpgradeDamage() * 20
        + gc.getIsUpgradeFiringSpeed() * 20;
  }
}
