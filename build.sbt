enablePlugins(GatlingPlugin)

scalaVersion := "2.11.5"

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.7", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2"           % "test"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.2.2"           % "test"
libraryDependencies += "org.postgresql"        % "postgresql"                % "9.4-1201-jdbc4"  % "test"
libraryDependencies += "io.gatling"            % "gatling-jdbc"              % "2.2.2"           % "test"
libraryDependencies += "net.debasishg"        %% "redisclient"              % "3.0"             % "test"
libraryDependencies += "joda-time" % "joda-time" % "2.8.1"  % "test"




javaOptions in Gatling := overrideDefaultJavaOptions("-Xms1024m", "-Xmx2048m")
