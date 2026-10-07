package spacegame.model

import spacegame.model.Fixtures.given
import spacegame.util.Vec2

import scala.util.Random

class WorldSuite extends munit.FunSuite:
  private val rng = Random(42)
  private val noInput = Input()

  test("a new world has a full swarm and starting lives") {
    val w = World.initial
    assertEquals(w.swarm.size, 12)
    assertEquals(w.lives, 5)
    assertEquals(w.score, 0)
  }

  test("firing is limited by the cooldown") {
    val fire = Input(fire = true)
    val afterFirst = Fixtures.emptyWorld.step(fire, rng)
    assertEquals(afterFirst.playerBullets.size, 1)
    val afterSecond = afterFirst.step(fire, rng)
    assertEquals(afterSecond.playerBullets.size, 1)
    val cooledDown = (1 to 20).foldLeft(afterFirst)((w, _) => w.step(noInput, rng))
    assertEquals(cooledDown.step(fire, rng).playerBullets.size, 2)
  }

  test("enemies fire on the fire interval") {
    val w = Fixtures.emptyWorld.copy(frame = 40)
    assertEquals(w.step(noInput, rng).enemyBullets.size, 1)
    assertEquals(w.copy(frame = 41).step(noInput, rng).enemyBullets.size, 0)
  }

  test("hitting an enemy removes it and scores a point") {
    val enemy = Enemy(Vec2(500, 300), Fixtures.sizes.enemy, row = 0)
    val bullet = Bullet(Vec2(510, 310), Vec2.Zero, Fixtures.sizes.bullet)
    val other = Enemy(Vec2(0, 0), Fixtures.sizes.enemy, row = 0)
    val w = Fixtures.emptyWorld.copy(swarm = EnemySwarm(Vector(enemy, other)), playerBullets = Vector(bullet))
    val next = w.step(noInput, rng)
    assertEquals(next.score, 1)
    assertEquals(next.swarm.size, 1)
    assert(next.playerBullets.isEmpty)
  }

  test("getting shot costs a life and resets the player") {
    val w0 = Fixtures.emptyWorld
    val moved = w0.copy(player = w0.player.copy(pos = Vec2(100, 600)))
    val shot = Bullet(Vec2(110, 610), Vec2.Zero, Fixtures.sizes.bullet)
    val next = moved.copy(enemyBullets = Vector(shot)).step(noInput, rng)
    assertEquals(next.lives, 4)
    assertEquals(next.player.pos, w0.player.pos)
    assert(next.enemyBullets.isEmpty)
  }

  test("several hits in one frame end the game instead of going below zero lives") {
    val w0 = Fixtures.emptyWorld.copy(lives = 1)
    val p = w0.player.pos
    val shots = Vector.fill(3)(Bullet(p + Vec2(10, 10), Vec2.Zero, Fixtures.sizes.bullet))
    val next = w0.copy(enemyBullets = shots).step(noInput, rng)
    assertEquals(next.lives, 0)
    assert(next.isGameOver)
  }

  test("colliding bullets cancel each other one for one") {
    val at = Vec2(300, 300)
    val w = Fixtures.emptyWorld.copy(
      playerBullets = Vector(Bullet(at, Vec2.Zero, Fixtures.sizes.bullet)),
      enemyBullets = Vector.fill(2)(Bullet(at, Vec2.Zero, Fixtures.sizes.bullet))
    )
    val next = w.step(noInput, rng)
    assert(next.playerBullets.isEmpty)
    assertEquals(next.enemyBullets.size, 1)
  }

  test("a cleared swarm respawns") {
    val w = Fixtures.emptyWorld.copy(swarm = EnemySwarm(Vector.empty))
    assertEquals(w.step(noInput, rng).swarm.size, 12)
  }
