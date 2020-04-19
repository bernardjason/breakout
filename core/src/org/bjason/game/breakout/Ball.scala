package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import org.bjason.game.breakout.Ball.size

object Ball {
  val size = 9
  val pixmap = new Pixmap(size * 2, size * 2, Pixmap.Format.RGBA8888)
  pixmap.setColor(Color.YELLOW)
  pixmap.drawCircle(size, size, size / 2)
  pixmap.drawCircle(size, size, size / 2-1)
  pixmap.drawCircle(size, size, size / 2-2)
  val texture = new Texture(pixmap)
  pixmap.dispose()

  val points: Array[Float] = Array[Float](
    -size, -size,
    0, size,
    size,-size
  )

}
class Ball(startx: Int, starty: Int) extends Actor with MyCollision {
  val dir = new Vector2(0, 1)
  val speed = 400 + ((CurrentGame.difficult) * 100)

  override val points: Array[Float] = Ball.points

  setPosition(startx, starty)

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    setPosition(getX + dir.x * speed * CurrentGame.deltaTime, getY + dir.y * speed * CurrentGame.deltaTime)
    polygon.setPosition(getX, getY)
    val x = getX
    val y = getY
    if (x <= Ball.size || x >= Gdx.graphics.getWidth-Ball.size) dir.x = dir.x * -1
    if (y >= Gdx.graphics.getHeight-Ball.size) dir.y = dir.y * -1
    batch.draw(Ball.texture, getX - Ball.size, getY - Ball.size)

    if ( y <= Ball.size ) {
      CurrentGame.balls = CurrentGame.balls -1
      CurrentGame.removeActor(this)
    }
  }

  override def onHit(by: Actor): Unit = {
    by match {
      case p: Player =>
        dir.scl(1,-1)
        val diff = getX - p.getX - Ball.size/2
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
  }

}
