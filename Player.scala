package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scalafx.scene.canvas.GraphicsContext

/** The player representation for a simple game based on sprites. Handles all
 *  information regarding the player's positions, movements, and abilities.
 *
 *  @param avatar the image representing the player
 *  @param initPos the initial position of the '''center''' of the player
 *  @param bulletPic the image of the bullets fired by this player
 */
class Player(avatar:Image, initPos:Vec2, private val bulletPic:Image) extends Sprite(avatar, initPos) {

  /** moves the player sprite one "step" to the left.  The amount of this
   *  movement will likely need to be tweaked in order for the movement to feel
   *  natural.
   *
   *  @return none/Unit
   */

  var step = 5

  def moveLeft():Unit = { 
    if(this.pos.x - step >= 0) {
      this.pos.x -= step
    }  
  }

  def moveRight():Unit = {
    if(this.pos.x + Images.player_img_width + step <= SpaceGameApp.screen_width) {
      this.pos.x += step
    } 
  }

  def moveUp():Unit = {
    if(this.pos.y - step >= 0){
      this.pos.y -= step
    }
    
  }
  def moveDown():Unit = {
    if(this.pos.y + Images.player_img_height + step <= SpaceGameApp.screen_height) {
      this.pos.y += step
    }
    
  }

  def shoot():Bullet = {  
    var bullet_pos = this.pos.clone()
    bullet_pos.x += Images.player_img_width/3
      
    new Bullet(Images.bullet_img, bullet_pos, new Vec2(0,-5)) 
  }

  def display(g:GraphicsContext):Unit = {
    g.drawImage(Images.player_img, this.pos.x, this.pos.y) 
  }

  // Return to the orginal position 
  def moveOriginal(): Unit = {
    this.pos = Vec2(SpaceGameApp.screen_width/2 - Images.player_img_width, 
                    SpaceGameApp.screen_height-Images.player_img_height)
  }

  override def clone(): Player = new Player(Images.player_img, this.pos.clone(), Images.bullet_img)

}
