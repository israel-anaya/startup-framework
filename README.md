# startup-framework
Arquitectura de buenas practicas y estandares para el desarrollo de microservicios basados en SPRING® Framework.  
http://startupframework.org/

Spring is a trademark of Pivotal Software, Inc. in the U.S. and other countries.

## Taxonomia de Servicios

| Tipo | Descripción | Nomeclatura
| :--- | :--- | :--- |
| Entidad | Manipulas estado de datos | ms-et
| Legacy | Encapsula un sistema | ms-lg
| Tarea | Servicio de alto nivel que orquesta operaciones entre varios servicios para proveer un API | ms-ts
| MicroTarea | Orquesta operaciones entre varios servicios sin llegar a ser un API | ms-mts

## Arquitectura de Microservicio de Entidad

| Elemento | Tipo | Descripción | 
| :--- | :--- | :--- |
| Controller | Capa de Interfaz | Interfaz de operaciones expuestas |
| DataSource | Capa de Compatibilidad | Componente que mapea y convierte las entidades con los DTOs y viceversa |
| Service | Capa Lógica | Componente que mantiene la integridad y validez de los datos. Reglas de datos y negocio |
| Repository | Capa de Datos | Capa esclusiva para datos |
| Entity | Modelo | Es la estrucutra del modelo a nivel de base de datos |
| DTO | Modelo | Es la estructura del modelo a nivel servicio |


## Arquitectura de Microservicio de Tarea

| Elemento | Tipo | Descripción | 
| :--- | :--- | :--- |
| Controller | Capa de Interfaz | Interfaz de operaciones expuestas |
| Enproceso... |  |  |

## Estructura de código de un Microservicio

#### packagename = {Organization}.{serviceType}.{module}
Ejemplo com.google.ms.et.customer

| Package | Descripción | 
| :--- | :--- | 
| / | Clase principal del microservicio |
| controller | Controladores |
| datasource | Fuentes de conversión y mapeo entre Entity -> DTO y DTO -> Entity |
| entity | Modelo de la entidad de datos |
| dto | Modelo del contratos del servicio |
| service | Interfaces de servicios |
| service.impl | Implementaciones de los servicios |

## Arquitectura de url, verbos y códigos de respuesta HTTP de un Microservicio 

#### Main URL: /{version}/module/
#### Child URL: /{version}/module/{parentId}/childModule

| Verbo | Descripción | URL
| :--- |  :--- | :--- |
| GET | Lista todos los items | /
| GET | Obtiene el item especificado por el id | /{id}
| POST | Crea un nuevo item  | /
| PUT | Actualiza el item especificado por el id | /
| DELETE | No permitido a menos que sea un borrado lógico | /

| Repuesta HTTP | Descripción | Exception
| :--- |  :--- | :--- |
| 200 | Operación sin problema (aplica para GET, PUT | N/A
| 201 | Item creado (aplica para POST) | N/A
| 204 | No hay contenido o lista vacia (Aplica para GET) | N/A
| 500 | Error general o posible error de programación  | Exception
| 409 | Error de conflicto, posible mal uso del framework | StartupException
| 400 | Llamada inadecuada en la petición (Validación de datos requeridos o formato)  | DataException
| 400 | Duplicidad de información | DuplicateDataException
| 404 | No se encontraron datos con el criterio de la petición | DataNotFoundException

## Arquitectura de datos para todas la entidades

| Field | Type | Description
| :--- |  :--- | :--- |
| id | uuid | Estratégia de identificador unico para compartir este entre diferentes bases de datos |
| createdDate | Date | Fecha de creación del elemento |
| modifiedDate | Date | Fecha de ultima modificación |
| active | boolean | El borrado físico no debe permitirse jamas, en todo caso los elementos se desactivan o deshabilitan |

La validación del modelo se hace con notaciones "javax.validation.constraints" que estan automatizadas en la capa de service

## Artefactos:

| Artefactos | Descripción | Dependecia Maven |
| :--- | :--- | :--- |
| startup-starter-parent | POM parent que agrupa las librerias spring  | [![Maven Central](https://img.shields.io/maven-central/v/org.startupframework/startup-starter-parent.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.startupframework%22%20AND%20a:%22startup-starter-parent%22) |
| startup-starter-ms-parent | POM parent para los microservicios | [![Maven Central](https://img.shields.io/maven-central/v/org.startupframework/startup-starter-ms-parent.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.startupframework%22%20AND%20a:%22startup-starter-ms-parent%22) |
| startup-starter-core | Componentes para crear microservicios sin enlace a fuentes de datos | [![Maven Central](https://img.shields.io/maven-central/v/org.startupframework/startup-starter-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.startupframework%22%20AND%20a:%22startup-starter-core%22) |
| startup-starter-data | Componentes para crear microservicios con enlace a fuentes de datos | [![Maven Central](https://img.shields.io/maven-central/v/org.startupframework/startup-starter-data.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.startupframework%22%20AND%20a:%22startup-starter-data%22) |

## Versión Actual de Spring

Spring Cloud Hoxton.SR5  
Spring Boot 2.3.0

## Licensed

Copyright 2019-2020 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


