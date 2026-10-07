package spacegame

import scalafx.scene.image.Image
import spacegame.model.Size
import spacegame.util.Rect

/** Where the menu buttons sit, shared by drawing and click handling so they can't drift apart. */
final class Layout(assets: Assets, arena: Size):
  private def centredX(image: Image): Double = (arena.width - image.width.value) / 2

  private def rect(image: Image, y: Double): Rect =
    Rect(centredX(image), y, image.width.value, image.height.value)

  private val startTop = (arena.height - assets.startButton.height.value) / 2 + 15
  val startButton: Rect = rect(assets.startButton, startTop)
  val startExitButton: Rect = rect(assets.exitButton, startButton.bottom + 5).copy(x = startButton.x)

  private val restartTop = (arena.height - assets.restartButton.height.value) / 2
  val restartButton: Rect = rect(assets.restartButton, restartTop)
  val gameOverExitButton: Rect = rect(assets.exitButton, restartButton.bottom + 3).copy(x = restartButton.x)
