package com.magnusludicrum.spacedotpong.screens;

//TODO: Background (Parralax stars maybe?)
//TODO: Button skins, Text buttons or image buttons?
//TODO: Game Title Image
//TODO: Sound effects for menuing
//TODO: BGM For main menu (Background Music).
//TODO: Create options screen (Sound options, reset progress, etc).

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.magnusludicrum.spacedotpong.SpaceDotPong;

public class MainMenuScreen extends BaseScreen {

    //UI Stage
    Stage stage;
    Label label;

    public MainMenuScreen(final SpaceDotPong spaceDotPong) {
        super(spaceDotPong); //Parent constructor

        //Initalizing UI Stage
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("screens/mainmenuscreen/uiskin.json"));
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);
        label = new Label("PongBoi", skin);
        Table root = new Table(skin);
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("default-pane"));
        root.add(label).row();
        TextButton tmpButton = new TextButton("Play", skin);
        tmpButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                spaceDotPong.setScreen(spaceDotPong.gamePlayScreen);
            }
        });
        root.add(tmpButton).row();
        tmpButton = new TextButton("Options", skin);
        tmpButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Released");
            }
        });
        root.add(tmpButton).row();
        stage.addActor(root);

    }


    @Override
    public void render(float delta) {
        super.render(delta); //Parent render method

        //Update method for teh stage maybe?
        stage.act();
        //Render Stage
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose(); //Parent dispose Method
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height); //Parent resize methof
    }

    @Override
    public void pause() {

    }

    @Override
    public void show() {

    }
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


}
