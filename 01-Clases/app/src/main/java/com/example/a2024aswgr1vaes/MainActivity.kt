package com.example.a2024aswgr1vaes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.id_layout_main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Logica negocio
                    val data = result.data
                    mostrarSnackbar(
                        "${data?.getStringExtra("nombreModificado")}"
                    )
                }
            }
        }

    val callbackContenidoIntentImplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Logica negocio
                    if (result.data!!.data != null) {
                        val uri: Uri = result.data!!.data!!
                        val cursor = contentResolver.query(
                            uri, null, null, null, null, null
                        )

                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(indiceTelefono!!)
                        cursor?.close()
                        mostrarSnackbar("Telefono $telefono")
                    }
                }
            }
        }

    // Listener para el botón "btn_ir_intent_implicito"
    val botonIntentImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito).setOnClickListener {
        val intentConRespuesta = Intent(
            Intent.ACTION_PICK,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        )
        callbackContenidoIntentImplicito.launch(intentConRespuesta)
    }

    // Listener para el botón "btn_ir_intent_explicito"
    val botonIntentExplicito = findViewById<Button>(R.id.btn_ir_intent_explicito).setOnClickListener {
        val intentExplicito = Intent(
            this,
            CIntentExplicitoParametros::class.java
        )
        intentExplicito.putExtra("nombre", "Adrian")
        intentExplicito.putExtra("apellido", "Eguez")
        intentExplicito.putExtra("edad", 34)

        intentExplicito.putExtra(
            "entrenador",
            BEntrenador(1, "Adrian", "Eguez")
        )

        callbackContenidoIntentExplicito.launch(intentExplicito)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.id_layout_main)) { v, insets ->
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

        EBaseDeDatos.tablaEntrenador = ESQLiteHelperEntrenador(
            this
        )

        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }

        val botonRview = findViewById<Button>(R.id.btn_recycler_view)
        botonRview.setOnClickListener {
            irActividad(FRecyclerView::class.java)
        }

        val botonGMaps = findViewById<Button>(R.id.btn_google_maps)
        botonGMaps.setOnClickListener {
            irActividad(GGoogleMapsActivity::class.java)
        }
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}