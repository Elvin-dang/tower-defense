package Button;

import WizardTD.App;

public abstract class Button implements Clickable, Hoverable, HotKeyMountable {
  protected final App app;
  protected int x;
  protected int y;
  protected String description;
  protected String name;
  protected char hotKey;
  protected boolean showTooltip;

  protected boolean isClicked = false;
  protected boolean isHovered = false;
  protected int cost = 0;

  public Button(App app, int x, int y, String description, String name, char hotKey, boolean showTooltip) {
    this.app = app;
    this.x = x;
    this.y = y;
    this.description = description;
    this.name = name;
    this.hotKey = hotKey;
    this.showTooltip = showTooltip;
  }

  public void display() {
    if (isClicked) {
      app.fill(255, 255, 4);
    } else {
      app.fill(131, 115, 73);
    }
    if (isHovered) {
      onHover();
      if (showTooltip) {
        app.fill(255);
        app.stroke(0);
        app.strokeWeight(1);
        app.rect(x - 90, y, 70, 20);
        app.fill(0);
        app.textSize(12);
        app.text("Cost: " + cost, x - 87, y + 15);
      }
      if (!isClicked)
        app.fill(206, 206, 206);
      else
        app.fill(255, 255, 4);
    }
    app.stroke(0);
    app.strokeWeight(3);
    app.rect(x, y, 40, 40);
    app.fill(0);
    app.textSize(25);
    app.text(name, x + 5, y + 30);
    app.textSize(12);
    app.text(description, x + 45, y + 5, 65, 50);
  }

  @Override
  public void mouseMoved() {
    if (app.mouseX >= x && app.mouseX <= x + 46 && app.mouseY >= y && app.mouseY <= y + 46) {
      isHovered = true;
    } else {
      isHovered = false;
    }
  }

  @Override
  public void mousePressed() {
    if (app.mouseX >= x && app.mouseX <= x + 46 && app.mouseY >= y && app.mouseY <= y + 46) {
      if (isClicked) {
        isClicked = !isClicked;
        toggleOff();
      } else {
        isClicked = !isClicked;
        toggleOn();
      }
    }
  }

  @Override
  public void keyPressed() {
    if (app.key == hotKey || app.key == hotKey - 32) {
      if (isClicked) {
        isClicked = !isClicked;
        toggleOff();
      } else {
        isClicked = !isClicked;
        toggleOn();
      }
    }
  }
}
