package com.terje.chesstacticstrainer_full;

public interface TempMeter_I {

	void init();

	void showComputerThinking();

	void advance(int i);

	void greenSequence();

	void onfail();

	void score();

	int getBonusFactor();

	void showTemp();

	void registerWin();

	void hide();

}
