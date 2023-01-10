package cs2.game

import cs2.util.Vec2
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.animation.AnimationTimer
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.image.Image
import scala.collection.mutable.Buffer
import scala.collection.mutable.Set
import scalafx.scene.text.Font
import scala.collection.mutable.Stack

object SpaceGameApp extends JFXApp {
    val screen_width = 1000
    val screen_height = 800

    // splash screen
    var showStartScreen = true
    var showEndScreen = false

    // track player score & lives
    var player_lives:Int = 5
    var player_score:Int = 0

    // to display the rewind time remaining
    var undo_count = 0

    stage = new JFXApp.PrimaryStage {
        title = "Space Game"
        scene = new Scene(screen_width,screen_height){
            val canvas = new Canvas(screen_width,screen_height)
            content = canvas
            val g = canvas.graphicsContext2D

            canvas.requestFocus()

            // Beginning game splash screen
            def startGameScreen() {
                g.setFill(Color.White)
                g.drawImage(Images.start_screen_img, 0, 0)
                g.drawImage(Images.start_button_img, (width.value - Images.start_button_width)/2, (height.value-Images.start_button_height)/2 + 15)
                g.drawImage(Images.exit_button_img, (width.value - Images.start_button_width)/2, (height.value-Images.start_button_height)/2 + 15 + Images.start_button_height + 5)
                // Dectect mouse click within an area
                canvas.onMouseClicked = (e: MouseEvent) => {
                    if(e.x >= ((width.value - Images.start_button_width)/2) && e.x <= ((width.value - Images.start_button_width)/2 + Images.start_button_width)
                        && e.y >= ((height.value - Images.start_button_height)/2 + 15) && e.y <= ((height.value - Images.start_button_height)/2 + 15 + Images.start_button_height)) {
                            showStartScreen = false
                    }
                    if(e.x>=((width.value - Images.start_button_width)/2) && e.x <= ((width.value - Images.start_button_width)/2 + Images.exit_button_width)
                        && e.y>= ((height.value-Images.start_button_height)/2 + 15 + Images.start_button_height + 5) && e.y<=((height.value-Images.start_button_height)/2 + 15 + Images.start_button_height + 5 + Images.exit_button_height)) {
                            sys.exit
                        }
                }
            }

            // End game screen
            def endGameScreen(){
                g.drawImage(Images.end_game_img, 0, 0)
                g.setFill(Color.White)
                g.setFont(new Font("Arial", 50))
                var score_display =  "Score: " + player_score
                g.fillText(score_display, 400, 350)
                 
                g.drawImage(Images.restart_button_img, (width.value - Images.restart_button_width)/2, (height.value-Images.restart_button_height)/2)
                g.drawImage(Images.exit_button_img, (width.value - Images.restart_button_width)/2, (height.value-Images.restart_button_height)/2  + Images.restart_button_height + 3)

                // Dectect mouse click within an area
                canvas.onMouseClicked = (e: MouseEvent) => {
                    if(e.x >= ((width.value - Images.restart_button_width)/2) && e.x <= ((width.value - Images.restart_button_width)/2 + Images.restart_button_width)
                        && e.y >= ((height.value - Images.restart_button_height)/2) && e.y <= ((height.value - Images.restart_button_height)/2 + Images.restart_button_height)) {
                            player_lives = 5
                            player_score = 0
                            bullets_ene.clear
                            bullets_ene.clear
                            player = new Player(Images.player_img, new Vec2(width.value/2 - Images.player_img_width, height.value-Images.player_img_height), Images.bullet_img) 
                            enemyswarm = new EnemySwarm(3,4)
                            enemyswarm.addEnemy()
                            showEndScreen = false
                            GameState.player_stack.clear
                            GameState.enemySwarm_stack.clear
                            GameState.bulletPlayer_stack.clear
                            GameState.bulletEne_stack.clear
                            GameState.lives_stack.clear
                            GameState.score_stack.clear
                    }
                    if(e.x>=((width.value - Images.restart_button_width)/2) && e.x <= ((width.value - Images.restart_button_width)/2 + Images.exit_button_width)
                        && e.y>= ((height.value-Images.restart_button_height)/2  + Images.restart_button_height + 3) && e.y<=((height.value-Images.restart_button_height)/2  + Images.restart_button_height + 3 + Images.exit_button_height)) {
                            sys.exit
                        }
                }  
            }

            var player = new Player(Images.player_img, new Vec2(width.value/2 - Images.player_img_width, height.value-Images.player_img_height), Images.bullet_img) 
            var enemyswarm = new EnemySwarm(3,4)
            enemyswarm.addEnemy()
            
            var bullets_ene:Buffer[Bullet] = Buffer()
            var bullets_player:Buffer[Bullet] = Buffer()

            var key:Set[KeyCode] = Set()

            var time = 0
            var counter = 20

            val timer = AnimationTimer(t => {
                if (showStartScreen){
                    startGameScreen()
                } else if (showEndScreen){
                    endGameScreen()
                } else {
                    g.setFill(Color.rgb(9, 19, 83))	
                    g.fillRect(0,0, width.value, height.value)
                    player.display(g)
                    enemyswarm.display(g)

                    // Display the player's score and number of lives 
                    g.setFill(Color.White)
                    g.setFont(new Font("Arial", 20))
                    var score_display =  "Player Score: " + player_score + "\n" + player_lives + " lives remaining"
                    if(player_lives == 1){
                        score_display =  "Player Score: " + player_score + "\n" + player_lives + " life remaining"
                    }
                    
                    g.fillText(score_display, 800,400)

                    // Reset swarm when all enemies removed 
                    if(enemyswarm.length() == 0){
                        enemyswarm = new EnemySwarm(3,4)
                        enemyswarm.addEnemy()
                    }
                    // Restrict player and enemy firing rates
                    if (time%40==0/enemyswarm.length()){
                        if(enemyswarm.length() !=0){
                            bullets_ene += enemyswarm.shoot()
                        }
                    
                    }
                    if (key.contains(KeyCode.Space) && counter < 0){
                        bullets_player += player.shoot()
                        counter = 20
                    }
        
                    // Remove out of screen bullets
                    bullets_ene = bullets_ene.filterNot((x:Bullet) => x.outOfScreen())
                    bullets_player = bullets_player.filterNot((x:Bullet) => x.outOfScreen())
                    
                    // Display the bullets
                    for (b <- bullets_ene) {
                        b.display(g)
                        b.timeStep()
                        b.applyForce(new Vec2(0, 0.1))
                    }
                    for (b <- bullets_player) {
                        b.display(g)
                        b.timeStep()
                        b.applyForce(new Vec2(0, -0.1))
                    }

                    // Update the key events
                    if(key.contains(KeyCode.Left) || key.contains(KeyCode.A)) player.moveLeft()
                    if(key.contains(KeyCode.Right) || key.contains(KeyCode.D)) player.moveRight()
                    if(key.contains(KeyCode.Up) || key.contains(KeyCode.W)) player.moveUp()
                    if(key.contains(KeyCode.Down) || key.contains(KeyCode.S)) player.moveDown()
                    if(key.contains(KeyCode.R)) {
                        undo() //pressing the "R" key as a mechanic to "undo"
                        undo_count += 1

                        // Visual display of remaining rewind time
                        g.fillText("Rewind time", 800, 480)
                        g.setStroke(Color.White)
                        g.strokeRect(800, 500, 100, 20)
                        g.fillRect(800,500, undo_count*100/(GameState.player_stack.size + undo_count), 20) // round to integer // get outofbound
                    }

                    // Player Bullet/Enemy Bullet intersections with each other
                    var interCount_bulEne = 0
                    var interCount_bulPlayer = 0
                    while(interCount_bulEne < bullets_ene.length){
                        interCount_bulPlayer = 0
                        while(interCount_bulPlayer < bullets_player.length){
                            if(interCount_bulEne < bullets_ene.length && bullets_ene(interCount_bulEne).intersection(bullets_player(interCount_bulPlayer))){
                                bullets_ene.remove(interCount_bulEne)
                                bullets_player.remove(interCount_bulPlayer) 
                            }
                            else interCount_bulPlayer += 1
                        }
                        interCount_bulEne += 1
                    }

                    // Enemy bullet intersections with the player
                    interCount_bulEne = 0
                    while(interCount_bulEne < bullets_ene.length){
                        if(player.intersection(bullets_ene(interCount_bulEne))){
                            bullets_ene.remove(interCount_bulEne)
                            player.moveOriginal()
                            // substract 1 from the player's lives
                            player_lives -= 1 
                            if(player_lives == 0){ // update endGame
                                showEndScreen = true
                            }
                        }
                        interCount_bulEne += 1
                    }

                    // Player bullet intersections with enemies
                    interCount_bulPlayer = 0
                    while(interCount_bulPlayer < bullets_player.length){ 
                        if(enemyswarm.intersection_enemySwarm(bullets_player(interCount_bulPlayer))) {
                            bullets_player.remove(interCount_bulPlayer)
                            // add 1 to player's score
                            player_score += 1
                        }
                        interCount_bulPlayer += 1
                    }

                    // Player intersections with enemies
                    var interCount_ene = 0
                    while(interCount_ene < enemyswarm.length){
                        if(player.intersection(enemyswarm(interCount_ene))){
                            player.moveOriginal()
                            // substract 1 from the player's lives
                            player_lives -= 1 
                            if(player_lives == 0){ // update endGame
                                showEndScreen = true
                            }
                        }
                        interCount_ene += 1
                    }

                    // Enemy movement
                    if((time/40)%2 == 0){
                        enemyswarm.oddRowMoveRight(new Vec2(2,0))
                        if(time/40 != 0) enemyswarm.evenRowMoveLeft(new Vec2(-2,0))
                    } else {
                        enemyswarm.evenRowMoveRight(new Vec2(2,0))
                        enemyswarm.oddRowMoveLeft(new Vec2(-2,0))
                    }

                    // Update the GameState of each frame
                    if(!key.contains(KeyCode.R)) {
                        GameState.player_stack.push(player.clone) 
                        GameState.enemySwarm_stack.push(enemyswarm.clone()) 
                        GameState.bulletPlayer_stack.push(bullets_player.map(_.clone))
                        GameState.bulletEne_stack.push(bullets_ene.map(_.clone)) 
                        GameState.lives_stack.push(new Integer(player_lives)) 
                        GameState.score_stack.push(new Integer(player_score)) 
                    }


                    time += 1
                    counter -= 1
                }
            })
            timer.start()

            canvas.onKeyPressed = (e:KeyEvent) => { 
                key += e.code
            }
            canvas.onKeyReleased = (e:KeyEvent) => {
                key -= e.code
            }

            def undo(){
                player = GameState.player_stack.pop
                enemyswarm.eneList(GameState.enemySwarm_stack.pop)
                bullets_player = GameState.bulletPlayer_stack.pop
                bullets_ene = GameState.bulletEne_stack.pop
                player_lives = GameState.lives_stack.pop
                player_score = GameState.score_stack.pop
            }
        }
        
    }  

}

object Images {
    val path_player_img = getClass().getResource("/images/player8_img.png")
    val player_img = new Image(path_player_img.toString)
    val player_img_width = player_img.getWidth()
    val player_img_height = player_img.getHeight()

    val path_enemy_img = getClass().getResource("/images/enemy3_img.png")
    val enemy_img = new Image(path_enemy_img.toString)

    val path_bullet_img = getClass().getResource("/images/bullet_img.png")
    val bullet_img = new Image(path_bullet_img.toString)

    val path_start_screen_img = getClass().getResource("/images/startGame.png")
    val start_screen_img = new Image(path_start_screen_img.toString())  

    val path_start_button_img = getClass().getResource("/images/start_button.png")   
    val start_button_img = new Image(path_start_button_img.toString()) 
    val start_button_width = start_button_img.getWidth()
    val start_button_height = start_button_img.getHeight()

    val path_exit_button_img = getClass().getResource("/images/exit_img.png")   
    val exit_button_img = new Image(path_exit_button_img.toString()) 
    val exit_button_width = exit_button_img.getWidth()
    val exit_button_height = exit_button_img.getHeight()

    val path_end_game_img = getClass().getResource("/images/endGame.png")
    val end_game_img = new Image(path_end_game_img.toString())  

    val path_restart_button_img = getClass().getResource("/images/restart.png")   
    val restart_button_img = new Image(path_restart_button_img.toString()) 
    val restart_button_width = restart_button_img.getWidth()
    val restart_button_height = restart_button_img.getHeight()
}

object GameState {
    var player_stack = Stack[Player]()
    var enemySwarm_stack = Stack[Buffer[Enemy]]()
    var bulletPlayer_stack = Stack[Buffer[Bullet]]()
    var bulletEne_stack = Stack[Buffer[Bullet]]()
    var lives_stack = Stack[Int]()
    var score_stack = Stack[Int]()
}







