package org.bjason.game.breakout

import com.badlogic.gdx.scenes.scene2d.Actor

import scala.collection.mutable.ListBuffer

// perhaps should have used box2d....

object CollisionDetection {
  def checkPlayerAgainst(player: Player, against: ListBuffer[Actor with MyCollision], extra: (Int, Int) => Unit = (_, _) => {}) = {
    var player_hit = false
    for (b <- against) {
      for (xy <- b.polygon.getTransformedVertices.grouped(2)) {
        if ( !player_hit && player.polygon.contains(xy(0),xy(1))){
          player_hit = true
          b.onHit(player)
          extra(b.getX.toInt, b.getY.toInt)
        }
      }
    }
    if ( ! player_hit ) {
      for (xy <- player.polygon.getTransformedVertices.grouped(2)) {
        for (a <- against.filter(!_.isInstanceOf[Ball])) {
          if (!player_hit) {
            a match {
              case f: PrizeBrick => if (f.polygon.contains(xy(0), xy(1))) {
                a.onHit(player)
                CurrentGame.removeActor(f)
                extra(a.getX.toInt, a.getY.toInt)
              }
              case _ => println("What?")
            }
          }
        }
      }
    }
    player_hit
  }

  def checkCollision(what: ListBuffer[Actor with MyCollision], against: ListBuffer[Actor with MyCollision], extra: (Int, Int) => Unit = (_, _) => {}) = {
    for (w <- what) {
      for (a <- against) {
        var beenDone = false
        for (xy <- w.polygon.getTransformedVertices.grouped(2)) {
          if (!beenDone) {
            a match {
              case b: Brick => if (b.polygon.contains(xy(0), xy(1))) {
                a.onHit(w)
                w.onHit(a)
                extra(a.getX.toInt, a.getY.toInt)
                beenDone = true
              }
              case _ => println("WHAT????")
            }
          }
        }
      }
    }
  }
}
