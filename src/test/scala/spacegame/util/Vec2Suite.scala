package spacegame.util

class Vec2Suite extends munit.FunSuite:
  test("arithmetic") {
    assertEquals(Vec2(1, 2) + Vec2(3, 4), Vec2(4, 6))
    assertEquals(Vec2(5, 5) - Vec2(2, 3), Vec2(3, 2))
    assertEquals(Vec2(1, -2) * 3, Vec2(3, -6))
    assertEquals(Vec2(4, 8) / 4, Vec2(1, 2))
  }

  test("magnitude, dot product and normalizing") {
    assertEqualsDouble(Vec2(3, 4).magnitude, 5.0, 1e-9)
    assertEqualsDouble(Vec2(1, 2).dot(Vec2(3, 4)), 11.0, 1e-9)
    assertEqualsDouble(Vec2(10, 0).normalized.magnitude, 1.0, 1e-9)
    assertEquals(Vec2.Zero.normalized, Vec2.Zero)
  }
