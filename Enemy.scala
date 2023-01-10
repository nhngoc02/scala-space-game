package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext

/** An enemy representation for a simple game based on sprites. Handles all
 *  information regarding the enemy's position, movements, and abilities.
 *
 *  @param pic the image representing the enemy
 *  @param initPos the initial position of the '''center''' of the enemy
 *  @param bulletPic the image of the bullets fired by this enemy
 */
class Enemy(pic:Image, initPos:Vec2, private val bulletPic:Image) extends Sprite(pic, initPos) {

  def shoot():Bullet = {  
    new Bullet(Images.bullet_img, this.pos.clone(), new Vec2(0,5))
  }

  def display(g:GraphicsContext):Unit = {
    g.drawImage(Images.enemy_img, this.pos.x, this.pos.y) 
  }
  override def clone(): Enemy = new Enemy(Images.enemy_img, this.pos.clone(), Images.bullet_img)
}
