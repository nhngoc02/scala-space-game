package cs2.game

import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext
import scala.util.Random
import scala.collection.mutable.Buffer

/** contains the control and logic to present a coordinated set of Enemy objects.
 *  For now, this class generates a "grid" of enemy objects centered near the
 *  top of the screen.
 *
 *  @param nRows - number of rows of enemy objects
 *  @param nCols - number of columns of enemy objects
 */
class EnemySwarm(private val nRows:Int, private val nCols:Int) {

  private var enemyList:Buffer[Enemy] = Buffer()
    
  def addEnemy():Unit = {
    for(i <- 0 until nRows) {
      for(j <- 0 until nCols) {
          enemyList += new Enemy(Images.enemy_img, 
                                  new Vec2((SpaceGameApp.screen_width - 10)/nCols*j + 30, (SpaceGameApp.screen_height/2/nRows)*i), 
                                  Images.bullet_img) 
      }
    }
  }

  def display(g:GraphicsContext):Unit = {
        for(ene <- enemyList) ene.display(g) 
    }

  /** overridden method of ShootsBullets. Creates a single, new bullet instance
   *  originating from a random enemy in the swarm. (Not a bullet from every
   *  object, just a single from a random enemy)
   *
   *  @return Bullet - the newly created Bullet object fired from the swarm
   */
  def shoot():Bullet = { 
    enemyList(scala.util.Random.nextInt(enemyList.length)).shoot()
  
  }

  def intersection_enemySwarm(other:Sprite):Boolean = {
    var interCount_ene = 0
    var intersect = false
    while(interCount_ene < enemyList.length){
      if(enemyList(interCount_ene).intersection(other)){
        enemyList.remove(interCount_ene)
        intersect = true
      }
      interCount_ene +=1
    }
    intersect
  }
  
  def length():Int = {enemyList.length}
  def apply(n: Int):Enemy = {
    enemyList(n)
  }

  def oddRowMoveRight(direction:Vec2){
    for(ene <- enemyList){
      if(ene.getY %2 != 0){
        ene.move(direction)
      }
    }
  }
  def oddRowMoveLeft(direction:Vec2){
    for(ene <- enemyList){
      if(ene.getY %2 != 0){
        ene.move(direction)
      }
    }
  }
  def evenRowMoveRight(direction:Vec2){
    for(ene <- enemyList){
      if(ene.getY %2 == 0){
        ene.move(direction)
      }
    }
  }
  def evenRowMoveLeft(direction:Vec2){
    for(ene <- enemyList){
      if(ene.getY %2 == 0){
        ene.move(direction)
      }
    }
  }
  override def clone():Buffer[Enemy] = {this.enemyList.map(_.clone())}

  def eneList(l:Buffer[Enemy]):Unit = {
    enemyList = l
  }

}