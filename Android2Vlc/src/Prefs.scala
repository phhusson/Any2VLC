package me.phh.Android2Vlc

import org.scaloid.common._
import android.content.{SharedPreferences,Context}

class Prefs(preferences : SharedPreferences) extends Preferences(preferences) {
	def server = super.server("192.168.1.1");
	def port = super.port("8080");
	def proxy = super.port("/requests/status.xml");
	def password = super.password("azertyuiop");
	def dlapi = super.password("http://youtube-dl.appspot.com/api/?url=");
}

object Prefs {
	def apply()(implicit ctx: Context) = new Prefs(defaultSharedPreferences);
}
