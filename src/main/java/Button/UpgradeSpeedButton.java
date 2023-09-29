package Button;

import WizardTD.App;
import WizardTD.GameController;

public class UpgradeSpeedButton extends Button {
  public UpgradeSpeedButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.setIsUpgradeFiringSpeed(1);
  }

  @Override
  public void toggleOff() {
    gc.setIsUpgradeFiringSpeed(0);
  }

  @Override
  public void onHover() {
  }
}
