package com.example.deber02_crud_movil

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class GoogleMapsActivity : AppCompatActivity() {

    private lateinit var mapa: GoogleMap
    var permisos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_google_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_google_maps)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        solicitarPermisos()
        iniciarLogicaMapa()
        /*val botonCarolina = findViewById<Button>(R.id.btn_ir_ubicacion)
        botonCarolina.setOnClickListener {
            val carolina = LatLng(-0.18221288005854652, -78.48553955554578)
            val zoom = 17f
            moverCamaraConZoom(carolina, zoom)
        }*/

        //---------------------

        // Para retroceso de actividades
        val toolbar: Toolbar = findViewById(R.id.toolbar_ubicacion)
        setSupportActionBar(toolbar)

        // Habilitar el botón de regreso en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // ----------------------

        val locationString = intent.getStringExtra("AUTHOR_LOCATION") ?: ""
        val geocoder = Geocoder(this, Locale.getDefault())

        val addressList = geocoder.getFromLocationName(locationString, 1)
        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            val zoom = 17f
            val botonUbicacion = findViewById<Button>(R.id.btn_ir_ubicacion)
            botonUbicacion.setOnClickListener {
                moverCamaraConZoom(latLng, zoom)
            }

            // Marcar la ubicación directamente al iniciar
            //anadirMarcador(carolina, "Ubicación de $locationString")
        } else {
            Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    fun solicitarPermisos(){
        val contexto = this.applicationContext
        val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
        val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        if(tienePermisos){
            permisos = true
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine,nombrePermisoCoarse), 1
            )
        }
    }
    fun iniciarLogicaMapa(){
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync{ googleMap ->
            with(googleMap){
                mapa = googleMap
                establecerConfiguracionMapa()
                escucharListeners()
            }
        }
    }
    fun  escucharListeners(){
        mapa.setOnPolygonClickListener {
            mostrarSnackbar("setOnPolygonClickListener $it.tag")
        }
        mapa.setOnPolylineClickListener {
            mostrarSnackbar("setOnPolylineClickListener $it.tag")
        }
        mapa.setOnMarkerClickListener {
            mostrarSnackbar("setOnMarkerClickListener $it.tag")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraMoveListener {
            mostrarSnackbar("setOnCameraMoveListener")
        }
        mapa.setOnCameraMoveStartedListener {
            mostrarSnackbar("setOnCameraMoveStartedListener")
        }
        mapa.setOnCameraIdleListener {
            mostrarSnackbar("setOnCameraIdleListener")
        }
    }
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_google_maps),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
    fun anadirMarcador(latLang:LatLng, title:String): Marker {
        return mapa.addMarker(
            MarkerOptions().position(latLang)
                .title(title)
        )!!
    }


    fun establecerConfiguracionMapa(){
        val contexto = this.applicationContext
        with(mapa){
            val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
            val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
            val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
            val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
            val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                    permisoCoarse == PackageManager.PERMISSION_GRANTED
            if(tienePermisos){
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }
    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 10f){
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
            latLang, zoom
        ))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar el clic del botón de regreso en la Toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}