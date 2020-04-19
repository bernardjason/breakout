package org.bjason.game.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.{Actor, Stage}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object CurrentGame {

  def render(): Unit = {
    countDownToPlaySeconds=countDownToPlaySeconds+Gdx.graphics.getDeltaTime
  }


  var hashCounter = 1
  val canBeHitActors: ListBuffer[Actor with MyCollision] = new ListBuffer[Actor with MyCollision]()
  val bricks: ListBuffer[Actor with MyCollision] = new ListBuffer[Actor with MyCollision]()
  var countDownToPlaySeconds = 0f

  def okToPlayNow = {
     countDownToPlaySeconds > 0
  }


  def getHashCounter = {
    hashCounter = hashCounter + 1
    hashCounter
  }

  private val toRemove = new ArrayBuffer[Actor]()
  private val toAdd = new ArrayBuffer[Actor]()
  lazy val font = getFont(20)
  var score = 0
  var balls = 0
  val BALLS = 3
  var difficult = 1
  var ballReadyToFire = true
  var deltaTime = 0f

  def ballFired = {
    ballReadyToFire = false
  }

  def addActor(a: Actor) {
    a match {
      case b: PrizeBrick =>
        canBeHitActors += b
      case b: Brick =>
        bricks += b
      case m: MyCollision => canBeHitActors += m
      case _ =>
    }
    toAdd += a
  }

  def removeActor(a: Actor) {
    toRemove += a
  }

  def doAddAndRemove(stage: Stage): Unit = {
    for (a <- toAdd) {
      stage.addActor(a)
    }
    for (a <- toRemove) {
      stage.getRoot.removeActor(a)
      a match {
        case b: Brick =>
          canBeHitActors -= b
          bricks -= b
          b.dispose()
        case m: Actor with MyCollision =>
          canBeHitActors -= m
          m.dispose()
        case _ => println("WHAT THE!!!")
      }
    }
    toAdd.clear()
    toRemove.clear()
  }

  def reset(): Unit = {
    for( a <- canBeHitActors) {
      a.dispose()
    }
    bricks.clear()
    canBeHitActors.clear()
    toAdd.clear()
    toRemove.clear()
    score = 0
    balls = BALLS
    difficult = 1
    countDownToPlaySeconds = -1.5f
    ballReadyToFire = true
  }

  def getFont(size: Int) = {

    val fontFile = Gdx.files.internal("data/OpenSans-Italic.ttf")
    val generator = new FreeTypeFontGenerator(fontFile)
    val parameter = new FreeTypeFontParameter()
    parameter.color = Color.WHITE
    parameter.size = size

    val font = generator.generateFont(parameter)
    generator.dispose()
    font
  }


}

