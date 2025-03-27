# GameKeeper

## Descripción

GameKeeper es una aplicación móvil desarrollada para la gestión de colecciones personales de juegos de mesa. Permite a los usuarios registrarse, añadir, editar y buscar juegos de mesa. Además, la aplicación facilita el registro de los jugadores que han participado en cada partida y ofrece sugerencias de juegos basadas en la colección del usuario. Está diseñada para entusiastas de los juegos de mesa que desean organizar y gestionar sus juegos y partidas de forma eficiente.

## Objetivos

**Objetivo General:**

Ayudar a las personas con una extensa colección de juegos de mesa a gestionar y organizar sus juegos y partidas jugadas de manera sencilla y eficiente.

**Objetivos Específicos:**

* Permitir a los usuarios registrar y autenticar su perfil.
* Facilitar la adición, edición y gestión de juegos de mesa en la colección personal.
* Proporcionar una herramienta para registrar y gestionar los juegos jugados por diferentes jugadores.
* Permitir la eliminación de juegos de la colección personal.
* Facilitar la adición de jugadores y la gestión de quién ha jugado a cada juego.
* Ofrecer sugerencias de juegos basadas en la colección y preferencias del usuario.

## Características Principales

* **Registro y Autenticación de Usuarios:** Los usuarios pueden crear una cuenta y acceder a la aplicación de forma segura.
* **Gestión de Colección de Juegos:**
    * **Añadir juegos:** Los usuarios pueden agregar nuevos juegos a su colección personal.
    * **Eliminar juegos:** Posibilidad de eliminar juegos de la colección.
    * **Editar juegos:** Modificación de la información de los juegos existentes en la base de datos.
    * **Añadir Nuevos Juegos a la Base de Datos:** Los usuarios pueden agregar nuevos juegos a la base de datos general.
    * **Ver detalles de los juegos:** Visualización de información detallada sobre cada juego en la colección.
* **Gestión de Jugadores:**
    * **Creación de jugadores:** Los usuarios pueden crear perfiles para los jugadores.
    * **Registro de juegos jugados:** Los usuarios pueden registrar quién ha jugado a cada juego.
* **Sugerencias de Juegos:** La aplicación sugiere juegos basándose en diferentes criterios:
    * **Más Jugado:** Muestra el juego más jugado.
    * **Menos Jugado:** Muestra el juego menos jugado.
    * **Aleatorio:** Muestra un juego seleccionado al azar.

## Tecnologías Utilizadas

* **Desarrollo:** Android Studio
* **Lenguaje de Programación:** Java
* **Base de Datos:** SQLite (local en el dispositivo Android)
* **Almacenamiento de Sesión y Configuración:** SharedPreferences
* **Gestión de Imágenes:** Glide
* **Diseño de Interfaz:** Uso de componentes Material Design como Chip, SearchView, RecyclerView y BottomNavigationView.
* **Seguridad:** Cifrado de contraseñas con Encrypt.

## Instalación

La aplicación es una aplicación móvil para Android. Para instalarla, siga estos pasos:

1.  Transfiera el archivo APK al dispositivo móvil utilizando un cable USB.
2.  Habilite la opción de "Orígenes desconocidos" en la configuración de seguridad de su dispositivo (si aún no está habilitada).
3.  Busque el archivo APK en su dispositivo y tóquelo para iniciar la instalación.
4.  Siga las instrucciones en pantalla para completar la instalación.

## Manual de Usuario

1.  **Registro de Nuevo Usuario:** En la pantalla principal, seleccione la opción para registrarse. Complete el formulario con su correo electrónico y contraseña.
2.  **Iniciar Sesión:** En la pantalla principal, ingrese su correo electrónico y contraseña para acceder a la aplicación.
3.  **Ver Colección de Juegos:** Una vez iniciada la sesión, podrá ver la lista de juegos de mesa que ha añadido a su colección.
4.  **Añadir Juego a la Colección:** Utilice la función de búsqueda para encontrar juegos y añadirlos a su colección.
5.  **Ver Detalles de Juegos:** Seleccione un juego de su colección para ver información detallada.
6.  **Eliminar Juego de la Colección:** Seleccione un juego y elija la opción para eliminarlo de su colección.
7.  **Filtrar Juegos por Género:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
8.  **Añadir Nuevo Juego a la Base de Datos:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
9.  **Registrar Jugadores:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
10. **Acceder al Perfil de Juegos Jugados:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
11. **Seleccionar Juegos Disponibles:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
12. **Guardar y Actualizar Selecciones de Juegos:** (Esta funcionalidad está mencionada en los requisitos funcionales pero no detallada en el manual de usuario proporcionado).
13. **Mostrar Sugerencias de Juegos:** Acceda a la sección de sugerencias para ver juegos recomendados basados en diferentes criterios (Más Jugado, Menos Jugado, Aleatorio).

## Conclusiones y Posibles Ampliaciones

La aplicación GameKeeper cumple con los objetivos establecidos para la gestión de colecciones de juegos de mesa. Sin embargo, se podrían implementar las siguientes mejoras a futuro:

* Poder añadir fotos de los juegos desde la galería y la cámara del dispositivo.
* Implementar filtros de búsqueda en la colección por jugadores, tiempo de juego y número de jugadores.
* Integrar la autenticación de cuenta con Google.
* Utilizar una API de juegos de mesa para obtener información más detallada y actualizada.
* Establecer conexión entre usuarios registrados para compartir colecciones o estadísticas.

## Bibliografía

* Chips: [https://material.io/components/chips](https://material.io/components/chips)
* SearchView: [https://developer.android.com/reference/android/widget/SearchView](https://developer.android.com/reference/android/widget/SearchView)
* RecyclerView: [https://developer.android.com/guide/topics/ui/layout/recyclerview](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* BottomNavigationView: [https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)
* Glide: [https://bumptech.github.io/glide/](https://bumptech.github.io/glide/)
* SQLite: [https://www.sqlite.org/docs.html](https://www.sqlite.org/docs.html)

## Documentación Adicional
Para más información detallada sobre el proyecto, puedes consultar los siguientes documentos:  
[Memoria](https://github.com/nestor115/gameKeeper/blob/main/DeFrutos_Alonso_Nestor_Memoria_ProyectoFinal_DAM24.pdf) | [Manual](https://github.com/nestor115/gameKeeper/blob/main/DeFrutos_Alonso_Nestor_Manual_ProyectoFinal_DAM24.pdf)

