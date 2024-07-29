package com.example.deber02_crud_movil

import java.time.LocalDate

data class Book(
    var id: Int,
    var title: String,
    var fechaPublicacion: String,
    var genero: String
)