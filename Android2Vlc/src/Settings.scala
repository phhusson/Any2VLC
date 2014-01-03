package me.phh.Android2Vlc

import org.scaloid.common._
import android.graphics.Color

class Settings extends SActivity {
  onCreate {
    val pref = Preferences();

    contentView = new SVerticalLayout {
      STextView("VLC Server:")
      val server = SEditText(pref.server("192.168.1.1"));

      STextView("VLC Port (if unsure, leave 8080):")
      val port = SEditText(pref.port("8080"));

	  STextView("VLC proxy script (if unsure, leave default)");
	  val proxy = SEditText(pref.proxy("/requests/status.xml"));

      STextView("Password:")
      val password = SEditText(pref.password("azertyuiop"));

      SButton("Enregistrer").onClick({
        pref.server = server.text.toString();
        pref.port = port.text.toString();
        pref.proxy = proxy.text.toString();
        pref.password = password.text.toString();
      });
    } padding 20.dip
  }

}
