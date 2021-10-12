create-jar-linux:
	mvn clean compile assembly:single -P lwjgl-natives-linux-amd64

create-jar-macos:
	mvn clean compile assembly:single -P lwjgl-natives-macos-amd64

create-jar-windows:
	mvn clean compile assembly:single -P lwjgl-natives-windows-amd64