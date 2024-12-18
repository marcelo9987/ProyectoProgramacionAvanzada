# TrainGo

![banner](TrainGo/src/main/resources/images/tren.png)

[![license](https://img.shields.io/github/license/marcelo9987/ProyectoProgramacionAvanzada.svg)](LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

Aplicación para quioscos de venta de billetes para
trenes de cercanías. <br/>
También se ha implementado una pequeña API para gestionar la E/S de la aplicación basada en XML.

## Índice

- [Autor](#Autor)
- [Fecha](#Fecha)
- [Fuentes](#Fuentes)
- [Instalación](#instalación-y-puesta-en-marcha)
- [Uso](#Uso)
- [Dependencias](#Dependencias)
- [Licencia](#licencia)

## Autor

Marcelo F.M.    <br/>
Ver en github:    [marcelo9987](https://github.com/marcelo9987)

## Fecha

Diciembre del 2024

## Fuentes

- [stackoverflow](https://stackoverflow.com/)
- [w3schools](https://www.w3schools.com/)
- [Java](https://docs.oracle.com/en/java/)
- [XML](https://www.w3schools.com/xml/)
- [Maven](https://maven.apache.org/)
- [Baeldung](https://www.baeldung.com/)

## Instalación y puesta en marcha

Para obtener el código fuente, clonar el repositorio en tu máquina local. <br/>
Este proyecto se ha desarrollado con Java 22 y Maven. <br/>
Si usa un IDE como Eclipse o IntelliJ, importe el proyecto como un proyecto Maven. <br/>
Para ejecutar la aplicación, ejecute el método main de fachada aplicación. <br/>
Para crear un autoejecutable, ejecute el comando `mvn -f TrainGo/pom.xml clean compile package` y ejecute el jar generado en una terminal. <br/>

Nota: Para que el programa pueda funcionar, necesitas tener los xml de datos en la misma carpeta que el jar. Si no los tienes, puedes usar los que están proporcionados en este repositorio. <br/>

## Uso

Para ejecutar la aplicación, ejecute el método main de la clase `FachadaAplicacion`. <br/>
Alternativamente, puede ejecutar el jar disponible en la sección de lanzamientos o que hayas generado con Maven. <br/>
Para hacerlo, ejecute el siguiente comando en una terminal (asegúrese de estar en la carpeta donde se encuentra el jar): <br/>

Con  salida por consola:
```
java -jar trainGo.jar
```

Como proceso en segundo plano:
```
javaw -jar trainGo.jar
```

## Dependencias

TrainGo utiliza las siguientes dependencias:

- ch.qos.logback:logback-core (1.5.6)
- ch.qos.logback:logback-classic (1.5.6)
- org.jetbrains:annotations (13.0)
- org.hibernate.validator:hibernate-validator (8.0.1.Final)

## Licencia

[Licencia Pública General Reducida de GNU](LICENSE)