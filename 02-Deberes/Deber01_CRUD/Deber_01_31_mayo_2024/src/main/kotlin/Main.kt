package org.example

import Autor
import Libro
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val autores = Autor.cargarAutores()

    while (true) {
        mostrarMenu()
        val opcion = getIntInput(scanner, "Por favor, seleccione una opción del menú: ")
        when (opcion) {
            1 -> crearAutor(scanner, autores)
            2 -> actualizarAutor(scanner, autores)
            3 -> eliminarAutor(scanner, autores)
            4 -> Autor.leerAutores(autores)
            5 -> leerLibrosDeAutor(scanner, autores)
            6 -> crearLibro(scanner, autores)
            7 -> actualizarLibro(scanner, autores)
            8 -> eliminarLibro(scanner, autores)
            9 -> leerTodosLosLibros(autores)
            10 -> {
                println("El programa ha finalizado exitosamente. ¡Gracias por usar nuestra aplicación!")
                break
            }
            else -> println("Lo siento, la opción que ingresaste no es válida. Por favor, intenta de nuevo con una opción válida.")
        }
    }
}

fun mostrarMenu() {
    println("CRUD Autores-Libros")
    println("1. Crear un Autor")
    println("2. Actualizar un Autor")
    println("3. Eliminar un Autor")
    println("4. Listar todos los Autores y sus libros")
    println("5. Listar libros de un Autor")
    println("6. Registrar un Libro")
    println("7. Actualizar un Libro")
    println("8. Eliminar un Libro")
    println("9. Listar todos los Libros")
    println("10. Finalizar y salir del Programa")
}

fun getIntInput(scanner: Scanner, prompt: String): Int {
    println(prompt)
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    return scanner.nextInt()
}

fun getAutorById(autores: MutableList<Autor>, id: Int): Autor? {
    return autores.find { it.id == id } ?: println("Autor no encontrado.").let { null }
}

fun getLibroById(libros: MutableList<Libro>, id: Int): Libro? {
    return libros.find { it.id == id } ?: println("Libro no encontrado.").let { null }
}

fun crearAutor(scanner: Scanner, autores: MutableList<Autor>) {
    val id = getIntInput(scanner, "Ingrese el ID del Autor:")
    if (autores.any { it.id == id }) {
        println("Error: Ya existe un autor con el ID proporcionado.")
        return
    }
    scanner.nextLine()
    println("Ingrese el Nombre del Autor:")
    val nombre = scanner.nextLine()
    println("Ingrese la Nacionalidad del Autor:")
    val nacionalidad = scanner.nextLine()
    var fechaNacimiento: LocalDate? = null
    while (fechaNacimiento == null) {
        println("Ingrese la Fecha de Nacimiento del Autor (formato AAAA-MM-DD):")
        try {
            fechaNacimiento = LocalDate.parse(scanner.nextLine())
        } catch (e: DateTimeParseException) {
            println("Fecha no válida. Por favor, intente de nuevo.")
        }
    }
    val nuevoAutor = Autor(id, nombre, nacionalidad, fechaNacimiento, mutableListOf())
    Autor.crearAutor(autores, nuevoAutor)
}

fun crearLibro(scanner: Scanner, autores: MutableList<Autor>) {
    val autorId = getIntInput(scanner, "Ingrese el ID del Autor del Libro:")
    val autor = getAutorById(autores, autorId)
    if (autor != null) {
        val id = getIntInput(scanner, "Ingrese el ID del Libro:")
        if (autor.libros.any { it.id == id }) {
            println("Error: Ya existe un libro con el ID proporcionado.")
            return
        }
        scanner.nextLine()
        println("Ingrese el Título del Libro:")
        val titulo = scanner.nextLine()
        var fechaPublicacion: LocalDate? = null
        while (fechaPublicacion == null) {
            println("Ingrese la Fecha de Publicación del Libro (formato AAAA-MM-DD):")
            try {
                fechaPublicacion = LocalDate.parse(scanner.nextLine())
            } catch (e: DateTimeParseException) {
                println("Fecha no válida. Por favor, intente de nuevo.")
            }
        }
        println("Ingrese el Género del Libro:")
        val genero = scanner.nextLine()
        val nuevoLibro = Libro(id, titulo, fechaPublicacion, genero)
        Libro.crearLibro(autor.libros, nuevoLibro)
        Autor.guardarAutores(autores)
    } else {
        println("Autor no encontrado.")
    }
}

fun leerLibrosDeAutor(scanner: Scanner, autores: MutableList<Autor>) {
    val autorId = getIntInput(scanner, "Ingrese el ID del Autor cuyos libros desea ver:")
    val autor = getAutorById(autores, autorId)
    if (autor != null) {
        Autor.leerLibros(autor.libros)
    }
}

fun leerTodosLosLibros(autores: MutableList<Autor>) {
    if (autores.isEmpty()) {
        println("No hay autores para mostrar.")
    } else {
        autores.forEach { autor ->
            if (autor.libros.isEmpty()) {
                println("\tNo hay libros en este autor.")
            } else {
                autor.libros.forEach { libro ->
                    println("Libro ID: ${libro.id}, Título: ${libro.titulo}, Fecha de Publicación: ${libro.fechaPublicacion}, Género: ${libro.genero}")
                }
            }
        }
    }
}

fun actualizarAutor(scanner: Scanner, autores: MutableList<Autor>) {
    val id = getIntInput(scanner, "Ingrese el ID del Autor a actualizar:")
    if (autores.none { it.id == id }) {
        println("Error: No existe un autor con el ID proporcionado.")
        return
    }
    scanner.nextLine()
    println("Ingrese el Nuevo Nombre del Autor:")
    val nombre = scanner.nextLine()
    println("Ingrese la Nueva Nacionalidad del Autor:")
    val nacionalidad = scanner.nextLine()
    var fechaNacimiento: LocalDate? = null
    while (fechaNacimiento == null) {
        println("Ingrese la Nueva Fecha de Nacimiento del Autor (formato AAAA-MM-DD):")
        try {
            fechaNacimiento = LocalDate.parse(scanner.nextLine())
        } catch (e: DateTimeParseException) {
            println("Fecha no válida. Por favor, intente de nuevo.")
        }
    }
    val autor = getAutorById(autores, id)
    if (autor != null) {
        if (nombre.isNotBlank() && nacionalidad.isNotBlank()) {
            autor.nombre = nombre
            autor.nacionalidad = nacionalidad
            autor.fechaNacimiento = fechaNacimiento
            Autor.guardarAutores(autores)
        } else {
            println("Nombre y nacionalidad no pueden estar vacíos.")
        }
    }
}

fun actualizarLibro(scanner: Scanner, autores: MutableList<Autor>) {
    val autorId = getIntInput(scanner, "Ingrese el ID del Autor del Libro:")
    val autor = getAutorById(autores, autorId)
    if (autor != null) {
        val id = getIntInput(scanner, "Ingrese el ID del Libro a actualizar:")
        val libro = getLibroById(autor.libros, id)
        if (libro != null) {
            scanner.nextLine()
            println("Ingrese el Nuevo Título del Libro:")
            val titulo = scanner.nextLine()
            var fechaPublicacion: LocalDate? = null
            while (fechaPublicacion == null) {
                println("Ingrese la Nueva Fecha de Publicación del Libro (formato AAAA-MM-DD):")
                try {
                    fechaPublicacion = LocalDate.parse(scanner.nextLine())
                } catch (e: DateTimeParseException) {
                    println("Fecha no válida. Por favor, intente de nuevo.")
                }
            }
            println("Ingrese el Nuevo Género del Libro:")
            val genero = scanner.nextLine()
            if (titulo.isNotBlank() && genero.isNotBlank()) {
                libro.titulo = titulo
                libro.fechaPublicacion = fechaPublicacion
                libro.genero = genero
                Autor.guardarAutores(autores)
            } else {
                println("Título y género no pueden estar vacíos.")
            }
        } else {
            println("Libro no encontrado.")
        }
    } else {
        println("Autor no encontrado.")
    }
}

fun eliminarAutor(scanner: Scanner, autores: MutableList<Autor>) {
    val id = getIntInput(scanner, "Ingrese el ID del Autor a eliminar:")
    val autor = getAutorById(autores, id)
    if (autor != null) {
        autor.libros.forEach { libro ->
            Libro.eliminarLibroPorId(libro.id)
        }
        Autor.eliminarAutor(autores, id)
    } else {
        println("Autor no encontrado.")
    }
}

fun eliminarLibro(scanner: Scanner, autores: MutableList<Autor>) {
    val autorId = getIntInput(scanner, "Ingrese el ID del Autor del Libro:")
    val autor = getAutorById(autores, autorId)
    if (autor != null) {
        val id = getIntInput(scanner, "Ingrese el ID del Libro a eliminar:")
        Libro.eliminarLibro(autor.libros, id)
        Autor.guardarAutores(autores)
    } else {
        println("Autor no encontrado.")
    }
}
