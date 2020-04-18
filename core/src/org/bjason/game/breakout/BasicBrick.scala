package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor

object BasicBrick {
  val size = 40
  val sizex = size
  val sizey=size/2

  val points: Array[Float] = Array[Float](
    -BasicBrick.sizex, -BasicBrick.sizey,
    -BasicBrick.sizex, BasicBrick.sizey,
    BasicBrick.sizex, BasicBrick.sizey,
    BasicBrick.sizex, -BasicBrick.sizey
  )

  //val texture = BasicBrick.makeTexture(BasicBrick.sizex * 2 + 1, BasicBrick.sizey * 2 + 1, BasicBrick.sizex, BasicBrick.sizey, points)
  val pixmapFullSize = new Pixmap(Gdx.files.internal("data/basicbrick.png"))
  val height =BasicBrick.sizey * 2 + 1
  val width =BasicBrick.sizex * 2 + 1
  val pixmap = new Pixmap(width,height, Pixmap.Format.RGBA8888)
  pixmap.drawPixmap(pixmapFullSize,0,0,pixmapFullSize.getWidth,pixmapFullSize.getHeight,0,0,width,height)
  val texture = new Texture(pixmap)
  pixmapFullSize.dispose()
  pixmap.dispose()


  def makeTexture(w: Int, h: Int, ax: Int, ay: Int, points: Array[Float]): Texture = {
    val pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888)
    pixmap.setColor(Color.FIREBRICK)
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

class BasicBrick(startx: Int, starty: Int,level:Int) extends Brick {

  override val points: Array[Float] = BasicBrick.points

  protected val texture = BasicBrick.texture
  setSize(texture.getWidth, texture.getHeight)
  setX(startx)
  setY(starty)
  polygon.setPosition(startx, starty)

  override def onHit(by: Actor): Unit = {
    super.onHit(by)
    CurrentGame.score = CurrentGame.score + level
    println(level)
  }


}

