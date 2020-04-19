package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor
import org.bjason.game.{CareAboutKeyboard, GameKeyboard}


class Player(startx: Int, starty: Int) extends Actor with MyCollision with CareAboutKeyboard {

  val size = 96
  val XY = 600

  val sizey = size / 8
  val sizex = size

  override val points: Array[Float] = Array[Float](
    -sizex, -sizey,
    -sizex, sizey,
    sizex, sizey,
    sizex, -sizey
  )

  private val texture = makeTexture(sizex * 2 + 1, sizey * 2 + 1, sizex, sizey, points)
  setSize(texture.getWidth, texture.getHeight)
  setPosition(startx, starty)
  GameKeyboard.listeners += this


  def makeTexture(w: Int, h: Int, ax: Int, ay: Int, points: Array[Float]): Texture = {
    val pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888)
    pixmap.setColor(Color.RED)
    pixmap.fill()
    pixmap.setColor(Color.WHITE)

    var p = Array(points(0), points(1))
    var dontSkip = false
    for (n <- points.grouped(2)) {
      if (dontSkip) {
        pixmap.drawLine(ax + p(0).toInt, ay + p(1).toInt, ax + n(0).toInt, ay + n(1).toInt)
      }
      dontSkip = true
      p = n
    }
    pixmap.drawLine(ax + p(0).toInt, ay + p(1).toInt, ax + points(0).toInt, ay + points(1).toInt)
    val texture = new Texture(pixmap)
    pixmap.dispose()
    texture
  }

  override def left(): Unit = {
    doHorizontal(-XY * Gdx.graphics.getDeltaTime)
  }

  override def right(): Unit = {
    doHorizontal(XY * Gdx.graphics.getDeltaTime)
  }

  private def doHorizontal(by: Float) {
    setX(getX + by)
    if (getX <= size || getX >= Gdx.graphics.getWidth-size) {
      setX(getX - by)
    }
  }



  override def fire(): Unit = {
    if (CurrentGame.ballReadyToFire) {
      CurrentGame.ballFired
      val b = new Ball(getX.toInt , getY.toInt + sizey+5)
      CurrentGame.addActor(b)
    }
  }


  override def draw(batch: Batch, parentAlpha: Float) {
    polygon.setPosition(getX, getY)

    batch.draw(texture, getX - sizex, getY - sizey)
    if (CurrentGame.ballReadyToFire) {
      batch.draw(Ball.texture,getX-Ball.size , getY+Ball.size )
    }
  }

  def dispose(): Unit = {
    texture.dispose()
  }

}
