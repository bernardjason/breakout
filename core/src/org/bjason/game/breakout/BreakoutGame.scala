package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.bjason.game.{DrawAscii, GameKeyboard, LoopTrait}


class BreakoutGame extends LoopTrait {
  lazy val ascii = DrawAscii()
  var soft: Texture = null
  var counter = 0
  var stage: Stage = null
  lazy val batch = new SpriteBatch()
  lazy val textBatch = new SpriteBatch()
  lazy val shapeRenderer = new ShapeRenderer()
  var player: Player = null

  override def firstScreenSetup(): Unit = {
    CurrentGame.reset
    GameKeyboard.reset
    Sound.reset

    stage = new Stage(new ScreenViewport()) // GameScreen.cam))

    super.firstScreenSetup()
    soft = new Texture(ascii.getPixmapForString("GAME " + counter, 1624, 350, Color.YELLOW))
    counter = counter + 1
    player = new Player(Gdx.graphics.getWidth / 2, 160)
    stage.clear()

    stage.addActor(player)
    addTheBricks
  }

  private def addTheBricks = {
    var level = 1
    var rows = 6
    var yy = (Gdx.graphics.getHeight * 0.75f).toInt
    while (rows > 0 && yy >= Gdx.graphics.getHeight / 2) {
      for (xx <- BasicBrick.sizex * 2 to Gdx.graphics.getWidth - BasicBrick.sizex * 2 by BasicBrick.sizex * 2 + 3) {
        val random = (Math.random() * 1000).toInt % 10
        val b = if (random < 7) {
          new BasicBrick(xx, yy, level)
        } else {
          new FancyBrick(xx, yy)
        }
        CurrentGame.addActor(b)
      }
      yy = yy - BasicBrick.sizey * 2
      rows = rows - 1
    }
    level = level + 1
  }


  override def create(): Unit = {
    Gdx.input.setInputProcessor(GameKeyboard)
    firstScreenSetup()
  }

  override def render(): Unit = {
    Gdx.gl.glClearColor(0, 0.0f, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    CurrentGame.deltaTime = Gdx.graphics.getDeltaTime

    if (!CurrentGame.okToPlayNow) {
      Sound.playStarting
    }

    CurrentGame.render()


    GameKeyboard.render()

    textBatch.begin()
    CurrentGame.font.draw(textBatch,
      "Score " + CurrentGame.score + " balls " + CurrentGame.balls + " level=" + CurrentGame.difficult +
        " fps=" + Gdx.graphics.getFramesPerSecond, 0, Gdx.graphics.getHeight - 5)
    textBatch.end()

    batch.begin()
    stage.draw()
    batch.end()

    CollisionDetection.checkPlayerAgainst(player, CurrentGame.canBeHitActors)
    val balls = CurrentGame.canBeHitActors.filter(_.isInstanceOf[Ball])
    if (balls.length == 0) {
      CurrentGame.ballReadyToFire = true
    } else {
      CurrentGame.ballReadyToFire = false
      CollisionDetection.checkCollision(balls, CurrentGame.bricks)
    }
    if (CurrentGame.bricks.length <= 0) {
      CurrentGame.bricks.map(CurrentGame.removeActor(_))
      CurrentGame.difficult = CurrentGame.difficult + 1
      addTheBricks
    }

    CurrentGame.doAddAndRemove(stage)

    if (CurrentGame.balls <= 0 || GameKeyboard.escape == true) {
      clearUp()
      this.screenComplete = true
    }

  }


  def clearUp() = {
    for (a <- CurrentGame.canBeHitActors) {
      a.dispose()
    }
    player.dispose()
    stage.dispose()
  }


  override def dispose(): Unit = {
    stage.dispose()
  }

  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
    stage.getViewport.update(width, height, true)
  }
}


