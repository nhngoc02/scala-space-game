package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext
import scala.collection.mutable.ListBuffer

/** Representation of a bullet/projectile for a simple game based on sprites.
 *  Handles all information regarding a bullet's position, movements, and 
 *  abilities.
 *  
 *  @param pic the image representing the bullet
 *  @param initPos the initial position of the '''center''' of the bullet
 *  @param vel the initial velocity of the bullet
 */
class Bullet(pic:Image, initPos:Vec2, private var vel:Vec2) extends Sprite(pic, initPos) {

  // Advances the position of the Bullet over a single time step
  def timeStep():Unit = { 
    move(vel)
  }
  
  def display(g: GraphicsContext): Unit = {
    g.drawImage(Images.bullet_img, this.pos.x, this.pos.y)
  }

  def applyForce(force:Vec2):Unit = {
    vel += force
  }

  // Remove out of screen bullet
  def outOfScreen():Boolean = {
    if (pos.y<0 || pos.y > SpaceGameApp.screen_height) true
    else false
  }
  override def clone(): Bullet = new Bullet(Images.bullet_img, this.pos.clone(), new Vec2(0,-5))
}
