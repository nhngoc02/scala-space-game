package spacegame.model

import scala.util.Random

enum Screen:
  case Start, Playing, GameOver

/** The whole game: which screen is showing, the current world, and its rewind history.
  *
  * Holding the rewind key steps backwards through recent frames instead of forwards.
  */
final case class Game(screen: Screen, world: World, history: List[World])(using settings: Settings):

  def start: Game = copy(screen = Screen.Playing)

  def restart(using SpriteSizes): Game = Game(Screen.Playing, World.initial, Nil)

  /** How much rewind is stored, from 0.0 (none) to 1.0 (full). */
  def rewindFill: Double = history.size.toDouble / settings.rewindFrames

  def tick(input: Input, rewinding: Boolean, rng: Random): Game =
    if screen != Screen.Playing then this
    else if rewinding then
      history match
        case previous :: older => copy(world = previous, history = older)
        case Nil               => this
    else
      val next = world.step(input, rng)
      copy(
        screen = if next.isGameOver then Screen.GameOver else Screen.Playing,
        world = next,
        history = (world :: history).take(settings.rewindFrames)
      )

object Game:
  def initial(using Settings, SpriteSizes): Game = Game(Screen.Start, World.initial, Nil)
