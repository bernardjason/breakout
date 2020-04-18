package org.bjason.game.breakout

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Actor

trait MyCollision {
  val points:Array[Float]

  lazy val polygon = new Polygon(points)

  // WHY? Because stage wasnt always removing actors
  val hash = CurrentGame.getHashCounter
  override def hashCode(): Int = {
    hash
  }
  override def equals(obj: Any): Boolean = {
    hash == obj.hashCode()
  }

  def dispose()

  def onHit(by:Actor) = {

  }
}
