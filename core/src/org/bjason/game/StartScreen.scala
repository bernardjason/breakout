package org.bjason.game

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, GL20, Texture}

class StartScreen extends LoopTrait {
  private[game] var batch: SpriteBatch = null
  lazy val ascii = DrawAscii(normalSize = 64)
  val bernSoftHeight = 250
  lazy val bernsoft = List(
    new Texture(ascii.getPixmapForString("BERNIESOFT", 1280, bernSoftHeight, Color.YELLOW)),
    new Texture(ascii.getPixmapForString("BERNIESOFT", 1280, bernSoftHeight, Color.RED)),
    new Texture(ascii.getPixmapForString("BERNIESOFT", 1280, bernSoftHeight, Color.BLUE)),
  )

  val backgroundColour = Color.BLACK //new Color(0,0,0.3f,1)

  lazy val instructions = ascii.getRegularTextAsTexture(
    "         arrow keys rotate, space fire, left shift thrust\n\n        radar shows location of asteroids\n\n        GOOD LUCK!!", 1024, 400, backgroundColour)
  lazy val keys = new Texture(Gdx.files.internal("data/keys.png"))
  var mainTitleShow = 0f
  var mainTitley = 0f
  var instructionsy = 0f
  val SHOW_MAIN_TITLE = 0.5f //2
  var delayMoveOn = 0

  override def create(): Unit = {
    batch = new SpriteBatch()
    instructionsy = -instructions.getHeight
  }

  override def firstScreenSetup(): Unit = {
    super.firstScreenSetup()
    mainTitleShow = 0
    mainTitley = 0
    instructionsy = -instructions.getHeight
    delayMoveOn = 60
  }

  override def render(): Unit = {
    delayMoveOn = delayMoveOn - 1
    if (delayMoveOn < 0 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      this.screenComplete = true
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      System.exit(0)
    }
    Gdx.gl.glClearColor(backgroundColour.r, backgroundColour.g, backgroundColour.b, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    var y = mainTitley
    for (t <- bernsoft) {
      batch.draw(t, 0, y)
      y = y + bernSoftHeight
    }
    batch.draw(keys, Gdx.graphics.getWidth / 2 - instructions.getWidth / 2 - keys.getWidth, instructionsy)
    batch.draw(instructions, Gdx.graphics.getWidth / 2 - instructions.getWidth / 2, instructionsy)
    batch.end()

    mainTitleShow = mainTitleShow + Gdx.graphics.getDeltaTime
    val speed = Gdx.graphics.getDeltaTime * 80

    if (mainTitleShow > SHOW_MAIN_TITLE) {
      mainTitley = mainTitley + speed
      if (instructionsy <= 0) {
        instructionsy = instructionsy + speed
      }
    }
  }

  override def dispose(): Unit = {
    batch.dispose()
  }
}


