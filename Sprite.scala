package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.scene.input.KeyCode
import scala.collection.mutable.Buffer

/** A graphical sprite object used for gaming or other visual displays
 *  
 *  @constructor create a new sprite based on the given image and initial location
 *  @param img the image used to display this sprite
 *  @param pos the initial position of the '''center''' of the sprite in 2D space
 */
abstract class Sprite (protected val img:Image, protected var pos:Vec2) {

  def move (direction:Vec2):Unit = { 
    this.pos += direction
  }
  
  /** moves the sprite to a specific location specified by a vector (not a relative movement)
   *  
   *  @param location - the new location for the sprite's position
   *  @return none/Unit
   */
  def moveTo (location:Vec2):Unit = { 
    this.pos = location
  }
    
  // Display the sprite at its current location in the specified Graphics2D context
  def display (g:GraphicsContext):Unit 

  val height = img.getHeight()
  val width = img.getWidth()

  def intersection(other:Sprite):Boolean = {
    val thislowx = this.pos.x
    val thishighx = this.pos.x + this.width
    val thislowy = this.pos.y
    val thishighy = this.pos.y + this.height
    val otherlowx = other.pos.x
    val otherhighx = other.pos.x + other.width
    val otherlowy = other.pos.y
    val otherhighy = other.pos.y + other.height

    if((otherlowx<thishighx & otherhighx>thislowx) & (otherlowy<thishighy & otherhighy>thislowy)){
      return true
    } else return false

  }

  def getX: Double = {pos.x}
  def getY:Double = {pos.y}

  
}


