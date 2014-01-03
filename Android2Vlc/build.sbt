import android.Keys._

android.Plugin.androidBuild

name := "Android2Vlc"

scalaVersion := "2.10.3"

proguardOptions in Android ++= Seq("-dontobfuscate", "-dontoptimize")

libraryDependencies += "org.scaloid" %% "scaloid" % "3.0-8"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "0.3.12"

scalacOptions in Compile += "-feature"

run <<= run in Android

install <<= install in Android
