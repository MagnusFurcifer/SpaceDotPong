package com.magnusludicrum.spacedotpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.magnusludicrum.spacedotpong.SpaceDotPong;

public class BaseScreen implements Screen {
    //Root game class
    SpaceDotPong spaceDotPong;

    //World Cood Settings
    public final float VIRTUAL_WIDTH = 9;
    public final float VIRTUAL_HEIGHT = 16;

    //Camera and Viewport
    Viewport viewport;
    public Camera camera;
    //Rendering Resources
    public SpriteBatch batch;

    public BaseScreen(SpaceDotPong spaceDotPong) {
        //Grab the root game class
        this.spaceDotPong = spaceDotPong;
        //Initalize camera and viewport
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply(false);
        //Centre the Camera
        camera.position.set(viewport.getWorldWidth() / 2,viewport.getWorldHeight() / 2, 0);
        //Initialize Rendering Resources
        batch = new SpriteBatch();

        Gdx.app.log("OKAY", "VIRTUAL: " + VIRTUAL_WIDTH + "/" + VIRTUAL_HEIGHT);
        Gdx.app.log("OKAY", "Camera: " + camera.viewportWidth + "/" + camera.viewportHeight);
        Gdx.app.log("OKAY", "Camera POS: " + camera.position.x + "/" + camera.position.y);

        Gdx.app.log("OKAY", "Viewport World: " + viewport.getWorldWidth() + "/" + viewport.getWorldHeight());
        Gdx.app.log("OKAY", "Viewport Screen: " + viewport.getScreenWidth() + "/" + viewport.getScreenHeight());

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Update camera
        camera.update();
        //Set background
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update projection matrix
        batch.setProjectionMatrix(camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        //Dispose of the SpriteBatch
        batch.dispose();
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
