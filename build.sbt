name := "mapred-zipinput-example-scala"

version := "0.1"

scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.3",
  "org.apache.hadoop" % "hadoop-common" % "2.7.3",
  "org.apache.mrunit" % "mrunit" % "1.1.0" % "test" classifier "hadoop2",
  "junit" % "junit" % "4.12"  % "test",
  "org.hamcrest" % "hamcrest-all" %  "1.3" % "test"
)

unmanagedJars in Compile += file("com-cotdp-hadoop-1.0-SNAPSHOT.jar")

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
assemblyJarName in assembly := "mapred-zipinput-example-scala-uber.jar"
