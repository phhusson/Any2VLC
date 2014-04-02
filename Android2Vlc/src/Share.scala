package me.phh.Android2Vlc

import org.scaloid.common._
import scala.reflect.ClassTag
import android.graphics.Color
import android.content.{Context,Intent,IntentFilter,BroadcastReceiver}
import scala.util.matching.Regex
import scala.concurrent.ops._

class Share extends SActivity {
	val serv = new LocalServiceConnection[ShareService]

	onCreate {
		var i : Intent = getIntent()
		var action : String = i.getAction()
		var uri : String = i.getStringExtra(Intent.EXTRA_TEXT)

        var re = "http[^ ]*$".r
        uri = (re findAllIn uri).mkString(" ")

        contentView = new SVerticalLayout {
          STextView("Loading...")
        }

		serv.onConnected += {
			s : ShareService => spawn { s.open(uri) }
			finish()
		}
	}

}
