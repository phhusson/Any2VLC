package me.phh.Android2Vlc

import org.scaloid.common._
import android.content.{SharedPreferences,Context}

class Prefs(preferences : SharedPreferences) extends Preferences(preferences) {
	def server = super.server("192.168.1.1");
	def server_=(s: String):Unit = super.server = s

	def port = super.port("8080");
	def port_=(s: String):Unit = super.port = s

	def proxy = super.proxy("/requests/status.xml");
	def proxy_=(s: String):Unit = super.proxy = s

	def password = super.password("azertyuiop");
	def password_=(s: String):Unit = super.password = s

	def dlapi = super.dlapi("http://youtube-dl.appspot.com/api/?url=");
	def dlapi_=(s: String):Unit = super.dlapi = s
}

object Prefs {
	def apply()(implicit ctx: Context) = new Prefs(defaultSharedPreferences);
}
