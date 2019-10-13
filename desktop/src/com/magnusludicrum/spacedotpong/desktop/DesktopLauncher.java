package com.magnusludicrum.spacedotpong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.magnusludicrum.spacedotpong.SpaceDotPong;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 580;
		config.height = 960;
		new LwjglApplication(new SpaceDotPong(), config);
	}
}
