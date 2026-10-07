package spacegame

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import spacegame.model.{Entity, Game, Screen, Settings}
import spacegame.util.Rect

/** Draws a [[Game]] onto a canvas. Holds no game state of its own. */
final class Renderer(g: GraphicsContext, assets: Assets, layout: Layout)(using settings: Settings):
  private val background = Color.rgb(9, 19, 83)
  private val hudFont = Font("Arial", 20)
  private val titleFont = Font("Arial", 50)

  def draw(game: Game, rewinding: Boolean): Unit =
    game.screen match
      case Screen.Start    => drawStartScreen()
      case Screen.Playing  => drawPlaying(game, rewinding)
      case Screen.GameOver => drawGameOver(game.world.score)

  private def drawStartScreen(): Unit =
    g.drawImage(assets.startScreen, 0, 0)
    drawAt(assets.startButton, layout.startButton)
    drawAt(assets.exitButton, layout.startExitButton)

  private def drawGameOver(score: Int): Unit =
    g.drawImage(assets.gameOverScreen, 0, 0)
    g.fill = Color.White
    g.font = titleFont
    g.fillText(s"Score: $score", 400, 350)
    drawAt(assets.restartButton, layout.restartButton)
    drawAt(assets.exitButton, layout.gameOverExitButton)

  private def drawPlaying(game: Game, rewinding: Boolean): Unit =
    val world = game.world
    g.fill = background
    g.fillRect(0, 0, settings.arena.width, settings.arena.height)

    drawEntity(assets.player, world.player)
    world.swarm.enemies.foreach(drawEntity(assets.enemy, _))
    (world.playerBullets ++ world.enemyBullets).foreach(drawEntity(assets.bullet, _))

    g.fill = Color.White
    g.font = hudFont
    val lives = if world.lives == 1 then "1 life remaining" else s"${world.lives} lives remaining"
    g.fillText(s"Player Score: ${world.score}\n$lives", 800, 400)

    if rewinding then
      g.fillText("Rewind time", 800, 480)
      g.stroke = Color.White
      g.strokeRect(800, 500, 100, 20)
      g.fillRect(800, 500, 100 * game.rewindFill, 20)

  private def drawEntity(image: Image, entity: Entity): Unit =
    g.drawImage(image, entity.pos.x, entity.pos.y)

  private def drawAt(image: Image, at: Rect): Unit =
    g.drawImage(image, at.x, at.y)
