package com.terje.chesstacticstrainer_full;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.terje.chesstacticstrainer_full.SceneManager.SceneType;
import com.terje.chesstacticstrainer_full.WebService.WsCallback;

public class EndingScene extends BaseScene {

	final int SW = ActivityGame.CAMERA_WIDTH;
	final int SH = ActivityGame.CAMERA_HEIGHT;
	final int RW = 55;

	boolean touched = false;

	@Override
	public void createScene() {

		Text txt = new Text(SW/2,SH-SH/10, resourceManager.font, "You did it!", vbom);
		attachChild(txt);

		txt = new Text(SW/2,SH-SH/10-RW, resourceManager.mediumWhiteFont, "Final Score: "+resourceManager.getTotalScore(), vbom);
		attachChild(txt);	    
		getWorldStats();
		resourceManager.endMusic.play();
		touched = false;
		ButtonSprite okButton = new  ButtonSprite(SW/2, SH/8, ResourceManager.getInstance().ok_region, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (!touched) {

					touched = true;
					EndingScene.this.unregisterTouchArea(this);
					if (!resourceManager.isPaid()) {
						activity.runOnUiThread(new Runnable() {

							public void run() {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										activity);

								// set title
								alertDialogBuilder.setTitle("There is more");

								// set dialog message
								alertDialogBuilder
								.setMessage("Support us and get the Advanced levels by buying the full version! This will also give you access to all future updates for free.")
								.setCancelable(false)
								.setPositiveButton("Show me",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										Uri uri = Uri.parse("market://search?q=pub:Grand Design".replace(" ","%20"));
										Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
										try {
											EndingScene.this.activity.startActivity(goToMarket);
										} catch (ActivityNotFoundException e) {
											Toast.makeText(EndingScene.this.activity, "Couldn't reach the Market", Toast.LENGTH_LONG).show();
										}
										touched = false;
									}
								})
								.setNegativeButton("Later",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.dismiss();
										EndingScene.this.onBackKeyPressed();
										touched = false;
									}
								});


								AlertDialog alertDialog = alertDialogBuilder.create();
								// show it
								alertDialog.show();
							}
						});
					} else {
						activity.runOnUiThread(new Runnable() {

							public void run() {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										activity);

								// set title
								alertDialogBuilder.setTitle("This is the end");

								// set dialog message
								alertDialogBuilder
								.setMessage("You have done the almost impossible by finishing this game. You are now a true Chess Tactics Master. If you want to, you can now restart and play the game in Master mode. This will make the puzzles more challenging.")
								.setCancelable(false)
								.setPositiveButton("OK!",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										touched = false;
										resourceManager.goMaster();
										EndingScene.this.onBackKeyPressed();
									}
								})
								.setNegativeButton("Not now!",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.dismiss();
										EndingScene.this.onBackKeyPressed();
										touched = false;
									}
								});


								AlertDialog alertDialog = alertDialogBuilder.create();
								// show it
								alertDialog.show();
							}
						});
						
					}
				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			};
		

		registerTouchArea(okButton);

		attachChild(okButton);
	}

	private void getWorldStats() {


		if (resourceManager.isNetworkAvailable()) {
			final WsCallback afterPerfCall = new WsCallback() {

				@Override
				public void doSomething(String res) {
					Log.d("schack","Got back: "+res);
					if (res==null)
						return;
					String[] r = res.split(",");

					if (r==null||r.length!=2)
						return;
					float numberOfPlayers = Float.parseFloat(r[0]);
					float pos = Float.parseFloat(r[1])+1.0f;				
					Text txt;
					txt = new Text(SW/2,SH-SH/10-RW*3, resourceManager.smallWhiteFont, "Players that have reached", vbom);
					attachChild(txt);
					txt = new Text(SW/2,SH-SH/10-RW*4, resourceManager.smallWhiteFont, "this level: "+(int)numberOfPlayers, vbom);
					attachChild(txt);
					txt = new Text(SW/2,SH-SH/10-RW*5, resourceManager.mediumWhiteFont, "Your position is: "+(int)pos, vbom);
					attachChild(txt);	    
					int p = (int)((pos/numberOfPlayers)*100.0f);
					String result = (p>75)?"Apprentice":(p>50)?"Good":(p>25)?"Average!":(p>10)?"Excellent!":(p>1)?"Elite":"Otherworldly!";

					txt = new Text(SW/2,SH-SH/10-RW*7, resourceManager.mediumWhiteFont, "Your achievement: ", vbom);
					attachChild(txt);
					txt = new Text(SW/2,SH-SH/10-RW*8, resourceManager.mediumWhiteFont, result, vbom);
					txt.setColor(Color.YELLOW);
					txt.registerEntityModifier(FirstTimeScene.zoom(2.0f));
					attachChild(txt);

				}
			};
			WebService ws = new WebService(afterPerfCall);
			ws.execute("performance=&uuid='"+resourceManager.getMyId()+"'&level="+resourceManager.getHighestLevelCleared());

		}



	}

	@Override
	public void onBackKeyPressed() {
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				SceneManager.getInstance().createScene(SceneType.SCENE_MENU);					
			}
		});
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneManager.SceneType.SCENE_ENDING;
	}

	@Override
	public void disposeScene() {
		resourceManager.unloadEndResources();
		engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
            	detachChildren();
        		detachSelf();
        	}
		});
	}
	/*
	 void startFireworksExplosion(float fCoordX, float fCoordY, int iParticleCount, double dInitVel, double dSpreading, int iDecelerationPercent)
	    {
	        // Each particle must be given a unique velocity for the fireworks
	        // explosion to look realistic. This is accomplished first by calculating
	        // a velocity increment based on the number of particles, and then by
	        // specifying num_particles velocities, where each velocity is a unique
	        // multiple of the increment.
	        int num_particles = iParticleCount;
	        Random generator = new Random();

	        float fDecelPercent = iDecelerationPercent/100.0f;
	        double incr = dSpreading/num_particles;
	        double vel = dInitVel;

	        while (--num_particles >= 0)
	        {
	            // Calculate particle's departure angle in terms of degrees.
	            int ang = generator.nextInt(359);//0..359

	            // Establish appropriate departure velocity.
	            vel += incr;

	            // Generate the particle so that it leaves the fireworks explosion's
	            // center in the appropriate direction and at the appropriate
	            // velocity.
	            Sprite spriteParticle = new Sprite(fCoordX, fCoordY, mParticleTextureRegion);

	            //convert angle to X,Y velocity
	            float fVelocityX = (float)(Math.cos (Math.toRadians (ang))*vel);
	            float fVelocityY = (float)(Math.sin (Math.toRadians (ang))*vel);
	            spriteParticle.setVelocity(fVelocityX, fVelocityY);

	            //calculate air resistance that acts opposite to particle velocity
	            float fVelXopposite = TogglePosNeg(fVelocityX);
	            float fVelYopposite = TogglePosNeg(fVelocityY);
	            //x% of deceleration is applied (that is opposite to velocity)
	            spriteParticle.setAcceleration(fVelXopposite*fDecelPercent, fVelYopposite*fDecelPercent);

	            //add particle to scene
	            this.mEngine.getScene().getBottomLayer().addEntity(spriteParticle);

	        }
	    }


	 */



}
