package me.phh.Android2Vlc

import org.scaloid.common._
import android.graphics.Color

class Settings extends SActivity {
  onCreate {
    val pref = Prefs();

    contentView = new SVerticalLayout {
      STextView("VLC Server:")
      val server = SEditText(pref.server);

      STextView("VLC Port (if unsure, leave 8080):")
      val port = SEditText(pref.port);

	  STextView("VLC proxy script (if unsure, leave default)");
	  val proxy = SEditText(pref.proxy);

      STextView("VLC Password:")
      val password = SEditText(pref.password);

      SButton("Enregistrer").onClick({
        pref.server = server.text.toString();
        pref.port = port.text.toString();
        pref.proxy = proxy.text.toString();
        pref.password = password.text.toString();
      });
    } padding 20.dip
  }

}
