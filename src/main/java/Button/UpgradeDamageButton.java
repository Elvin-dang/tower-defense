package Button;

import WizardTD.App;
import WizardTD.GameController;

public class UpgradeDamageButton extends Button {
  public UpgradeDamageButton(App app, GameController gc, int x, int y, String description, String name, char hotKey,
      boolean showTooltip) {
    super(app, gc, x, y, description, name, hotKey, showTooltip);
  }

  @Override
  public void toggleOn() {
    gc.setIsUpgradeDamage(1);
  }

  @Override
  public void toggleOff() {
    gc.setIsUpgradeDamage(0);
  }

  @Override
  public void onHover() {
  }
}
