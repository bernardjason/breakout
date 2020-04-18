package org.bjason.game.breakout

import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor


object FancyBrick {
  val size = 40
  val sizex = size
  val sizey=size/2

  val points: Array[Float] = Array[Float](
    -FancyBrick.sizex, -FancyBrick.sizey,
    -FancyBrick.sizex, FancyBrick.sizey,
    FancyBrick.sizex, FancyBrick.sizey,
    FancyBrick.sizex, -FancyBrick.sizey
  )

  val texture = FancyBrick.makeTexture(FancyBrick.sizex * 2 + 1, FancyBrick.sizey * 2 + 1, FancyBrick.sizex, FancyBrick.sizey, points)

  def makeTexture(w: Int, h: Int, ax: Int, ay: Int, points: Array[Float]): Texture = {
    val pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888)
    pixmap.setColor(Color.CYAN)
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
}

class FancyBrick(startx: Int, starty: Int) extends Brick {

  override val points: Array[Float] = FancyBrick.points

  protected val texture = FancyBrick.texture
  setSize(texture.getWidth, texture.getHeight)
  setX(startx)
  setY(starty)
  polygon.setPosition(startx, starty)

  override def onHit(by: Actor): Unit = {
    super.onHit(by)
    val prizeBrick = new PrizeBrick(startx,starty)
    CurrentGame.addActor(prizeBrick)
  }
}

