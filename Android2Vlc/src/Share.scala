package me.phh.Android2Vlc

import org.scaloid.common._
import scala.reflect.ClassTag
import android.graphics.Color
import android.content.{Context,Intent,IntentFilter,BroadcastReceiver}
import scala.util.matching.Regex

class Share extends SActivity {
	val serv = new LocalServiceConnection[ShareService]

	onCreate {
		var i : Intent = getIntent()
		var action : String = i.getAction()
		var uri : String = i.getStringExtra(Intent.EXTRA_TEXT)

        var re = "http[^ ]*$".r
        uri = (re findAllIn uri).mkString(" ")

		serv.onConnected += {
			s : ShareService => s.open(uri)
			finish()
		}
	}

}
