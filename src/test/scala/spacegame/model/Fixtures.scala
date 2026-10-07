package spacegame.model

import spacegame.util.Vec2

object Fixtures:
  given settings: Settings = Settings()
  given sizes: SpriteSizes = SpriteSizes(player = Size(50, 50), enemy = Size(40, 40), bullet = Size(5, 10))

  /** A quiet world: no enemies firing, nothing near the player. */
  def emptyWorld: World =
    World.initial.copy(swarm = EnemySwarm(Vector(Enemy(Vec2(0, 0), sizes.enemy, row = 0))), frame = 1)
