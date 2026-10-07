package spacegame.model

import spacegame.model.Fixtures.given

import scala.util.Random

class GameSuite extends munit.FunSuite:
  private val rng = Random(1)
  private val noInput = Input()

  test("nothing advances until the game is started") {
    val g = Game.initial
    assertEquals(g.tick(noInput, rewinding = false, rng), g)
    assertEquals(g.start.tick(noInput, rewinding = false, rng).world.frame, 1)
  }

  test("rewinding steps back through earlier frames") {
    val played = (1 to 5).foldLeft(Game.initial.start)((g, _) => g.tick(noInput, rewinding = false, rng))
    assertEquals(played.world.frame, 5)
    val rewound = played.tick(noInput, rewinding = true, rng).tick(noInput, rewinding = true, rng)
    assertEquals(rewound.world.frame, 3)
  }

  test("rewinding with no history does nothing") {
    val g = Game.initial.start
    assertEquals(g.tick(noInput, rewinding = true, rng), g)
  }

  test("rewind history is capped") {
    given Settings = Settings(rewindFrames = 3)
    val played = (1 to 10).foldLeft(Game.initial.start)((g, _) => g.tick(noInput, rewinding = false, rng))
    assertEquals(played.history.size, 3)
    assertEqualsDouble(played.rewindFill, 1.0, 1e-9)
  }

  test("losing the last life shows the game over screen, and restart resets") {
    val g = Game.initial.start
    val dying = g.copy(world = g.world.copy(lives = 0))
    val over = dying.tick(noInput, rewinding = false, rng)
    assertEquals(over.screen, Screen.GameOver)
    val again = over.restart
    assertEquals(again.screen, Screen.Playing)
    assertEquals(again.world.lives, 5)
    assert(again.history.isEmpty)
  }
