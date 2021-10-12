create-jar-linux:
	mvn clean compile assembly:single -P lwjgl-natives-linux-amd64
	mv ./target/*.jar ./target/mines-linux.jar

create-jar-macos:
	mvn clean compile assembly:single -P lwjgl-natives-macos-amd64
	mv ./target/*.jar ./target/mines-macos.jar

create-jar-windows:
	mvn clean compile assembly:single -P lwjgl-natives-windows-amd64
	mv ./target/*.jar ./target/mines-windows.jar