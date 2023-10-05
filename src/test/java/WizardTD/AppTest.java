package WizardTD;

import processing.core.PImage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class AppTest {

  private App app;

  @BeforeAll
  public void setUp() {
    app = new App();
  }

  @Test
  public void testAppInitialization() {
    assertEquals("config.json", app.configPath);
    assertEquals(32, App.CELLSIZE);
    assertEquals(120, App.SIDEBAR);
    assertEquals(40, App.TOPBAR);
    assertEquals(20, App.BOARD_WIDTH);
    assertEquals(760, App.WIDTH);
    assertEquals(680, App.HEIGHT);
    assertEquals(60, App.FPS);
  }

  @Test
  public void testRotationFunctionality() {
    PImage original = new PImage(10, 10);

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        original.set(i, j, i * j);
      }
    }

    PImage rotated = App.rotateImageByDegrees(original, 90, app);

    assertEquals(10, rotated.width);
    assertEquals(10, rotated.height);
  }
}
