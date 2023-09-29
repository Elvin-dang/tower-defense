package Button;

import WizardTD.App;
import WizardTD.GameController;

public class UpgradeRangeButton extends Button {
  public UpgradeRangeButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.setIsUpgradeRange(1);
  }

  @Override
  public void toggleOff() {
    gc.setIsUpgradeRange(0);
  }

  @Override
  public void onHover() {
  }
}
