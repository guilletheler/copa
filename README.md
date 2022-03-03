# COPA - Costeo por Actividad

### Desarrollado por Guillermo Theler y Ariel (Pato) Tacón
### Fuentes en [Github](https://github.com/guilletheler/copa)
<br>
Copa está desarrollado en el lenguaje Java, utilizando el framework [Spring Boot](https://spring.io/projects/spring-boot) y [JavaFX](https://openjfx.io/).

### Instrucciones para compilar:

* Instalar Java JDK 11 o superior, preferentemente [Eclipse Temurin](https://adoptium.net/). Recordar agregar las variables de entorno de Java para que funcione correctamente.
<br><br>
* Instalar cliente git
  * En Linux: sudo apt-get install git
  * En Windows: [Git for Windows](https://sourceforge.net/projects/git-for-windows.mirror/). Recordar agregar las variables de entorno de Git para que funcione correctamente.
<br><br>
* Instalar [Maven](https://maven.apache.org)
  * En linux: sudo apt-get install maven
  * En windows: Seguir las [instrucciones](https://maven.apache.org/guides/getting-started/windows-prerequisites.html). Recordar agregar las variables de entorno de Maven para que funcione correctamente.
<br><br>
* Clonar los repositorios de github [COPA](https://github.com/guilletheler/copa) y [ToolboxFX](https://github.com/guilletheler/toolboxfx) y compilar ambos con MAVEN
 
    `git clone https://github.com/guilletheler/toolboxfx.git`

    `git clone https://github.com/guilletheler/copa.git`
<br><br>
* Compilar:
 
    `cd toolboxfx`

    `mvn clean install`

    `cd ..`

    `cd copa`
    
    `mvn clean install`

    `cd ..`
<br><br>
* Ejecutar:
  
    `java -jar copa/target/copa-1.0-SNAPSHOT-jar-with-dependencies.jar`