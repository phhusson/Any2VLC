package me.phh.AndroidToVlc

import org.scaloid.common._
import android.graphics.Color
import android.content.{Context,Intent}
import java.net.{HttpURLConnection,URL,URLEncoder}
import scala.language.implicitConversions
import scalaj.http.Http
import scala.util.parsing.json.JSON

class ShareService extends LocalService {
	implicit val tag = new LoggerTag("Android2VLC");
	onCreate {
		warn("Hellllo");
	};

	class Video(videoUri: String) {
		val scriptUri = "http://youtube-dl.appspot.com/api/?url="+URLEncoder.encode(videoUri, "UTF-8");
		val jsonText = scala.io.Source.fromURL(scriptUri).mkString
		val jsonTree = JSON.parseFull(jsonText);
		var url: String = "";
		var duration: String = "";
		var title: String = "";
		jsonTree match {
			case Some(tree: Map[String,Any]) => tree("videos") match {
				case List(obj, _*) => obj match {
					case video: Map[String, Any] => {
						url = video("url").toString;
						duration = video("duration").toString;
						title = video("title").toString;
					}
				}
			}
		}
	}

	def open(uri : String) {
		val pref = Preferences();

		toast("Retrieving file url");
		warn("Retrieving file url");
		val v = new Video(uri);

		toast("Sending to VLC");
		warn("Sending to VLC");
		var base = "http://"+pref.server("")+":"+pref.port("8080");
		if(!pref.script("/requests/status.xml").startsWith("/"))
			base+="/";
		base+=pref.script("/requests/status.xml");

		var status = Http.get(base).
						auth("", pref.password("azertyuiop")).asXml
		var stopped = (status \ "state").exists (_.text == "stopped")

		base += (if(stopped) "?command=in_play&" else "?command=in_enqueue&");
		base += "name="+URLEncoder.encode(v.title, "UTF-8")+"&";
		base += "input="+URLEncoder.encode(v.url)+"&";
		base += "duration="+v.duration+"&";

		Http.get(base).auth("", pref.password("azertyuiop")).responseCode

		toast("Sent to VLC");
		warn("Sent to VLC");
	}
}
