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

	def toVlc(uri: String, password: String): Option[scala.xml.Elem]= {
		def wrong(s: String) = {
			startActivity(
					SIntent[Settings].
					addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
					putExtra("wrong", s))
		}
		try {
			val res = Http.get(uri).
				auth("", password).asXml
			if( (res \ "body" size) > 0) {
				//Got an HTML page...
				//This means password hasn't been set.
				//We can't get status, but we can add files
				warn("Got an HTML page...");
				warn("VLC_PASSWORD_NOT_SET ?");

				//TODO: Handle this case
				//wrong("password_not_set");
			}
			return Some(res)
		} catch {
			case e: java.net.UnknownHostException => {
						warn("Host not found...")
						wrong("host");
					}
			case e: java.net.SocketTimeoutException => {
						warn("Host timed out...")
						wrong("host");
					}
			case e: java.net.ConnectException => {
						//VLC not put in server mode or inaccessible
						warn("Couldn't connect...")
						wrong("server");
						warn(e.toString)
					}
			case e: scalaj.http.HttpException => {
						e.code match {
							case 401 => wrong("password");
							case _ => {
								wrong("proxy");
								warn("proxy");
							}
						}
					}
			case e: Exception => {
						wrong("unknown");
						warn("Couldn't request...")
						warn(e.toString)
					}
		}
		return None
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

		var status = toVlc(base, pref.password)
		var stopped = status match {
			case Some(s) => (s \ "state").exists (_.text == "stopped")
			case None => false
		};

		base += (if(stopped) "?command=in_play&" else "?command=in_enqueue&")
		base += "name="+URLEncoder.encode(v.title, "UTF-8")+"&"
		base += "title="+URLEncoder.encode(v.title, "UTF-8")+"&"
		base += "input="+URLEncoder.encode(v.url)+"&"
		base += "duration="+v.duration+"&"

		toVlc(base, pref.password) match {
			case Some(_) => {
				toast("Sent to VLC")
				warn("Sent to VLC")
			}
			case None => toast("Send to VLC failed");
		}
	}
}
