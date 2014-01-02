package me.phh.AndroidToVlc

import org.scaloid.common._
import scala.reflect.ClassTag
import android.graphics.Color
import android.content.{Context,Intent,IntentFilter,BroadcastReceiver}

class Share extends SActivity {
	val serv = new LocalServiceConnection[ShareService] ()(this, onCreateDestroy, implicitly[ClassTag[ShareService]]);

	onCreate {
		var i : Intent = getIntent();
		var action : String = i.getAction();
		var uri : String = i.getStringExtra(Intent.EXTRA_TEXT);

		serv.onConnected += {
			s : ShareService => s.open(uri);
			finish();
		}
	};

}