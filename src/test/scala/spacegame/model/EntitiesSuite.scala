package spacegame.model

import spacegame.util.Vec2

class EntitiesSuite extends munit.FunSuite:
  private val arena = Size(100, 100)
  private val size = Size(10, 10)

  test("player spawns at the bottom centre") {
    assertEquals(Player.spawn(size, arena).pos, Vec2(45, 90))
  }

  test("player cannot leave the arena") {
    val atLeftEdge = Player(Vec2(2, 50), size)
    assertEquals(atLeftEdge.moved(Vec2(-5, 0), arena).pos, Vec2(2, 50))
    assertEquals(atLeftEdge.moved(Vec2(5, -5), arena).pos, Vec2(7, 45))
    val atBottom = Player(Vec2(50, 88), size)
    assertEquals(atBottom.moved(Vec2(0, 5), arena).pos, Vec2(50, 88))
  }

  test("bullets move by their velocity and then accelerate") {
    val b = Bullet(Vec2(0, 50), Vec2(0, -5), Size(1, 1)).stepped(Vec2(0, -0.1))
    assertEquals(b.pos, Vec2(0, 45))
    assertEqualsDouble(b.vel.y, -5.1, 1e-9)
  }

  test("bullets above or below the arena are off screen") {
    assert(Bullet(Vec2(0, -1), Vec2.Zero, size).isOffScreen(arena))
    assert(Bullet(Vec2(0, 101), Vec2.Zero, size).isOffScreen(arena))
    assert(!Bullet(Vec2(0, 50), Vec2.Zero, size).isOffScreen(arena))
  }
