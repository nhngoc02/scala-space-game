package spacegame.util

/** An axis-aligned rectangle whose origin is its top-left corner. */
final case class Rect(x: Double, y: Double, width: Double, height: Double):
  def right: Double = x + width
  def bottom: Double = y + height

  /** True when the two rectangles overlap. Touching edges do not count. */
  def intersects(other: Rect): Boolean =
    x < other.right && other.x < right && y < other.bottom && other.y < bottom

  def contains(px: Double, py: Double): Boolean =
    px >= x && px <= right && py >= y && py <= bottom
