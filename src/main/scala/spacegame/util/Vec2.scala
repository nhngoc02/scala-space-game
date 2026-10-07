package spacegame.util

/** An immutable 2D vector, used for positions and velocities. */
final case class Vec2(x: Double, y: Double):
  def +(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
  def -(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
  def *(scalar: Double): Vec2 = Vec2(x * scalar, y * scalar)
  def /(scalar: Double): Vec2 = Vec2(x / scalar, y / scalar)

  def dot(other: Vec2): Double = x * other.x + y * other.y
  def magnitude: Double = math.hypot(x, y)

  /** A unit vector pointing the same way. The zero vector stays zero. */
  def normalized: Vec2 =
    val m = magnitude
    if m == 0 then Vec2.Zero else this / m

object Vec2:
  val Zero: Vec2 = Vec2(0, 0)
