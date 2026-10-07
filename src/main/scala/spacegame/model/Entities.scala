package spacegame.model

import spacegame.util.{Rect, Vec2}

/** Width and height of a sprite, in pixels. */
final case class Size(width: Double, height: Double)

/** Anything on the playfield with a position (its top-left corner) and a size. */
sealed trait Entity:
  def pos: Vec2
  def size: Size

  def bounds: Rect = Rect(pos.x, pos.y, size.width, size.height)
  def collidesWith(other: Entity): Boolean = bounds.intersects(other.bounds)

final case class Player(pos: Vec2, size: Size) extends Entity:

  /** Moves by `delta`, but only along axes where the player stays inside the arena. */
  def moved(delta: Vec2, arena: Size): Player =
    val nx = pos.x + delta.x
    val ny = pos.y + delta.y
    val x = if nx >= 0 && nx + size.width <= arena.width then nx else pos.x
    val y = if ny >= 0 && ny + size.height <= arena.height then ny else pos.y
    copy(pos = Vec2(x, y))

  def shoot(bulletSize: Size, speed: Double): Bullet =
    Bullet(pos + Vec2(size.width / 3, 0), Vec2(0, -speed), bulletSize)

object Player:
  /** A player at the bottom-centre of the arena. */
  def spawn(size: Size, arena: Size): Player =
    Player(Vec2((arena.width - size.width) / 2, arena.height - size.height), size)

final case class Enemy(pos: Vec2, size: Size, row: Int) extends Entity:
  def moved(delta: Vec2): Enemy = copy(pos = pos + delta)

  def shoot(bulletSize: Size, speed: Double): Bullet =
    Bullet(pos, Vec2(0, speed), bulletSize)

/** A projectile. `vel` is pixels per frame; `accel` is added to `vel` every frame. */
final case class Bullet(pos: Vec2, vel: Vec2, size: Size) extends Entity:
  def stepped(accel: Vec2): Bullet = copy(pos = pos + vel, vel = vel + accel)
  def isOffScreen(arena: Size): Boolean = pos.y < 0 || pos.y > arena.height
