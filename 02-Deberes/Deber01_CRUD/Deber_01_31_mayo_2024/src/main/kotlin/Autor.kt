import java.io.*
import java.time.LocalDate

data class Autor(
    var id: Int,
    var nombre: String,
    var nacionalidad: String,
    var fechaNacimiento: LocalDate,
    var libros: MutableList<Libro>
) : Serializable {
    companion object {
        private const val FILE_NAME = "src/main/files/autores.txt"

        fun guardarAutores(autores: List<Autor>) {
            try {
                ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(autores) }
            } catch (e: IOException) {
                println("Error al guardar los autores: ${e.message}")
            }
        }

        fun cargarAutores(): MutableList<Autor> {
            val file = File(FILE_NAME)
            return if (file.exists() && file.length() > 0) {
                try {
                    ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as MutableList<Autor> }
                } catch (e: IOException) {
                    println("Error al cargar los autores: ${e.message}")
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
        }

        fun validarAutor(autor: Autor): Boolean {
            return if (autor.nombre.isNotBlank() && autor.nacionalidad.isNotBlank()) {
                true
            } else {
                println("Nombre y nacionalidad no pueden estar vacíos.")
                false
            }
        }

        fun crearAutor(autores: MutableList<Autor>, autor: Autor) {
            if (validarAutor(autor)) {
                autores.add(autor)
                guardarAutores(autores)
            }
        }

        fun actualizarAutor(autores: MutableList<Autor>, id: Int, nuevoAutor: Autor) {
            val autor = autores.find { it.id == id }
            if (autor != null) {
                if (validarAutor(nuevoAutor)) {
                    autor.nombre = nuevoAutor.nombre
                    autor.nacionalidad = nuevoAutor.nacionalidad
                    autor.fechaNacimiento = nuevoAutor.fechaNacimiento
                    autor.libros = nuevoAutor.libros
                    guardarAutores(autores)
                }
            }
        }

        fun eliminarAutor(autores: MutableList<Autor>, id: Int) {
            autores.removeIf { it.id == id }
            guardarAutores(autores)
        }

        fun leerAutores(autores: List<Autor>) {
            autores.forEach { autor ->
                println("Autor ID: ${autor.id}, Nombre: ${autor.nombre}, Nacionalidad: ${autor.nacionalidad}, Fecha de Nacimiento: ${autor.fechaNacimiento}")
                leerLibros(autor.libros)
            }
        }

        fun leerLibros(libros: List<Libro>) {
            libros.forEach { libro ->
                println("\tLibro ID: ${libro.id}, Título: ${libro.titulo}, Fecha de Publicación: ${libro.fechaPublicacion}, Género: ${libro.genero}")
            }
        }
    }
}
