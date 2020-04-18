package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

case class Ball(startx: Int, starty: Int) extends Actor with MyCollision {
  val size = 9
  override val points: Array[Float] = Array[Float](
    -size, -size,
    0, size,
    size, -size
  )
  val dir = new Vector2(0, 1)
  var speed = 6

  val pixmap = new Pixmap(size * 2, size * 2, Pixmap.Format.RGBA8888)
  pixmap.setColor(Color.YELLOW)
  pixmap.drawCircle(size, size, size / 2)
  val texture = new Texture(pixmap)
  pixmap.dispose()

  setPosition(startx, starty)

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    setPosition(getX + dir.x * speed * parentAlpha, getY + dir.y * speed * parentAlpha)
    polygon.setPosition(getX, getY)
    val x = getX
    val y = getY
    if (x <= size || x >= Gdx.graphics.getWidth-size) dir.x = dir.x * -1
    if (y >= Gdx.graphics.getHeight-size) dir.y = dir.y * -1
    batch.draw(texture, getX - size, getY - size)

    if ( y <= size ) {
      CurrentGame.removeActor(this)
    }
  }

  override def onHit(by: Actor): Unit = {
    by match {
      case p: Player =>
        dir.scl(1,-1)
        val diff = getX - p.getX - size/2
        if (diff != 0) {
          val fudge = diff / p.sizex
          dir.x = dir.x + fudge
        }
        val max = 0.5f
        dir.x = if (dir.x > max) {
          max
        } else if (dir.x < -max) {
          -max
        } else {
          dir.x
        }
      case b:Brick =>
        dir.scl(1,-1)
      case _ =>
    }
  }

  override def dispose(): Unit = {
    texture.dispose()
  }
}
