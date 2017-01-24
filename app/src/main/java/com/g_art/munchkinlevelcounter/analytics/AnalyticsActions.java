package com.g_art.munchkinlevelcounter.analytics;

/**
 * Created by agulia on 1/24/17.
 */

public enum AnalyticsActions {
	/**
	 * Monster
	 **/
	Monster_LVL_UP,
	Monster_LVL_DWN,
	Monster_MODS_UP,
	Monster_MODS_DWN,
	Monster_TR_UP,
	Monster_TR_DWN,

	/**
	 * Player
	 **/
	Player_LVL_UP,
	Player_LVL_DWN,
	Player_GEARS_UP,
	Player_GEARS_DWN,
	Player_MODS_UP,
	Player_MODS_DWN,

	Help_Request,
	Dice_Rolled,

	/**
	 * Battle
	 **/
	Battle_Finished,
	Battle_Started,
	Battle_Cancel,
	Run_Away,

	/**
	 * Game
	 */
	Game_Starting,
	Win_Game,
	Cancel_Game,
	Finish_Game,

	/**
	 * Players
	 */
	Remove_Players,
	Drag_Player,
	Add_Player,

	/**
	 * Utility
	 **/
	ILLEGAL_ACTION,
	Open,
	Rate,
	Contact,
	Error,
	Translation_Help,
	Translation_No,
	Translation_Not_Now, Donate,

}
