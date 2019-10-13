package com.magnusludicrum.spacedotpong.screens;

//TODO: Add Scoreboard
//TODO: Add Gameover animation and switch to gameover screen with final score
//TODO: Ideas - Powerups? Traps? Speed changes?

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.magnusludicrum.spacedotpong.SpaceDotPong;
import com.magnusludicrum.spacedotpong.utilities.CountDown;
import com.magnusludicrum.spacedotpong.utilities.GamePlayGestureListener;
import com.magnusludicrum.spacedotpong.utilities.ListenerClass;
import com.magnusludicrum.spacedotpong.utilities.MathUtilities;

import java.util.Random;

public class GamePlayScreen extends BaseScreen {

    //BG
    Texture gameBg;

    //Random Number Generator
    Random rng = new Random();

    //Game States
    public enum GameState {
        GAMESETUP,
        COUNTDOWN,
        PLAYING,
        SCORE,
        GAMEOVER
    }
    public GameState gameState;

    //Cutscenes
    CountDown countDown;

    //SCores
    public int playerScore = 0;
    public int enemyScore = 0;

    //var to determie if the server is player or enemy
    public Boolean playerServe = true;
    public Boolean inPlay = false; //If the ball has been served

    //Box2D
    World box2DWorld;
    Box2DDebugRenderer box2DDebugRenderer;
    ListenerClass listenerClass;

    //WallSpecs
    float wallWidth = 0.1f;

    //Walls
    Body bottomWallBody;
    Body topWallBody;
    Body leftWallBody;
    Body rightWallBody;

    //Ball
    public Body ballBody;
    public Fixture ballFixture;

    //Bats
    public Body playerBatBody;
    Body enemyBatBody;

    //Positioning, sizes, and speeds
    //Bats
    public float batWidth = 1f;
    float batLength = .1f;
    public final float MAX_VELOCITY = 15f;
    float playerBatXStart = viewport.getWorldWidth() / 2;
    float playerBatYStart = 4f;
    float enemyBatXStart = viewport.getWorldWidth() / 2;
    float enemyBatYStart = viewport.getWorldHeight() - 2.5f;
    public float batSpeed = 10.0f;
    //Ball
    float ballRadius = .20f;
    public float ballSpeed = 10.0f;
    public float maxBallSpeed = 15.0f;
    public float minBallSpeed = 5.0f;

    //Sensors
    Body playerSensor;
    Fixture playerSensorFixture;
    Body enemySensor;
    Fixture enemySensorFixture;

    //Input
    GamePlayGestureListener inputController;


    public GamePlayScreen(SpaceDotPong spaceDotPong) {
        super(spaceDotPong); //Parent Constructor

        //Load external resources
        loadResources(); //Textures and shit
        //Load cutscenes
        countDown = new CountDown(this);

        //Box 2D Configuration
        box2DWorld = new World(new Vector2(0, 0), true);
        listenerClass = new ListenerClass(this);
        box2DWorld.setContactListener(listenerClass);
        box2DDebugRenderer = new Box2DDebugRenderer();
        createBox2DBall();
        createBox2DWalls();
        createBox2DBats();
        createBox2DSensors();

        inputController = new GamePlayGestureListener(this);
        Gdx.input.setInputProcessor(new GestureDetector(inputController));

        //Set the games state to countdown
        gameState = GameState.GAMESETUP;

    }

    private void createBox2DSensors() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0, 3f);
        playerSensor = box2DWorld.createBody(bodyDef);
        PolygonShape tmpBox = new PolygonShape();
        tmpBox.setAsBox(viewport.getWorldWidth(), .5f);
        FixtureDef fD = new FixtureDef();
        fD.shape = tmpBox;
        fD.isSensor = true;
        playerSensorFixture = playerSensor.createFixture(fD);
        playerSensorFixture.setUserData("PlayerSensor");


        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0, viewport.getWorldHeight() - 1.5f);
        enemySensor = box2DWorld.createBody(bodyDef);
        tmpBox = new PolygonShape();
        tmpBox.setAsBox(viewport.getWorldWidth(),.5f);
        fD = new FixtureDef();
        fD.shape = tmpBox;
        fD.isSensor = true;
        enemySensorFixture = enemySensor.createFixture(fD);
        enemySensorFixture.setUserData("EnemySensor");

    }

    private void createBox2DBats() {
        playerBatBody = createBat(playerBatXStart, playerBatYStart, batWidth, batLength, true);
        enemyBatBody = createBat(enemyBatXStart, enemyBatYStart, batWidth, batLength, false);
    }

    private Body createBat(float x, float y, float width, float height, Boolean isBottom) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        Body tmpBody = box2DWorld.createBody(bodyDef);
        PolygonShape bottomWallBox = new PolygonShape();
        bottomWallBox.setAsBox(width, height);
        tmpBody.createFixture(bottomWallBox, 100); //Set a MASSIVE density so that the ball can't move the paddle.

        ///This adds little angled bits to the sides of the paddles
        if (isBottom) {
            PolygonShape leftSlope = new PolygonShape();
            leftSlope.setAsBox(width / 4, height / 4, new Vector2(-width + (width / 4), height), -60);
            PolygonShape rightSlope = new PolygonShape();
            rightSlope.setAsBox(width / 4, height / 4, new Vector2(width - (width / 4), height), 60);
            tmpBody.createFixture(leftSlope, 100); //Set a MASSIVE density so that the ball can't move the paddle.
            tmpBody.createFixture(rightSlope, 100); //Set a MASSIVE density so that the ball can't move the paddle.
        } else {
            PolygonShape leftSlope = new PolygonShape();
            leftSlope.setAsBox(width / 4, height / 4, new Vector2(-width + (width / 4), -height), 60);
            PolygonShape rightSlope = new PolygonShape();
            rightSlope.setAsBox(width / 4, height / 4, new Vector2(width - (width / 4), -height), -60);
            tmpBody.createFixture(leftSlope, 100); //Set a MASSIVE density so that the ball can't move the paddle.
            tmpBody.createFixture(rightSlope, 100); //Set a MASSIVE density so that the ball can't move the paddle.
        }

        return tmpBody;
    }
    private void createBox2DWalls() {
        //Walls take width/height measurements as half size and x/y pos is the center of the shape

        //Top and bottom
        bottomWallBody = createWall(VIRTUAL_WIDTH / 2,wallWidth / 2,VIRTUAL_WIDTH / 2, wallWidth / 2);
        topWallBody = createWall(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT - (wallWidth / 2),VIRTUAL_WIDTH / 2, wallWidth / 2);

        //Left and right
        /*
        Couple of notes:
        Because the x,y pos of the shapes are in the centre, the x origin has to be offset by half the width of the shape
        Also because of this reason, the y origin has to be set to the centre of the screens y axis (So that it covers the whole wall)
        Additionally, in order to perfeclty fit inbetween the top and bottom walls, the width of those walls has been subtracted from the height of these walls
         */
        leftWallBody = createWall(wallWidth / 2,VIRTUAL_HEIGHT/2,wallWidth / 2, (VIRTUAL_HEIGHT/2) - wallWidth);
        rightWallBody = createWall(VIRTUAL_WIDTH - (wallWidth/2),VIRTUAL_HEIGHT / 2,wallWidth / 2, (VIRTUAL_HEIGHT/2) - wallWidth);

    }

    private Body createWall( float x, float y, float width, float height) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x,y);
        Body tmpBody = box2DWorld.createBody(bodyDef);
        PolygonShape bottomWallBox = new PolygonShape();
        bottomWallBox.setAsBox(width, height);
        tmpBody.createFixture(bottomWallBox,0.0f);
        return tmpBody;
    }

    private void createBox2DBall() {

        //https://github.com/libgdx/libgdx/wiki/Box2d
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2);
        ballBody = box2DWorld.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(ballRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        ballFixture = ballBody.createFixture(fixtureDef);
        ballFixture.setUserData("BallFixture");
        circle.dispose();
    }


    /*
    loadResources
    Args: N/A
    Returns: N/A
    Desc: Loads textures
     */
    private void loadResources() {

        gameBg = new Texture(Gdx.files.internal("screens/gameplayscreen/bg.png"));

    }


    private void update(float delta) {

        switch (gameState) {
            case GAMESETUP:
                //Reset the game.
                resetGameBoard();
                gameState = GameState.COUNTDOWN;
                break;
            case COUNTDOWN:
                //Check if the countdown cutscene is finished.
                //If it is, change the game state
                if (countDown.isFinished){
                    countDown.isFinished = false;
                    //Change the gamestate to start play
                    gameState = GameState.PLAYING;
                }
                break;
            case PLAYING:
                //Hack to fix our bats being dynamic objects and getting pusjed by the ball
                Vector2 tmpPos = playerBatBody.getPosition();
                tmpPos.y = playerBatYStart;
                playerBatBody.setTransform(tmpPos, 0);
                tmpPos = enemyBatBody.getPosition();
                tmpPos.y = enemyBatYStart;
                enemyBatBody.setTransform(tmpPos, 0);

                //Hack to make sure the ball doesn't go super sayian or super slowian
                Vector2 tmpVec = ballBody.getLinearVelocity();
                if (ballBody.getLinearVelocity().x > 0 && ballBody.getLinearVelocity().x > maxBallSpeed) {
                    tmpVec.x = maxBallSpeed;
                } else if (ballBody.getLinearVelocity().x > 0 && ballBody.getLinearVelocity().x < minBallSpeed) {
                    tmpVec.x = minBallSpeed;
                }
                if (ballBody.getLinearVelocity().y > 0 && ballBody.getLinearVelocity().y > maxBallSpeed) {
                    tmpVec.y = maxBallSpeed;
                } else if (ballBody.getLinearVelocity().y > 0 && ballBody.getLinearVelocity().y < minBallSpeed) {
                    tmpVec.y = minBallSpeed;
                }
                if (ballBody.getLinearVelocity().x < 0 && ballBody.getLinearVelocity().x < -maxBallSpeed) {
                    tmpVec.x = -maxBallSpeed;
                } else if (ballBody.getLinearVelocity().x < 0 && ballBody.getLinearVelocity().x > -minBallSpeed) {
                    tmpVec.x = -minBallSpeed;
                }
                if (ballBody.getLinearVelocity().y < 0 && ballBody.getLinearVelocity().y < -maxBallSpeed) {
                    tmpVec.y = -maxBallSpeed;
                } else if (ballBody.getLinearVelocity().y < 0 && ballBody.getLinearVelocity().y > -minBallSpeed) {
                    tmpVec.y = -minBallSpeed;
                }
                ballBody.setLinearVelocity(tmpVec);

                //Handle AI and game logic
                handleAI();
                inputController.update();
                break;
            case SCORE:
                gameState = GameState.COUNTDOWN;
                //TODO: Implement score animation
                break;
            case GAMEOVER:
                break;
        }

    }

    private void resetGameBoard() {

        //Kill all velocity on the ball
        ballBody.setLinearVelocity(0,0);

        //Reset teh bats
        Vector2 tmpPlayerVec = new Vector2(playerBatXStart, playerBatYStart);
        playerBatBody.setTransform(tmpPlayerVec, 0);
        Vector2 tmpEnemyVec = new Vector2(enemyBatXStart, enemyBatYStart);
        enemyBatBody.setTransform(tmpEnemyVec, 0);

        //Either place the ball infront of the enemy of the player
        Vector2 tmpVec; //Ball Starting Position
        if (!playerServe) {
            tmpVec = new Vector2(enemyBatXStart, enemyBatYStart - (batWidth / 2));
        } else {
            tmpVec = new Vector2(playerBatXStart, playerBatYStart + (batWidth / 2));
        }

        //Actually place the ball at the vector decided above
        ballBody.setTransform(tmpVec, 0);
        ballBody.applyAngularImpulse(.01f, true);

    }

    private void handleAI() {

        //Handle serving for the enemy
        if (!inPlay && !playerServe) {
            int dirX;
            int dirY = -1; //Dir 1 is always -1 because it's going down field
            if (rng.nextBoolean()) { dirX = -1; } else { dirX = 1; };
            ballBody.applyForceToCenter( dirX * ballSpeed,  dirY * ballSpeed, true);
            Gdx.app.log("LOGIC", "Enemy Serve");
            inPlay = true;
        }

        Gdx.app.log("AI", "Ball Pos: " + ballBody.getPosition().x + "/" + ballBody.getPosition().y);
        Gdx.app.log("AI", "Screen: " + viewport.getWorldWidth() + "/" + viewport.getWorldHeight());
        Vector2 tmpVec = enemyBatBody.getLinearVelocity();
        if (ballBody.getPosition().y > viewport.getWorldHeight() / 4 && ballBody.getLinearVelocity().y > 0) {
                int difficult = 10;
                int chance = rng.nextInt(difficult);
                if (chance < 10) {
                    if (ballBody.getPosition().x > enemyBatBody.getPosition().x) {
                        if (enemyBatBody.getLinearVelocity().x < MAX_VELOCITY) {
                            enemyBatBody.applyLinearImpulse(50f, 0, enemyBatBody.getPosition().x, enemyBatBody.getPosition().y, true);
                            //enemyBatBody.setLinearVelocity(10f, 0);
                        }
                    } else if (ballBody.getPosition().x < enemyBatBody.getPosition().x) {
                        if (enemyBatBody.getLinearVelocity().x > -MAX_VELOCITY) {
                            enemyBatBody.applyLinearImpulse(-50f, 0, enemyBatBody.getPosition().x, enemyBatBody.getPosition().y, true);
                            //enemyBatBody.setLinearVelocity(-10f, 0);
                        }
                    }
                }
        }

    }



    @Override
    public void render(float delta) {
        super.render(delta); //Parent render method
        //Update method, handles game logic
        this.update(delta);
        //Render the game feild
        batch.begin();
        //Draw background
        batch.draw(gameBg, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        //Countdown Animation
        if (gameState == GameState.COUNTDOWN) {
            countDown.update(delta); //Perform all the animation nonsense
        }
        batch.end();
        //Box2D Debug Renderer
        box2DDebugRenderer.render(box2DWorld, camera.combined);
        //Step the physics
        if (gameState == GameState.PLAYING) {
            box2DWorld.step(1 / 60f, 6, 2);
        }
    }


    @Override
    public void show() {

    }


    @Override
    public void dispose() {
        super.dispose();

    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

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
