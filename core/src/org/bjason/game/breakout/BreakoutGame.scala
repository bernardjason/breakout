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
  var letJvmSettleDown = 0f

  lazy val starting = new Texture(ascii.getPixmapForString("Starting...", 1280, 200, Color.LIGHT_GRAY))

  override def firstScreenSetup(): Unit = {
    CurrentGame.reset
    GameKeyboard.reset
    stage = new Stage(new ScreenViewport()) // GameScreen.cam))

    super.firstScreenSetup()
    soft = new Texture(ascii.getPixmapForString("GAME " + counter, 1624, 350, Color.YELLOW))
    counter = counter + 1
    player = new Player(Gdx.graphics.getWidth / 2, 60)
    stage.clear()

    stage.addActor(player)
    addTheBricks
    letJvmSettleDown = -3
  }

  private def addTheBricks = {
    var level = 1
    for ( yy <-
          Gdx.graphics.getHeight - (BasicBrick.sizey * 2 + 1) * 8 to
            Gdx.graphics.getHeight - BasicBrick.sizey * 8
    by (BasicBrick.sizey + 1) * 2) {
      for (xx <- BasicBrick.sizex * 2 to Gdx.graphics.getWidth - BasicBrick.sizex * 2 by BasicBrick.sizex * 2 + 3) {
        val random = (Math.random() *1000).toInt % 10
        val b = if (random < 7) {
          new BasicBrick(xx, yy,level)
        } else {
          new FancyBrick(xx, yy)
        }
        CurrentGame.addActor(b)
      }
      level=level+1
    }
  }

  override def create(): Unit = {
    Gdx.input.setInputProcessor(GameKeyboard)
    firstScreenSetup()
  }
  override def render(): Unit = {
    letJvmSettleDown = letJvmSettleDown +Gdx.graphics.getDeltaTime
    if (letJvmSettleDown >= 0 ) {
      actualRender()
    } else {
      Gdx.gl.glClearColor(0, 0.0f, 0, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      textBatch.begin()
      textBatch.draw(starting,100+letJvmSettleDown*20,Gdx.graphics.getHeight/2)
      textBatch.end()
    }
  }

  def actualRender(): Unit = {
    Gdx.gl.glClearColor(0, 0.0f, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    GameKeyboard.render()

    textBatch.begin()
    CurrentGame.font.draw(textBatch,
      "Score " + CurrentGame.score + " lives " + CurrentGame.lives + " level=" + CurrentGame.difficult +
          " fps=" + Gdx.graphics.getFramesPerSecond, 0, Gdx.graphics.getHeight - 5)
    textBatch.end()

    batch.begin()
    stage.draw()
    batch.end()

    CollisionDetection.checkPlayerAgainst(player,CurrentGame.canBeHitActors)
    CollisionDetection.checkCollision(CurrentGame.canBeHitActors.filter(_.isInstanceOf[Ball]), CurrentGame.bricks)

    CurrentGame.doAddAndRemove(stage)

    if (CurrentGame.lives <= 0 || GameKeyboard.escape == true) {
      clearUp()
      this.screenComplete = true
    }

  }


  def clearUp() = {
    for( a <- CurrentGame.canBeHitActors) {
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


