package com.icefill.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.icefill.game.SigmaFiniteDungeon;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SigmaFiniteDungeon(), config);
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
	}
}
