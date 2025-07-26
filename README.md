# 🗂️ Planitarea

**Planitarea** es una aplicación desarrollada en **Java** para la gestión de tareas, horarios y sesiones Pomodoro. Está organizada con buenas prácticas y pensada para facilitar la productividad de los estudiantes.

---

## 📌 Características

- 📝 Gestión de tareas con duración
- 📅 Organización de horarios diarios y semanales
- ⏲️ Temporizador Pomodoro para sesiones de estudio
- 📂 Almacenamiento de datos en formato `.json`
- 💡 Estructura de proyecto modular con POO

---

## 🛠️ Tecnologías usadas

- Java 8 o superior
- Archivos `.json` para persistencia
- Organización con `src/main/java` y `data/`

---

## 📁 Estructura del proyecto

```
Planitarea/
├── data/
│ ├── horarios.json
│ └── tareas.json
├── src/
│ ├── examples/
│ │ ├── EjemploCalendarSet.java
│ │ └── EjemploSplit.java
│ └── main/
│ └── java/
│ ├── Horario.java
│ ├── Horario_Diario.java
│ ├── Horario_Semanal.java
│ ├── Main.java
│ ├── Planitarea.java
│ ├── Tarea.java
│ └── Tiempo_Tarea.java
├── resources/
├── .gitignore
├── LICENSE
└── README.md
```

---

## 🚀 Cómo correr el proyecto en local

### Requisitos:

- Tener **Java JDK 8 o superior** instalado.
- Tener un IDE como **IntelliJ IDEA**, **NetBeans** o **Eclipse**.

### Pasos:

1. Clona este repositorio:
   ```bash
   git clone https://github.com/Juardilap/Planitarea.git
   ```
2. Abre el proyecto en tu IDE favorito.
3. Asegúrate de que las dependencias de Java estén correctamente configuradas.
4. Ejecuta la clase principal `Main.java`.

---

## 📚 Cómo usar la aplicación

1. Al iniciar, se te presentará una interfaz donde podrás:
   - Añadir, eliminar y editar tareas.
   - Organizar tus tareas en un horario diario o semanal.
   - Iniciar sesiones de estudio con el temporizador Pomodoro.
2. Los datos se guardarán automáticamente en el archivo `tasks.json` en la carpeta `data`.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-caracteristica`).
3. Realiza tus cambios y haz commit (`git commit -m 'Añadir nueva característica'`).
4. Haz push a la rama (`git push origin feature/nueva-caracteristica`).
5. Crea un Pull Request.

---

## 📄 Licencia

Este proyecto está licenciado bajo los términos de la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

## 👤 Autor

**Planitarea** fue desarrollado por [Juan David Ardila Pacheco](https://github.com/Juardilap) como un proyecto para mejorar la productividad personal y la gestión del tiempo.

---
