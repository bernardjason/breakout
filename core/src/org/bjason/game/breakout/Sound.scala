package org.bjason.game.breakout

import com.badlogic.gdx.Gdx

object Sound {

  private val starting = Gdx.audio.newSound(Gdx.files.internal("data/starting.mp3"))
  private val ping = Gdx.audio.newSound(Gdx.files.internal("data/ping.wav"))
  private val powerup = Gdx.audio.newSound(Gdx.files.internal("data/powerup.wav"))

  def reset = {
    timeSinceLastPlay=0f
  }

  def playPing: Unit = {
    ping.play()
  }
  def playPowerup: Unit = {
    powerup.play()
  }
  var timeSinceLastPlay=0f
  def playStarting: Unit = {
    timeSinceLastPlay = timeSinceLastPlay + Gdx.graphics.getDeltaTime
    if ( timeSinceLastPlay >= 0 ) {
      starting.play()
      timeSinceLastPlay = -10
    }
  }
}


