package spacegame.model

/** Tunable game rules. Timings are in frames (the game runs at roughly 60 fps). */
final case class Settings(
    arena: Size = Size(1000, 800),
    startingLives: Int = 5,
    swarmRows: Int = 3,
    swarmCols: Int = 4,
    playerSpeed: Double = 5,
    bulletSpeed: Double = 5,
    /** Extra speed bullets pick up each frame, so shots accelerate away from the shooter. */
    bulletAcceleration: Double = 0.1,
    playerFireCooldown: Int = 20,
    enemyFireInterval: Int = 40,
    swarmSpeed: Double = 2,
    /** How many frames each sway of the swarm lasts before the rows reverse. */
    swarmSwayPeriod: Int = 40,
    /** How many past frames are kept for rewinding (10 seconds). */
    rewindFrames: Int = 600
)

/** Sprite sizes, taken from the loaded images so collisions match what is drawn. */
final case class SpriteSizes(player: Size, enemy: Size, bullet: Size)
