package org.example

import java.util.*

fun main() {
    val ejemploVariable = " David Yánez "
    ejemploVariable.trim()

    val edadEjemplo: Int = 12
    //ejemploVariable = edadEjemplo // Error!

    // Variables Primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    // Clases Java}
    val fechaNacimiento: Date = Date();

    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") -> {
            println("Casado")
        }

        "S" -> {
            println("Casado")
        }

        else -> {
            println("No sabemos")
        }
    }

    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if(esSoltero) "Si" else "No"


    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00, 20.00)

    // Parámetros nombrados
    calcularSueldo(10.00, bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 10.00, sueldo = 15.00, tasa = 14.00)

    // Uso de clases 15/05/2024
    val sumaUno = Suma(1,1)
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1,null)
    val sumaCuatro = Suma(null, null)

    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    //Arreglos
    // Estáticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico)

    // Dinámicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // FOR EACH => Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach{ valorActual: Int ->
            println("Valor Actual: ${valorActual}")
        }
    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor actual (it): ${it}")}

    // Clase 17/05/2024
    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor Actual (it) ${it}")}
    // MAP -> MUTA (Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un Nuevo ARREGLO con valores
    // de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map{ it + 15}
    println(respuestaMapDos)

    // Filter -> Filtrar el ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
    .filter{ valorActual: Int ->
        // Expresion o CONDICION
        val mayoresACinco: Boolean = valorActual > 5
        return@filter mayoresACinco
    }

    val respuestaFilterDos = arregloDinamico.filter { it <= 5}
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno Cumple?)
    // AND -> ALL (Todos cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual:Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) //True

    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual: Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) // False

    // REDUCE -> Valor acumulado
    // Valor acumulado = 0 (Siempre empieza en 0 en kotlin)
    // [1,2,3,4,5] -> Acumular "SUMAR" estos valores del arreglo
    // valorIteracion1 = valorEmpieza + 1 = 0 +1= 1 -> Iteracion1
    // valorIteracion2 = valorAcuuladoIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // valorIteracion3 = valorAcuuladoIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    // valorIteracion4 = valorAcuuladoIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4
    // valorIteracion5 = valorAcuuladoIteracion5 + 5 = 10 + 5 = 15 -> Iteracion4
    val respuestaReduce: Int = arregloDinamico
        .reduce{ acumulado: Int, valorActual: Int ->
            return@reduce acumulado + valorActual // -> Cambiar o usar la logica de negocio
        }
    println(respuestaReduce);
    //return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)

    
}

fun imprimirNombre(nombre:String): Unit {
    println("Nombre: ${nombre}") // Template Strings
}

fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial:Double? = null // Opcional (nulalble)
):Double {
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)

    if(bonoEspecial == null) {
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) + bonoEspecial
    }
}

abstract class NumerosJava{
    protected val numeroUno:Int
    private val numeroDos:Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract class Numeros(
    // Constructor Primario
    // Caso 1) Parametro normal
    // uno:Int, (parametro (sin modifical acceso))

    // Caso 2) Parametro y propiedad (atributo) (protected)
    // private var uno: Int (propiedad "instancia.uno")
    protected val numeroUno: Int, // instancia.numeroUno
    protected val numeroDos: Int, // instancia.numeroDos
)
{
    init{ // bloque constructor primario OPCIONAL
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma( // Constructor primario
    unoParametro: Int, // Parametro
    dosParametro: Int, // Parametro
): Numeros( // Clase papa, Numeros (extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito = "Explicito" // Publicas
    val soyPublicoImplicito:String = "Implicitito"

    init{
        this.numeroDos
        this.numeroUno
        numeroUno // this. OPCIONAL (propiedades, metodos)
        numeroDos // this, OPCIONAL (propiedades, metodos)
        this.soyPublicoExplicito
        soyPublicoImplicito // this. OPCIONAL (propuedades, metodos)
    }

    fun sumar(): Int{
        val total = numeroUno + numeroDos
        // Suma.agregarHistorial(total)
        // ("Suma." o "NombreClase." es OPCIONAL)
        agregarHistorial(total)
        return total
    }

    companion object {
        // similar al Static
        // funciones y variables
        val pi = 3.14 // Suma.pi

        fun elevarAlCuadrado(num:Int):Int{ // Suma.elevarAlCuadrado
            return num * num
        }

        val historialSumas = arrayListOf<Int>() // Suma.historialSumas

        fun agregarHistorial(valorTotalSuma:Int){ // Suma.agregarHistorial
            historialSumas.add(valorTotalSuma)
        }
    }

    constructor( // Constructor secundario
        uno:Int?,
        dos:Int
    ):this(
        if(uno==null) 0 else uno,
        dos
    )

    constructor( // Constructor terciario
        uno:Int,
        dos:Int?
    ):this(
        uno,
        if(dos==null) 0 else dos,
    )

    constructor( // Constructor cuarto
        uno:Int?,
        dos:Int?
    ):this(
        if(uno==null) 0 else uno,
        if(dos==null) 0 else dos
    )
}