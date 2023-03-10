package cs2.util

class Vec2 (var x:Double, var y:Double) {
  /** DO NOT MODIFY THE FOLLOWING TOSTRING METHOD **/
  override def toString():String = "("+x+","+y+")"
  
  //Methods for addition and subtraction of vectors
  def +  (other:Vec2):Vec2 = { 
    new Vec2(this.x + other.x, this.y + other.y)
    }
  def += (other:Vec2):Unit = { 
    this.x += other.x
    this.y += other.y
    }
  
  def -  (other:Vec2):Vec2 = { 
    new Vec2(this.x - other.x, this.y - other.y)
    }
  def -= (other:Vec2):Unit = { 
    this.x -= other.x
    this.y -= other.y
    }

  //Methods for multiplication and division of vectors by a scalar (non-vector)
  def * (scalar:Double):Vec2 = { 
    new Vec2(scalar*this.x, scalar*this.y)
    }
  def *= (scalar:Double):Unit = { 
    this.x *= scalar
    this.y *= scalar
    }

  def / (scalar:Double):Vec2 = { 
    new Vec2(this.x/scalar, this.y/scalar)
    }
  def /= (scalar:Double):Unit = { 
    this.x /= scalar
    this.y /= scalar
    }

  //Methods to determine the length of a vector (magnitude and length should return the same value)
  def magnitude():Double = { 
    math.sqrt(math.pow(this.x,2) + math.pow(this.y,2))
    }
  def length():Double = { 
    math.sqrt(math.pow(this.x,2) + math.pow(this.y,2)) 
    }
  
  //Methods to calculate the dot product (same returns)
  def dot(other:Vec2):Double = { 
    this.x*other.x + this.y*other.y 
    }
  def **(other:Vec2):Double = { 
    this.x*other.x + this.y*other.y 
    }
  
  //Methods to determine the angle between 2 vectors (same returns)
  def angleBetween(other:Vec2):Double = { 
    math.acos(dot(other)/(this.length()*other.length())) 
    }
  def <>(other:Vec2):Double = { 
    math.acos(dot(other)/(this.length()*other.length())) 
    }

  //Methods to return a new vector that is in the same direction, but length 1 (same returns)
  def normalize():Vec2 = { 
    new Vec2(this.x/this.length(), this.y/this.length())
    }
  def unary_~ : Vec2 = { 
    new Vec2(this.x/this.length(), this.y/this.length()) 
    }

  //A clone operator can be useful when making "deep" copies of objects
  override def clone():Vec2 = { 
    new Vec2(x,y)
   }
}

object Vec2 {
  //These apply methods can be used for constructing Vec2 instances without saying "new"
  /** DO NOT CHANGE THE FOLLOWING THREE APPLY METHODS**/
  def apply(myX:Double, myY:Double):Vec2 = { new Vec2(myX, myY) }
  def apply(toCopy:Vec2):Vec2 = { new Vec2(toCopy.x, toCopy.y) }
  def apply():Vec2 = { new Vec2(0, 0) }

  def main(args:Array[String]):Unit = {
    /** Your solution to the physics problem described should be calculated here.
     *  Remember to print out your answer using println.
     */
     
     var x1 = 29.4241
     var y1 = 98.4936

     var x2 = 30.2672
     var y2 = 97.7431  

     var vect = Vec2(x2-x1,y2-y1)
     //println(vect)
     var vect2 = vect*(4/1.5)
     var xnew = vect2.x + x1
     var ynew = vect2.y + y1
    
    //testing
     var test = Vec2(xnew-x1,ynew-y1)
     println(test.magnitude)
     println(test.magnitude/vect.magnitude)

     println(f"After continuing flying with this direction and speed for an additional 2.5 hours, the drone be in latitude $xnew%f N and longitude $ynew%f W.")
    
  }
}
