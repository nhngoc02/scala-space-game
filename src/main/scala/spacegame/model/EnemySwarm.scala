package spacegame.model

import spacegame.util.Vec2

import scala.util.Random

/** A grid of enemies near the top of the screen whose rows sway side to side. */
final case class EnemySwarm(enemies: Vector[Enemy]):
  def isEmpty: Boolean = enemies.isEmpty
  def size: Int = enemies.size

  /** A bullet fired by one randomly chosen enemy, if any are left. */
  def shoot(rng: Random, bulletSize: Size, speed: Double): Option[Bullet] =
    Option.when(enemies.nonEmpty)(enemies(rng.nextInt(enemies.size)).shoot(bulletSize, speed))

  /** Moves even and odd rows by their own deltas, so neighbouring rows can sway in opposite directions. */
  def sway(evenRows: Vec2, oddRows: Vec2): EnemySwarm =
    copy(enemies = enemies.map(e => e.moved(if e.row % 2 == 0 then evenRows else oddRows)))

  /** Removes every enemy hit by `other`, returning the survivors and whether anything was hit. */
  def hitBy(other: Entity): (EnemySwarm, Boolean) =
    val (hit, survivors) = enemies.partition(_.collidesWith(other))
    (EnemySwarm(survivors), hit.nonEmpty)

object EnemySwarm:
  /** Lays out `rows` x `cols` enemies across the top half of the arena. */
  def grid(rows: Int, cols: Int, enemySize: Size, arena: Size): EnemySwarm =
    val colSpacing = (arena.width - 10) / cols
    val rowSpacing = arena.height / 2 / rows
    EnemySwarm(
      for
        row <- (0 until rows).toVector
        col <- 0 until cols
      yield Enemy(Vec2(colSpacing * col + 30, rowSpacing * row), enemySize, row)
    )
