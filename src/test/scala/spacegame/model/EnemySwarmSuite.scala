package spacegame.model

import spacegame.util.Vec2

import scala.util.Random

class EnemySwarmSuite extends munit.FunSuite:
  private val enemySize = Size(10, 10)
  private val swarm = EnemySwarm.grid(rows = 3, cols = 4, enemySize, Size(1000, 800))

  test("grid lays out rows x cols enemies tagged with their row") {
    assertEquals(swarm.size, 12)
    assertEquals(swarm.enemies.map(_.row).distinct, Vector(0, 1, 2))
  }

  test("even and odd rows sway independently") {
    val moved = swarm.sway(evenRows = Vec2(-1, 0), oddRows = Vec2(1, 0))
    swarm.enemies.zip(moved.enemies).foreach { (before, after) =>
      val expected = if before.row % 2 == 0 then -1.0 else 1.0
      assertEquals(after.pos.x - before.pos.x, expected)
    }
  }

  test("hitBy removes only the enemies that were hit") {
    val target = swarm.enemies.head
    val bullet = Bullet(target.pos, Vec2.Zero, Size(2, 2))
    val (after, hit) = swarm.hitBy(bullet)
    assert(hit)
    assertEquals(after.size, 11)
    assert(!after.enemies.contains(target))
  }

  test("an empty swarm cannot shoot") {
    assertEquals(EnemySwarm(Vector.empty).shoot(Random(0), Size(1, 1), 5), None)
  }
