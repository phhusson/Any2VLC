package me.phh.Android2Vlc

import org.scaloid.common._
import android.graphics.Color

class Settings extends SActivity {
	implicit val tag = new LoggerTag("Android2Vlc");

	onCreate {
		val wrong = getIntent().getStringExtra("wrong");
		warn(wrong);

		val pref = Prefs()

		contentView = new SScrollView {
            this += new SVerticalLayout {
                STextView("VLC Server:")
                if(wrong == "host" || wrong == "server")
                    STextView("Seems wrong").textColor(Color.RED);
                val server = SEditText(pref.server) inputType TEXT_URI

                STextView("VLC Port (if unsure, leave 8080):")
                if(wrong == "server")
                    STextView("Seems wrong").textColor(Color.RED);
                val port = SEditText(pref.port)

                STextView("VLC proxy script (if unsure, leave default)")
                if(wrong == "proxy")
                    STextView("Seems wrong").textColor(Color.RED);
                val proxy = SEditText(pref.proxy)

                STextView("VLC Password:")
                if(wrong == "password")
                    STextView("Seems wrong").textColor(Color.RED);
                val password = SEditText(pref.password)

                STextView("youtube-dl API base (if unsure, leave default)")
                val dlapi = SEditText(pref.dlapi)

                SButton("Enregistrer").onClick({
                    pref.server = server.text.toString
                    pref.port = port.text.toString
                    pref.proxy = proxy.text.toString
                    pref.password = password.text.toString
                    pref.dlapi = dlapi.text.toString
                    ()
                })

                if(wrong != null)
                    SButton("How to setup VLC ?");
            } padding 20.dip
        }
	}
}
