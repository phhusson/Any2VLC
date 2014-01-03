package me.phh.Android2Vlc

import org.scaloid.common._
import android.graphics.Color
import android.content.{Context,Intent}
import java.net.{HttpURLConnection,URL,URLEncoder}
import scala.language.implicitConversions
import scalaj.http.Http
import scala.util.parsing.json.JSON

class ShareService extends LocalService {
	implicit val tag = new LoggerTag("Android2Vlc");

	class Video(videoUri: String, baseUri: String) {
		val scriptUri = baseUri + URLEncoder.encode(videoUri, "UTF-8")
		val jsonText = scala.io.Source.fromURL(scriptUri).mkString
		val jsonTree = JSON.parseFull(jsonText);
		var url: String = ""
		var duration: String = ""
		var title: String = ""
		jsonTree match {
			case Some(tree: Map[String,Any]) => tree("videos") match {
				case List(obj, _*) => obj match {
					case video: Map[String, Any] => {
						url = video("url").toString
						duration = video("duration").toString
						title = video("title").toString
					}
				}
			}
		}
	}

	def open(uri : String) {
		val pref = Prefs()

		toast("Retrieving file url")
		warn("Retrieving file url")
		val v = new Video(uri, pref.dlapi)

		toast("Sending to VLC")
		warn("Sending to VLC")
		var base = "http://"+pref.server+":"+pref.port
		if(!pref.proxy.startsWith("/"))
			base+="/"
		base+=pref.proxy

		var status =  try {
				Http.get(base).
					auth("", pref.password).asXml
			} catch {
				case e: java.net.UnknownHostException => {
					startActivity(
						SIntent[Settings].
							addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
							putExtra("wrong", "server"))
					warn("Host not found...")
					return
				}
				case e: java.net.ConnectException => {
					//VLC not put in server mode or inaccessible
					warn("Couldn't connect...")
					startActivity(
						SIntent[Settings].
							addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
							putExtra("wrong", "server"))
					warn(e.toString)
					return
				}
				case e: Exception => {
					startActivity(
						SIntent[Settings].
							addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
							putExtra("wrong", "unknown"))
					warn("Couldn't request...")
					warn(e.toString)
					return
				}
			}
		var stopped = (status \ "state").exists (_.text == "stopped")

		base += (if(stopped) "?command=in_play&" else "?command=in_enqueue&")
		base += "name="+URLEncoder.encode(v.title, "UTF-8")+"&"
		base += "input="+URLEncoder.encode(v.url)+"&"
		base += "duration="+v.duration+"&"

		Http.get(base).auth("", pref.password).responseCode

		toast("Sent to VLC")
		warn("Sent to VLC")
	}
}
