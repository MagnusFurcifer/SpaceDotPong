package com.magnusludicrum.spacedotpong.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.magnusludicrum.spacedotpong.screens.GamePlayScreen;

public class ListenerClass implements ContactListener {

    GamePlayScreen gamePlayScreen;

    public ListenerClass(GamePlayScreen gamePlayScreen) {
        this.gamePlayScreen = gamePlayScreen;
    }

    @Override
    public void endContact(Contact contact) {


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void beginContact(Contact contact) {

        if (gamePlayScreen.gameState == GamePlayScreen.GameState.PLAYING) {
            if (contact.getFixtureA().getUserData() == "PlayerSensor" || contact.getFixtureB().getUserData() == "PlayerSensor") {
                if (contact.getFixtureA().getUserData() == "BallFixture" || contact.getFixtureB().getUserData() == "BallFixture") {
                    Gdx.app.log("COLLISION", "PLAYER CONTACT");
                    gamePlayScreen.playerScore++; //Increment score (Goal scored)
                    gamePlayScreen.inPlay = false; //Mark game as not in placy. This allows the board to reset and the ball to not move awaiting serve
                    gamePlayScreen.playerServe = false; //Since the ball was scored in the player sgoal it is the AIs serve
                    gamePlayScreen.gameState = GamePlayScreen.GameState.GAMESETUP; //Change state to GAMESETUP. This will prompt resetting the board.
                }
            } else if (contact.getFixtureA().getUserData() == "EnemySensor" || contact.getFixtureB().getUserData() == "EnemySensor") {
                if (contact.getFixtureA().getUserData() == "BallFixture" || contact.getFixtureB().getUserData() == "BallFixture" ) {
                    Gdx.app.log("COLLISION", "ENEMY CONTACT");
                    gamePlayScreen.enemyScore++;
                    gamePlayScreen.inPlay = false;
                    gamePlayScreen.playerServe = true;
                    gamePlayScreen.gameState = GamePlayScreen.GameState.GAMESETUP;
                }
            }
        }

    }
}