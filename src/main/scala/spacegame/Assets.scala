package spacegame

import scalafx.scene.image.Image
import spacegame.model.{Size, SpriteSizes}

/** Images bundled under `src/main/resources/images`. Load only after JavaFX has started. */
final class Assets:
  val player: Image = load("player.png")
  val enemy: Image = load("enemy.png")
  val bullet: Image = load("bullet.png")
  val startScreen: Image = load("start_screen.png")
  val gameOverScreen: Image = load("game_over_screen.png")
  val startButton: Image = load("start_button.png")
  val restartButton: Image = load("restart_button.png")
  val exitButton: Image = load("exit_button.png")

  val spriteSizes: SpriteSizes = SpriteSizes(sizeOf(player), sizeOf(enemy), sizeOf(bullet))

  private def load(name: String): Image =
    val url = Option(getClass.getResource(s"/images/$name"))
      .getOrElse(throw IllegalStateException(s"Missing image resource: images/$name"))
    Image(url.toString)

  private def sizeOf(image: Image): Size = Size(image.width.value, image.height.value)
