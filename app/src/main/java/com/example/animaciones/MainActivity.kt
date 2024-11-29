package com.example.animaciones

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    //Declaración de variables necesarias para las animaciones y vistas
    private lateinit var imagen_core: ImageView
    private lateinit var imagen_pd: ImageView
    private lateinit var imagen_pi: ImageView
    private lateinit var core_caliente: ImageView
    private lateinit var animacion_core: AnimationDrawable
    private lateinit var animacion_pd: AnimationDrawable
    private lateinit var animacion_pi: AnimationDrawable
    private lateinit var animacion_salto: Animatable
    private lateinit var  botonMenu: Button
    private lateinit var  botonSaltar: Button
    private lateinit var cardAnimacion: CardView
    private var encendido: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Configuración de la pantalla Splash
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Configura el diseño para aprovechar toda la pantalla (sin barras del sistema)
        setContentView(R.layout.activity_main) // Establece el diseño de la actividad

        // Configuración de la pantalla de inicio y su duración
        screenSplash.setKeepOnScreenCondition { false }
        Thread.sleep(3000) // Mantiene la Splash Screen por 3 segundos

        // Ajuste de los márgenes para evitar que el contenido se solape con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom) // Ajuste de padding
            insets
        }

        // Inicializa los componentes y los listeners de los botones
        initComponent()
        initListeners()
    }

    private fun initComponent() {
        // Inicialización de las vistas para las animaciones
        imagen_core = findViewById<ImageView>(R.id.core).apply {
            setBackgroundResource(R.drawable.animation_core) // Asigna un recurso de animación al ImageView
            animacion_core = background as AnimationDrawable // Obtiene la animación como un AnimationDrawable
        }

        // Animación para la pierna derecha
        imagen_pd = findViewById<ImageView>(R.id.pd).apply {
            setBackgroundResource(R.drawable.animation_pd) // Asigna el recurso de animación
            animacion_pd = background as AnimationDrawable // Inicializa la animación
        }

        // Animación para la pierna izquierda
        imagen_pi = findViewById<ImageView>(R.id.pi).apply {
            setBackgroundResource(R.drawable.animation_pi) // Asigna el recurso de animación
            animacion_pi = background as AnimationDrawable // Inicializa la animación
        }

        // Intento de animación de un vector, que está deshabilitado
        // core_caliente = findViewById<ImageView>(R.id.caliente).apply {
        //     setBackgroundResource(R.drawable.animacion) // Recurso de animación para el calor
        //     animacion_salto = background as Animatable // Inicializa la animación vectorial
        // }

        // Inicialización de los botones y las vistas de la animación
        cardAnimacion = findViewById(R.id.cardAnimacion)
        botonMenu = findViewById(R.id.botonMenu)
        botonSaltar = findViewById(R.id.botonSalta)
        botonSaltar.visibility = View.GONE // Hace invisible el botón "Saltar" al inicio
    }

    private fun initListeners() {
        // Configura el listener para el botón de menú, que inicia o detiene las animaciones
        botonMenu.setOnClickListener {
            if(encendido == false) { // Si las animaciones no están activas, las inicia
                botonMenu.setText(R.string.botonMenuA) // Cambia el texto del botón
                startAnimation() // Inicia las animaciones
                botonSaltar.visibility = View.VISIBLE // Muestra el botón "Saltar"
                encendido = true // Marca que las animaciones están activas
            } else { // Si las animaciones ya están activas, las detiene
                botonMenu.setText(R.string.botonMenu) // Restaura el texto del botón
                stopAnimation() // Detiene las animaciones
                botonSaltar.visibility = View.GONE // Oculta el botón "Saltar"
                encendido = false // Marca que las animaciones no están activas
            }
        }

        // Configura el listener para el botón "Saltar"
        botonSaltar.setOnClickListener {
            // Intento de iniciar una animación de salto, no funcional en este caso
            // animacion_salto.start()

            // Nueva solución usando AnimatedVectorDrawable con AnimatorSet
            core_caliente = findViewById<ImageView>(R.id.caliente)
            val vector = ContextCompat.getDrawable(this, R.drawable.animacion) as AnimatedVectorDrawable
            core_caliente.setImageDrawable(vector) // Asocia la animación vectorial al ImageView
            vector.start() // Inicia la animación del vector

            // Animación de entrada (desaparece y aparece) para la imagen "caliente"
            val animEntrada = ObjectAnimator.ofFloat(core_caliente, "alpha", 0f, 1f)
            animEntrada.duration = 100 // Duración de la animación de entrada

            // Animación de salida (aparece y desaparece) para la imagen "caliente"
            val animSalida = ObjectAnimator.ofFloat(core_caliente, "alpha", 1f, 0f)
            animSalida.startDelay = 1000 // Retraso antes de la animación de salida
            animSalida.duration = 1000 // Duración de la animación de salida

            // Se crean las animaciones y se ejecutan de forma secuencial
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(animEntrada, animSalida)
            animatorSet.start() // Inicia el AnimatorSet

            // Animación del salto vertical (desplazamiento hacia arriba)
            val animacion_salto2 = ObjectAnimator.ofFloat(cardAnimacion, View.TRANSLATION_Y, -cardAnimacion.height.toFloat() * 2)
            animacion_salto2.duration = 2500 // Duración de la animación de salto

            // Animación de bajada (desplazamiento hacia abajo)
            val animacion_bajada = ObjectAnimator.ofFloat(cardAnimacion, View.TRANSLATION_Y, 0f)
            animacion_bajada.duration = 1500 // Duración de la animación de bajada

            // Se agrupan las animaciones en un AnimatorSet y se inician de forma secuencial
            val animacionSet = AnimatorSet()
            animacionSet.playSequentially(animacion_salto2, animacion_bajada)
            animacionSet.start() // Inicia el AnimatorSet de desplazamiento
        }
    }

    // Función para detener las animaciones de los AnimationDrawable
    private fun stopAnimation() {
        animacion_core.stop() // Detiene la animación del torso
        animacion_pd.stop() // Detiene la animación de la pierna derecha
        animacion_pi.stop() // Detiene la animación de la pierna izquierda
    }

    // Función para iniciar las animaciones de los AnimationDrawable
    private fun startAnimation() {
        animacion_core.start() // Inicia la animación del torso
        animacion_pd.start() // Inicia la animación de la pierna derecha
        animacion_pi.start() // Inicia la animación de la pierna izquierda
    }
}