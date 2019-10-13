package com.magnusludicrum.spacedotpong.screens;

//TODO: Add sound effect for bomb expoloding

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.magnusludicrum.spacedotpong.SpaceDotPong;

public class SplashScreen extends BaseScreen {

    //Resources
    Texture backGround;

    //Animations for the Logo
    Animation<TextureRegion> bombFuseAnimation;
    Animation<TextureRegion> bombExplodeAnimation;
    Animation<TextureRegion> bombLogoAnimation;
    private static final int bomb_FRAME_COLS = 5, bomb_FRAME_ROWS = 3, bomb_ANIM_LENGTH = 5;
    //Logo animation spriteSheet
    Texture bombSheet;
    //Static log
    Texture logoStatic;
    //Animation time variable
    float stateTime;

    //Splash Screen States
    enum AnimState {
        FUSE, //Shows fuse animation
        EXPLODE, //Shows explode animation
        LOGO, //Shows the logo animation
        IDLE //Shows static logo
    }
    //Var to hold the current animation state
    AnimState currentState;

    //Idle Logo Timer Vars
    long startTime;
    int logoScreenSwitchDelay = 1500;

    //Sizes
    float imageWidth = 3f;
    float imageHeight = 3f;

    public SplashScreen(SpaceDotPong spaceDotPong) {
        super(spaceDotPong); //Parent constructor

        loadResources();
        //Set the state to "FUSE" to trigger the first animation
        currentState = AnimState.FUSE;
    }

    private void loadResources() {
        backGround = new Texture("screens/splashscreen/bg.png");
        bombSheet = new Texture("screens/splashscreen/logoAnimation.png");
        logoStatic = new Texture("screens/splashscreen/logoStatic.png");
        //Parse SpriteSheet
        TextureRegion[][] tmp = TextureRegion.split(bombSheet,
                bombSheet.getWidth() / bomb_FRAME_COLS,
                bombSheet.getHeight() / bomb_FRAME_ROWS);

        //Get Animations
        TextureRegion[] fuseFrames = new TextureRegion[bomb_ANIM_LENGTH];
        TextureRegion[] explodeFrame = new TextureRegion[bomb_ANIM_LENGTH];
        TextureRegion[] logoFrames = new TextureRegion[bomb_ANIM_LENGTH];
        //Loop through the 2d array of frames and pull out the 3 rows of animation
        for (int i = 0; i < bomb_FRAME_COLS; i ++ ) {
            fuseFrames[i] = tmp[0][i];
            explodeFrame[i] = tmp[1][i];
            logoFrames[i] = tmp[2][i];
        }

        //Create the animations from the 1d frame arrays
        bombFuseAnimation = new Animation<>(.25f, fuseFrames);
        bombExplodeAnimation = new Animation<>(.05f, explodeFrame);
        bombLogoAnimation = new Animation<>(.25f, logoFrames);

        //0 out the state timer
        stateTime = 0f;

    }

    @Override
    public void render(float delta) {
        super.render(delta); //Parent render method

        //Beginning of rendering
        batch.begin();
        //Draw Background
        batch.draw(backGround, 0, 0, camera.viewportWidth, camera.viewportHeight);
        //Draw animation
        stateTime += delta;
        TextureRegion currentFrame = new TextureRegion(logoStatic);
        //Get the centre of the screen pos to render anim and sprite
        float logoPosX = (viewport.getWorldWidth()/ 2) - (imageWidth / 2);
        float logoPosY = (viewport.getWorldHeight() / 2) - (imageHeight / 2);
        switch (currentState) {
            case FUSE:
                if (bombFuseAnimation.isAnimationFinished(stateTime)) {
                    currentState = AnimState.EXPLODE;
                    stateTime = 0f; //Reset StateTime for next Animation
                } else {
                    currentFrame = bombFuseAnimation.getKeyFrame(stateTime, false);
                    batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case EXPLODE:
                if (bombExplodeAnimation.isAnimationFinished(stateTime)) { //If all 5 frames have played, move on to the next anim
                    currentState = AnimState.LOGO;
                    stateTime = 0f; //Reset StateTime for next Animation
                } else {
                    currentFrame = bombExplodeAnimation.getKeyFrame(stateTime, false);
                    batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case LOGO:

                //Draw the static logo behind this animation
                //So that it doesn't flash when it ends
                batch.draw(logoStatic, logoPosX, logoPosY);

                if (bombLogoAnimation.isAnimationFinished(stateTime)) { //If all 5 frames have played, move on to the next anim
                    currentState = AnimState.IDLE;
                    stateTime = 0f; //Reset StateTime for next Animation
                    //Start the delay timer for the screen switching on the idle logo
                    startTime = TimeUtils.millis();
                } else {
                    currentFrame = bombLogoAnimation.getKeyFrame(stateTime, false);
                    batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case IDLE:
                batch.draw(logoStatic, logoPosX, logoPosY);
                if (TimeUtils.timeSinceMillis(startTime) > logoScreenSwitchDelay) {
                    spaceDotPong.setScreen(spaceDotPong.gamePlayScreen);
                }
                break;
        }


        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose(); //Parent dispose method
        backGround.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height); //parent resize method

    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
