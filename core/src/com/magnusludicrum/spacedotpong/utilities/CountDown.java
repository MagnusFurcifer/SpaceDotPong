package com.magnusludicrum.spacedotpong.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.magnusludicrum.spacedotpong.screens.GamePlayScreen;

public class CountDown extends CutScene {


    //Countdown Vars
    //Animations for the Logo
    Animation<TextureRegion> countDownAnimation3;
    Animation<TextureRegion> countDownAnimation2;
    Animation<TextureRegion> countDownAnimation1;
    Animation<TextureRegion> goAnimation;
    private static final int countdown_FRAME_COLS = 3, countdown_FRAME_ROWS = 4, countdown_ANIM_LENGTH = 3;
    //Countdown animation spriteSheet
    Texture countDownSheet;
    Texture goStatic;
    //State time to control animation
    float countDownStateTime;
    //Delay time
    long countDownStartTime = 0;
    int countDownDelay = 1000;
    //Animation State
    enum CountDownAnimState {
        THREE,
        TWO,
        ONE,
        GO,
        IDLE
    }
    CountDownAnimState countDownAnimState;

    public Boolean isFinished;


    float imageWidth = 3f;
    float imageHeight = 3f;


    public CountDown(GamePlayScreen parentScreen) {
        super(parentScreen);
        loadCountDownResources();

        //Set the countdown animation state to the first animation
        countDownAnimState = CountDownAnimState.THREE;
        //Start the time
        countDownStateTime = 0f;
        isFinished = false;
    }


    private void loadCountDownResources() {
        countDownSheet = new Texture("screens/gameplayscreen/countDown.png");
        goStatic = new Texture("screens/gameplayscreen/goStatic.png");
        //Parse SpriteSheet
        TextureRegion[][] tmp = TextureRegion.split(countDownSheet,
                countDownSheet.getWidth() / countdown_FRAME_COLS,
                countDownSheet.getHeight() / countdown_FRAME_ROWS);

        //Get Animations
        TextureRegion[] threeFrames = new TextureRegion[countdown_ANIM_LENGTH];
        TextureRegion[] twoFrames = new TextureRegion[countdown_ANIM_LENGTH];
        TextureRegion[] oneFrames = new TextureRegion[countdown_ANIM_LENGTH];
        TextureRegion[] goFrames = new TextureRegion[countdown_ANIM_LENGTH];
        //Loop through the 2d array of frames and pull out the 3 rows of animation
        for (int i = 0; i < countdown_FRAME_COLS; i ++ ) {
            threeFrames[i] = tmp[0][i];
            twoFrames[i] = tmp[1][i];
            oneFrames[i] = tmp[2][i];
            goFrames[i] = tmp[3][i];
        }

        //Create the animations from the 1d frame arrays
        countDownAnimation3 = new Animation<>(.33f, threeFrames);
        countDownAnimation2 = new Animation<>(.33f, twoFrames);
        countDownAnimation1 = new Animation<>(.33f, oneFrames);
        goAnimation = new Animation<>(.33f, goFrames);
    }


    public void update(float delta) {
        /*
        update
        Args: delta - The render deltatime var
        Return: cutSceneFinished - Boolean that goes true once the cutscene is done
        Desc: Handles timing and display of the countdown animation, as well as switching
        the game state once it's completed
         */

        //Draw animation
        countDownStateTime += delta;
        TextureRegion currentFrame = new TextureRegion(goStatic);
        //Get the centre of the screen pos to render anim and sprite
        float logoPosX = (parentScreen.VIRTUAL_WIDTH / 2) - (imageWidth / 2);
        float logoPosY = (parentScreen.VIRTUAL_HEIGHT /2) - (imageHeight / 2);
        switch (countDownAnimState) {
            case THREE:
                if (countDownAnimation3.isAnimationFinished(countDownStateTime)) {
                    countDownAnimState = CountDownAnimState.TWO;
                    countDownStateTime = 0; //Reset StateTime for next Animation
                } else {
                    currentFrame = countDownAnimation3.getKeyFrame(countDownStateTime, false);
                    parentScreen.batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case TWO:
                if (countDownAnimation2.isAnimationFinished(countDownStateTime)) { //If all 5 frames have played, move on to the next anim
                    countDownAnimState = CountDownAnimState.ONE;
                    countDownStateTime = 0f; //Reset StateTime for next Animation
                } else {
                    currentFrame = countDownAnimation2.getKeyFrame(countDownStateTime, false);
                    parentScreen.batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case ONE:

                if (countDownAnimation1.isAnimationFinished(countDownStateTime)) { //If all 5 frames have played, move on to the next anim
                    countDownAnimState = CountDownAnimState.GO;
                    countDownStateTime = 0f; //Reset StateTime for next Animation
                } else {
                    currentFrame = countDownAnimation1.getKeyFrame(countDownStateTime, false);
                    parentScreen.batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case GO:
                //Draw the static logo behind this animation
                //So that it doesn't flash when it ends
                parentScreen.batch.draw(goStatic, logoPosX, logoPosY);

                if (goAnimation.isAnimationFinished(countDownStateTime)) { //If all 5 frames have played, move on to the next anim
                    countDownAnimState = CountDownAnimState.IDLE;
                    countDownStateTime = 0f; //Reset StateTime for next Animation
                    //Start the delay timer for the screen switching on the idle logo
                    countDownStartTime = TimeUtils.millis();
                } else {
                    currentFrame = goAnimation.getKeyFrame(countDownStateTime, false);
                    parentScreen.batch.draw(currentFrame, logoPosX, logoPosY, imageWidth, imageHeight);
                }
                break;
            case IDLE:
                parentScreen.batch.draw(goStatic, logoPosX, logoPosY);
                if (TimeUtils.timeSinceMillis(countDownStartTime) > countDownDelay) {
                    //Reset the countdown animation
                    countDownAnimState = CountDownAnimState.THREE;
                    countDownStateTime = 0f; //Need to reset the state time so that the anims aren't "finished"
                    //Mark cutscene as complete
                    isFinished = true;

                }
                break;
        }


    }

}
