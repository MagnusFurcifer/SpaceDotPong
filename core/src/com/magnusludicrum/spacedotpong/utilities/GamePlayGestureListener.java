package com.magnusludicrum.spacedotpong.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.magnusludicrum.spacedotpong.screens.GamePlayScreen;

public class GamePlayGestureListener implements GestureListener {

    GamePlayScreen gamePlayScreen;

    public GamePlayGestureListener(GamePlayScreen gamePlayScreen) {
        this.gamePlayScreen = gamePlayScreen;
    }

    public void update() {

        if (Gdx.input.isTouched()) {
            //Get PlayerBat Details
            Vector2 vel = gamePlayScreen.playerBatBody.getLinearVelocity();
            Vector2 pos = gamePlayScreen.playerBatBody.getPosition();

            /*
            Explanation required so I don't forget
            The Gdx.input.getX/getY() pos coods are relative to the screen
            by using camera.unproject on them, we can convert them to local "World" coods.
            */
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gamePlayScreen.camera.unproject(touchPos);

            if (touchPos.x > (gamePlayScreen.playerBatBody.getPosition().x + (gamePlayScreen.batWidth / 4)) && vel.x < gamePlayScreen.MAX_VELOCITY) {
                gamePlayScreen.playerBatBody.setLinearVelocity(gamePlayScreen.batSpeed, 0);
            } else if (touchPos.x < (gamePlayScreen.playerBatBody.getPosition().x - (gamePlayScreen.batWidth / 4)) && vel.x > -gamePlayScreen.MAX_VELOCITY) {
                gamePlayScreen.playerBatBody.setLinearVelocity(-gamePlayScreen.batSpeed, 0);
            } else {
                gamePlayScreen.playerBatBody.setLinearVelocity(0, 0);
            }
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        //This fling is bascially only used for the server
        Gdx.app.log("INPUT", "ServerXY: " + velocityX + "/" + velocityY);

        //This basically determines if its a flick up or not
        //if (velocityX > -1000 && velocityX < 1000 ) {
            //if (velocityY > 1000 || velocityY < -1000) {
        if (!gamePlayScreen.inPlay) {
            if (!gamePlayScreen.inPlay) {
                if (gamePlayScreen.playerServe) {
                    float dirX;
                    float dirY = 1;
                    if (velocityX > 0) {
                        dirX = 1;
                    } else if (velocityX < 0) {
                        dirX = -1;
                    } else {
                        dirX = 1;
                    }
                    gamePlayScreen.ballBody.applyForceToCenter(dirX * gamePlayScreen.ballSpeed, dirY * gamePlayScreen.ballSpeed, true);
                    gamePlayScreen.inPlay = true;
                }
            }
        }
           // }
        //}

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop () {
    }
}