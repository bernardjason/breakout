package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor

object PrizeBrick {
  val size = 40
  val sizex = size
  val sizey=size/2

  val points: Array[Float] = Array[Float](
    -PrizeBrick.sizex, -PrizeBrick.sizey,
    -PrizeBrick.sizex, PrizeBrick.sizey,
    PrizeBrick.sizex, PrizeBrick.sizey,
    PrizeBrick.sizex, -PrizeBrick.sizey
  )

  val texture = PrizeBrick.makeTexture(PrizeBrick.sizex * 2 + 1, PrizeBrick.sizey * 2 + 1, PrizeBrick.sizex, PrizeBrick.sizey, points)

  def makeTexture(w: Int, h: Int, ax: Int, ay: Int, points: Array[Float]): Texture = {
    val pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888)
    pixmap.setColor(Color.YELLOW)
    pixmap.fill()

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
}

class PrizeBrick(startx: Int, starty: Int) extends Actor with MyCollision {

  override val points: Array[Float] = PrizeBrick.points
  val speed = 5

  protected val texture = PrizeBrick.texture
  setSize(texture.getWidth, texture.getHeight)
  setX(startx)
  setY(starty)
  polygon.setPosition(startx, starty)

  override def onHit(by: Actor): Unit = {
    super.onHit(by)
    CurrentGame.score = CurrentGame.score + 1
  }

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    setY(getY - speed * parentAlpha)
    polygon.setPosition(getX, getY)
    val y = getY
    batch.draw(texture, getX - PrizeBrick.sizex, getY - PrizeBrick.sizey)

    if ( y <= PrizeBrick.sizey ) {
      CurrentGame.removeActor(this)
    }
  }

  override def dispose(): Unit = {
  }
}

