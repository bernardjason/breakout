package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor

object PrizeBrick {
  val size = 40
  val sizex = size
  val sizey = size / 2

  val points: Array[Float] = Array[Float](
    -PrizeBrick.sizex, -PrizeBrick.sizey,
    -PrizeBrick.sizex, PrizeBrick.sizey,
    PrizeBrick.sizex, PrizeBrick.sizey,
    PrizeBrick.sizex, -PrizeBrick.sizey
  )

  val pixmapFullSize = new Pixmap(Gdx.files.internal("data/prizebrick.png"))
  val height = BasicBrick.sizey * 2 + 1
  val width = BasicBrick.sizex * 2 + 1
  val pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888)
  pixmap.drawPixmap(pixmapFullSize, 0, 0, pixmapFullSize.getWidth, pixmapFullSize.getHeight, 0, 0, width, height)
  val texture = new Texture(pixmap)
  pixmapFullSize.dispose()
  pixmap.dispose()


}

class PrizeBrick(startx: Int, starty: Int) extends Actor with MyCollision {

  override val points: Array[Float] = PrizeBrick.points
  val speed = 200 + ((CurrentGame.difficult) * 50)
  var notPickedUp = true

  protected val texture = PrizeBrick.texture
  setSize(texture.getWidth, texture.getHeight)
  setX(startx)
  setY(starty)
  polygon.setPosition(startx, starty)

  override def onHit(by: Actor): Unit = {
    if (notPickedUp) {
      notPickedUp = false
      CurrentGame.removeActor(this)
      Sound.playPowerup
      CurrentGame.score = CurrentGame.score + 10
      CurrentGame.balls = CurrentGame.balls + 1
      if (Math.random() * 1000 > 500) {
        CurrentGame.balls = CurrentGame.balls + 1
        val b = new Ball(getX.toInt, getY.toInt + PrizeBrick.sizey+5)
        CurrentGame.addActor(b)
      }
    }
  }

  override def draw(batch: Batch, parentAlpha: Float): Unit = {

    //if (getY > 160) {

      setY(getY - speed * CurrentGame.deltaTime)
      polygon.setPosition(getX, getY)
      val y = getY
      if (y <= PrizeBrick.sizey) {
        CurrentGame.removeActor(this)
      }
    //}

    batch.draw(texture, getX - PrizeBrick.sizex, getY - PrizeBrick.sizey)
  }

  override def dispose(): Unit = {
  }
}

