package spacegame.model

import spacegame.util.Vec2

import scala.util.Random

/** What the player is pressing during a frame. */
final case class Input(
    left: Boolean = false,
    right: Boolean = false,
    up: Boolean = false,
    down: Boolean = false,
    fire: Boolean = false
)

/** A complete, immutable snapshot of a game in progress.
  *
  * Because a `World` is immutable, rewinding is just keeping a list of past worlds.
  */
final case class World(
    player: Player,
    swarm: EnemySwarm,
    playerBullets: Vector[Bullet],
    enemyBullets: Vector[Bullet],
    lives: Int,
    score: Int,
    frame: Int,
    fireCooldown: Int
)(using settings: Settings, sizes: SpriteSizes):

  def isGameOver: Boolean = lives <= 0

  /** Advances the game by one frame. */
  def step(input: Input, rng: Random): World =
    this
      .respawnSwarmIfCleared
      .enemiesFire(rng)
      .playerFires(input)
      .moveBullets
      .movePlayer(input)
      .resolveBulletClashes
      .resolveHitsOnPlayer
      .resolveHitsOnEnemies
      .resolvePlayerRammingEnemies
      .swaySwarm
      .nextFrame

  private def nextFrame: World =
    copy(frame = frame + 1, fireCooldown = math.max(0, fireCooldown - 1))

  private def respawnSwarmIfCleared: World =
    if swarm.isEmpty then copy(swarm = World.newSwarm) else this

  private def enemiesFire(rng: Random): World =
    if frame % settings.enemyFireInterval != 0 then this
    else copy(enemyBullets = enemyBullets ++ swarm.shoot(rng, sizes.bullet, settings.bulletSpeed))

  private def playerFires(input: Input): World =
    if !input.fire || fireCooldown > 0 then this
    else
      copy(
        playerBullets = playerBullets :+ player.shoot(sizes.bullet, settings.bulletSpeed),
        fireCooldown = settings.playerFireCooldown
      )

  private def moveBullets: World =
    val a = settings.bulletAcceleration
    copy(
      playerBullets = playerBullets.filterNot(_.isOffScreen(settings.arena)).map(_.stepped(Vec2(0, -a))),
      enemyBullets = enemyBullets.filterNot(_.isOffScreen(settings.arena)).map(_.stepped(Vec2(0, a)))
    )

  private def movePlayer(input: Input): World =
    val s = settings.playerSpeed
    val dx = (if input.right then s else 0) - (if input.left then s else 0)
    val dy = (if input.down then s else 0) - (if input.up then s else 0)
    copy(player = player.moved(Vec2(dx, dy), settings.arena))

  /** Player and enemy bullets that collide cancel each other out, one for one. */
  private def resolveBulletClashes: World =
    val (survivingEnemy, survivingPlayer) =
      enemyBullets.foldLeft((Vector.empty[Bullet], playerBullets)) { case ((keptEnemy, remainingPlayer), eb) =>
        remainingPlayer.indexWhere(_.collidesWith(eb)) match
          case -1 => (keptEnemy :+ eb, remainingPlayer)
          case i  => (keptEnemy, remainingPlayer.patch(i, Nil, 1))
      }
    copy(playerBullets = survivingPlayer, enemyBullets = survivingEnemy)

  private def resolveHitsOnPlayer: World =
    val (hits, misses) = enemyBullets.partition(_.collidesWith(player))
    copy(enemyBullets = misses).loseLives(hits.size)

  /** Each player bullet that hits removes every enemy it touches and scores one point. */
  private def resolveHitsOnEnemies: World =
    val (newSwarm, kept, points) =
      playerBullets.foldLeft((swarm, Vector.empty[Bullet], 0)) { case ((sw, keptBullets, pts), b) =>
        val (after, hit) = sw.hitBy(b)
        if hit then (after, keptBullets, pts + 1) else (after, keptBullets :+ b, pts)
      }
    copy(swarm = newSwarm, playerBullets = kept, score = score + points)

  private def resolvePlayerRammingEnemies: World =
    loseLives(swarm.enemies.count(_.collidesWith(player)))

  /** Each hit costs a life and sends the player back to the start position. */
  private def loseLives(hits: Int): World =
    if hits == 0 then this
    else copy(player = Player.spawn(sizes.player, settings.arena), lives = math.max(0, lives - hits))

  /** Neighbouring rows sway in opposite directions, swapping every `swarmSwayPeriod` frames. */
  private def swaySwarm: World =
    val v = settings.swarmSpeed
    val phase = frame / settings.swarmSwayPeriod
    val (even, odd) =
      if phase % 2 == 0 then (if phase == 0 then Vec2.Zero else Vec2(-v, 0), Vec2(v, 0))
      else (Vec2(v, 0), Vec2(-v, 0))
    copy(swarm = swarm.sway(even, odd))

object World:
  def initial(using settings: Settings, sizes: SpriteSizes): World =
    World(
      player = Player.spawn(sizes.player, settings.arena),
      swarm = newSwarm,
      playerBullets = Vector.empty,
      enemyBullets = Vector.empty,
      lives = settings.startingLives,
      score = 0,
      frame = 0,
      fireCooldown = 0
    )

  private def newSwarm(using settings: Settings, sizes: SpriteSizes): EnemySwarm =
    EnemySwarm.grid(settings.swarmRows, settings.swarmCols, sizes.enemy, settings.arena)
