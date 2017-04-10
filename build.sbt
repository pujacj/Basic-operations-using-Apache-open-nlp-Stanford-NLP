name := "EntityFinder"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq("org.apache.opennlp" % "opennlp-tools" % "1.7.2",
                        "edu.stanford.nlp" % "stanford-corenlp" % "3.7.0",
                         "org.slf4j" % "slf4j-api" % "1.6.1",
                          "org.slf4j" % "slf4j-simple" % "1.6.1")
