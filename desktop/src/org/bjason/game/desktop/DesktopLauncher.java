package org.bjason.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.bjason.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		MainGame g = new MainGame();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=1920;
		config.height=700;
		//config.height=900;
		//config.fullscreen = true;
		new LwjglApplication(g, config);
	}
}
