package com.terje.chesstacticstrainer_full;

import org.andengine.entity.sprite.Sprite;

public interface DragCallBack_I {

	public void onDragStart(Sprite s);
	
	public boolean onDragEnd(Move m);
}
