/******************************************************************************************
 * DisabledPreferencesFactory.java  PokerApp                                              * 
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 12/18/04 | Initial documented release                                    | *
 * |  1.00   | 07/05/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 * PokerApp Copyright (C) 2004  Dan Puperi                                                *
 *                                                                                        *
 *   This program is free software: you can redistribute it and/or modify                 *
 *   it under the terms of the GNU General Public License as published by                 *
 *   the Free Software Foundation, either version 3 of the License, or                    *
 *   (at your option) any later version.                                                  *
 *                                                                                        *
 *   This program is distributed in the hope that it will be useful,                      *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of                       *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                        *
 *   GNU General Public License for more details.                                         *
 *                                                                                        *
 *   You should have received a copy of the GNU General Public License                    *
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>                 *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp;

import java.util.prefs.PreferencesFactory;
import java.util.prefs.Preferences;

/****************************************************
 * Class to fix java.util.prefs warnings that come up on some Unix or Linux
 * systems. This class extends the PreferencesFactory class and basically stubs
 * out all the functions that are required to be implemented.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class NoPreferencesFactory implements PreferencesFactory {

	/***************************
	 * systemRoot() overrides the PreferencesFactory class to return
	 * NoPreferences.
	 **/
	public Preferences systemRoot() {
		return new NoPreferences();
	}

	/***************************
	 * userRoot() overrides the PreferencesFactory class to return
	 * NoPreferences.
	 **/
	public Preferences userRoot() {
		return new NoPreferences();
	}
}