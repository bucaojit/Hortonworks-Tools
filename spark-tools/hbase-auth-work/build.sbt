name := "scalaTest"
version := "1.0"
scalaVersion in ThisBuild := "2.10.5"
dependencyOverrides += "org.scala-lang" % "scala-library" % scalaVersion.value
val hadoopVersion = "2.7.1.2.4.2.6-1"
val hbaseVersion = "1.1.2.2.4.2.6-1"
val sparkVersion = "1.6.1.2.4.2.6-1"
val kafkaVersion = "0.9.0.2.4.2.0-258"

resolvers += "Hortonworks Repository" at "http://repo.hortonworks.com/content/repositories/releases/"
resolvers += "Hortonworks Jetty Maven Repository" at "http://repo.hortonworks.com/content/repositories/jetty-hadoop/"

val hadoopCommon = "org.apache.hadoop" % "hadoop-common" % hadoopVersion % "provided"
val hadoopHDFS = "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion % "provided"
val hbaseCommon = "org.apache.hbase" % "hbase-common" % hbaseVersion 
val hbaseClient = "org.apache.hbase" % "hbase-client" % hbaseVersion 
val hbaseProtocol = "org.apache.hbase" % "hbase-protocol" % hbaseVersion 
val hbaseHadoop2Compat = "org.apache.hbase" % "hbase-hadoop2-compat" % hbaseVersion 
val hbaseServer = "org.apache.hbase" % "hbase-server" % hbaseVersion 
val hbasePOM = "org.apache.hbase" % "hbase" % hbaseVersion pomOnly()
val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
val sparkHive = "org.apache.spark" %% "spark-hive" % sparkVersion % "provided"
val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaVersion

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.4.0"
libraryDependencies += "net.liftweb" %% "lift-json" % "2.6.3"
libraryDependencies += hbasePOM
libraryDependencies += hadoopCommon
libraryDependencies += hadoopHDFS
libraryDependencies += hbaseCommon
libraryDependencies += hbaseClient
libraryDependencies += hbaseProtocol
libraryDependencies += hbaseHadoop2Compat
libraryDependencies += hbaseServer
libraryDependencies += sparkCore
libraryDependencies += sparkHive
libraryDependencies += kafkaClient
