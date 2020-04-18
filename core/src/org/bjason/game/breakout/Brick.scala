package org.bjason.game.breakout

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class Brick extends Actor with MyCollision {

  protected  val texture:Texture

  override def onHit(by: Actor): Unit = {
    super.onHit(by)
    CurrentGame.removeActor(this)
  }

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    batch.draw(texture, getX - BasicBrick.sizex, getY - BasicBrick.sizey)
  }

  override def dispose(): Unit = {
  }
}
