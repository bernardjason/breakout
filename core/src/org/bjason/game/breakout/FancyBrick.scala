package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
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

  val pixmapFullSize = new Pixmap(Gdx.files.internal("data/fancybrick.png"))
  val height =BasicBrick.sizey * 2 + 1
  val width =BasicBrick.sizex * 2 + 1
  val pixmap = new Pixmap(width,height, Pixmap.Format.RGBA8888)
  pixmap.drawPixmap(pixmapFullSize,0,0,pixmapFullSize.getWidth,pixmapFullSize.getHeight,0,0,width,height)
  val texture = new Texture(pixmap)
  pixmapFullSize.dispose()
  pixmap.dispose()

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

