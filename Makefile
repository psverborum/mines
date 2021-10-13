create-jar-linux:
	mvn clean compile assembly:single -P lwjgl-natives-linux-amd64
	mv ./target/*.jar ./jars/mines-linux.jar

create-jar-macos:
	mvn clean compile assembly:single -P lwjgl-natives-macos-amd64
	mv ./target/*.jar ./jars/mines-macos.jar

create-jar-windows:
	mvn clean compile assembly:single -P lwjgl-natives-windows-amd64
	mv ./target/*.jar ./jars/mines-windows.jar

create-jar-all:
	rm -rf /jars
	mkdir jars
	make create-jar-linux
	make create-jar-macos
	make create-jar-windows