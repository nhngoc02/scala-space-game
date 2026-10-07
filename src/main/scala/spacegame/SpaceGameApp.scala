package spacegame

import scalafx.Includes.*
import scalafx.animation.AnimationTimer
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import spacegame.model.{Game, Input, Screen, Settings, SpriteSizes}

import scala.collection.mutable
import scala.util.Random

/** Entry point: wires keyboard and mouse input to the game model and redraws every frame. */
object SpaceGameApp extends JFXApp3:

  given Settings = Settings()

  override def start(): Unit =
    val settings = summon[Settings]
    val assets = Assets()
    given SpriteSizes = assets.spriteSizes

    val canvas = Canvas(settings.arena.width, settings.arena.height)
    val layout = Layout(assets, settings.arena)
    val renderer = Renderer(canvas.graphicsContext2D, assets, layout)
    val rng = Random()
    val keysDown = mutable.Set.empty[KeyCode]
    var game = Game.initial

    def held(codes: KeyCode*): Boolean = codes.exists(keysDown.contains)

    canvas.onKeyPressed = (e: KeyEvent) => keysDown += e.code
    canvas.onKeyReleased = (e: KeyEvent) => keysDown -= e.code

    canvas.onMouseClicked = (e: MouseEvent) =>
      game.screen match
        case Screen.Start =>
          if layout.startButton.contains(e.x, e.y) then game = game.start
          else if layout.startExitButton.contains(e.x, e.y) then Platform.exit()
        case Screen.GameOver =>
          if layout.restartButton.contains(e.x, e.y) then game = game.restart
          else if layout.gameOverExitButton.contains(e.x, e.y) then Platform.exit()
        case Screen.Playing => ()

    val timer = AnimationTimer { _ =>
      val input = Input(
        left = held(KeyCode.Left, KeyCode.A),
        right = held(KeyCode.Right, KeyCode.D),
        up = held(KeyCode.Up, KeyCode.W),
        down = held(KeyCode.Down, KeyCode.S),
        fire = held(KeyCode.Space)
      )
      val rewinding = held(KeyCode.R)
      game = game.tick(input, rewinding, rng)
      renderer.draw(game, rewinding)
    }

    stage = new JFXApp3.PrimaryStage:
      title = "Space Game"
      resizable = false
      scene = new Scene(settings.arena.width, settings.arena.height):
        content = canvas

    canvas.requestFocus()
    timer.start()
