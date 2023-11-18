package com.terje.chesstacticstrainer_full;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
    	setBackground(new Background(0.22f,0.32f,0.435f));
        attachChild(new Text(ActivityGame.CAMERA_WIDTH/2,ActivityGame.CAMERA_HEIGHT/2, resourceManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }
    
}
