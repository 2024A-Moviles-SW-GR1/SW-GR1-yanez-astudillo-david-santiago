import java.io.*
import java.time.LocalDate

data class Libro(
    var id: Int,
    var titulo: String,
    var fechaPublicacion: LocalDate,
    var genero: String
) : Serializable {
    companion object {
        private const val FILE_NAME = "src/main/files/libros.txt"

        fun guardarLibros(libros: List<Libro>) {
            try {
                ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(libros) }
            } catch (e: IOException) {
                println("Error al guardar los libros: ${e.message}")
            }
        }

        fun cargarLibros(): MutableList<Libro> {
            val file = File(FILE_NAME)
            return if (file.exists() && file.length() > 0) {
                try {
                    ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as MutableList<Libro> }
                } catch (e: IOException) {
                    println("Error al cargar los libros: ${e.message}")
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
        }

        fun validarLibro(libro: Libro): Boolean {
            return if (libro.titulo.isNotBlank() && libro.genero.isNotBlank()) {
                true
            } else {
                println("Título y género no pueden estar vacíos.")
                false
            }
        }

        fun crearLibro(libros: MutableList<Libro>, libro: Libro) {
            if (validarLibro(libro)) {
                libros.add(libro)
                guardarLibros(libros)
            }
        }

        fun actualizarLibro(libros: MutableList<Libro>, id: Int, nuevoLibro: Libro) {
            val libro = libros.find { it.id == id }
            if (libro != null) {
                if (validarLibro(nuevoLibro)) {
                    libro.titulo = nuevoLibro.titulo
                    libro.fechaPublicacion = nuevoLibro.fechaPublicacion
                    libro.genero = nuevoLibro.genero
                    guardarLibros(libros)
                }
            } else {
                println("Error: No se encontró un libro con el ID proporcionado.")
            }
        }

        fun eliminarLibro(libros: MutableList<Libro>, id: Int) {
            if (libros.removeIf { it.id == id }) {
                guardarLibros(libros)
            } else {
                println("Error: No se encontró un libro con el ID proporcionado.")
            }
        }

        fun eliminarLibroPorId(id: Int) {
            val libros = cargarLibros()
            libros.removeIf { it.id == id }
            guardarLibros(libros)
        }

        fun leerLibros(libros: List<Libro>) {
            if (libros.isEmpty()) {
                println("No hay libros para mostrar.")
            } else {
                libros.forEach { libro ->
                    println("Libro ID: ${libro.id}, Título: ${libro.titulo}, Fecha de Publicación: ${libro.fechaPublicacion}, Género: ${libro.genero}")
                }
            }
        }
    }
}
