package com.magnusludicrum.spacedotpong;

import com.badlogic.gdx.Game;
import com.magnusludicrum.spacedotpong.screens.GamePlayScreen;
import com.magnusludicrum.spacedotpong.screens.LevelSelectScreen;
import com.magnusludicrum.spacedotpong.screens.MainMenuScreen;
import com.magnusludicrum.spacedotpong.screens.SplashScreen;

public class SpaceDotPong extends Game {

	//Define screens for the game.
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;
	public GamePlayScreen gamePlayScreen;

	@Override
	public void create () {

		//Initialize the Screens
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		levelSelectScreen = new LevelSelectScreen(this);
		gamePlayScreen = new GamePlayScreen(this);

		//Game starts on the SplashScreen
		this.setScreen(splashScreen);

	}

	@Override
	public void render () {
		super.render(); //Required when using screens
	}

	@Override
	public void dispose () {

	}
}