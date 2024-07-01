package com.example.a2024aswgr1vaes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida) //Encontramos el componente por el identificador:
                        //"R." -> Hace referencia a recursos
        botonCicloVida.setOnClickListener {
            irActividad(ACicloVida::class.java)
        }

        val botonIrListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonIrListView
            .setOnClickListener {
                irActividad(BListView::class.java)
            }

    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}