package spacegame.util

class RectSuite extends munit.FunSuite:
  private val box = Rect(0, 0, 10, 10)

  test("overlapping rectangles intersect") {
    assert(box.intersects(Rect(5, 5, 10, 10)))
    assert(box.intersects(Rect(2, 2, 2, 2)))
  }

  test("rectangles that only touch or are apart do not intersect") {
    assert(!box.intersects(Rect(10, 0, 5, 5)))
    assert(!box.intersects(Rect(20, 20, 5, 5)))
  }

  test("contains includes the edges") {
    assert(box.contains(0, 0))
    assert(box.contains(10, 10))
    assert(!box.contains(10.1, 5))
  }
