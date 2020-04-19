package org.bjason.game.breakout

import com.badlogic.gdx.Gdx

object Sound {

  lazy private val ping = Gdx.audio.newMusic(Gdx.files.internal("data/ping.wav"))
  lazy private val powerup = Gdx.audio.newMusic(Gdx.files.internal("data/powerup.wav"))
  lazy private val starting = Gdx.audio.newMusic(Gdx.files.internal("data/starting.wav"))

  def playPing: Unit = {
    ping.play()
  }
  def playPowerup: Unit = {
    powerup.play()
  }
  def playStarting: Unit = {
    if ( ! starting.isPlaying ) starting.play()
  }
}


