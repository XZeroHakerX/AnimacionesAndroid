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

//Declaracion de las variables:

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

        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        screenSplash.setKeepOnScreenCondition { false }
        Thread.sleep(3000)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//Iniciamos componentess y listeners:
        initComponent()
        initListeners()



    }

    private fun initComponent() {
//Para la animacion del torso, usamos los AnimationDrawable:
        imagen_core = findViewById<ImageView>(R.id.core).apply {
            setBackgroundResource(R.drawable.animation_core)
            animacion_core = background as AnimationDrawable
        }
//Los 2 AnimationDrawable siguientes se refieren a las animaciones de la pierna derecha y la pierna izquierda:
        imagen_pd = findViewById<ImageView>(R.id.pd).apply{
            setBackgroundResource(R.drawable.animation_pd)
            animacion_pd = background as AnimationDrawable

        }

        imagen_pi = findViewById<ImageView>(R.id.pi).apply{
            setBackgroundResource(R.drawable.animation_pi)
            animacion_pi = background as AnimationDrawable

        }

// Aqui intento seguir la teoria pero no me funciona, me genera muchos problemas para el VectorAnimationDrawable

        //core_caliente = findViewById<ImageView>(R.id.caliente).apply{
        //  setBackgroundResource(R.drawable.animacion)
        //  animacion_salto = background as Animatable
        //}

//Inicio de botones para realizar animaciones:
        cardAnimacion = findViewById(R.id.cardAnimacion)
        botonMenu = findViewById(R.id.botonMenu)
        botonSaltar = findViewById(R.id.botonSalta)
        botonSaltar.visibility = View.GONE



    }


    private fun initListeners() {

//Boton principal, que inicia las animaciones de arranque, o las pausa. Da paso también al boton de salto, o desactiva segun convenga:
        botonMenu.setOnClickListener{
            if(encendido == false) {
                botonMenu.setText(R.string.botonMenuA)
                startAnimation()
                botonSaltar.visibility = View.VISIBLE
                encendido = true
            } else{
                botonMenu.setText(R.string.botonMenu)
                stopAnimation()
                botonSaltar.visibility = View.GONE
                encendido = false

            }

        }

// Boton saltar donde he intentado utilizar solo la llamada, como en la teoría, pero a vista de los errores he solucionado así:
        botonSaltar.setOnClickListener{
            //animacion_salto.start()

/*Utilizo directamente AnimatedVectorDrawable, e inicializando un AnimatorSet para ordenar y reproducir las animaciones:
Este primer AnimatorSet se encarga de enseñar una llama debajo del vehiculo, no he encontrado manera que la forma de la teoria funcione...*/
            core_caliente = findViewById<ImageView>(R.id.caliente)
            val vector = ContextCompat.getDrawable(this, R.drawable.animacion) as AnimatedVectorDrawable
            core_caliente.setImageDrawable(vector)
            vector.start()

            val animEntrada = ObjectAnimator.ofFloat(core_caliente, "alpha", 0f, 1f)
            animEntrada.duration = 100


            val animSalida = ObjectAnimator.ofFloat(core_caliente, "alpha", 1f, 0f)
            animSalida.startDelay = 1000
            animSalida.duration = 1000

            val animatorSet = AnimatorSet()
             animatorSet.playSequentially(animEntrada, animSalida)
             animatorSet.start()


//Este segundo AnimatorSet se encarga de hacer la animacion del desplazamiento vertical, apoyandonos de la clase ObjectAnimator, de manera mas secilla:
            val animacion_salto2 = ObjectAnimator.ofFloat(cardAnimacion, View.TRANSLATION_Y, -cardAnimacion.height.toFloat() * 2 )
            animacion_salto2.duration = 2500

            val animacion_bajada = ObjectAnimator.ofFloat(cardAnimacion, View.TRANSLATION_Y, 0f )
            animacion_bajada.duration = 1500

            val animacionSet = AnimatorSet()
            animacionSet.playSequentially( animacion_salto2, animacion_bajada)
            animacionSet.start()



        }
    }

//Dos funciones sencillas para empezar o terminar las animaciones de los AnimationDrawable:
    private fun stopAnimation() {
        animacion_core.stop()
        animacion_pd.stop()
        animacion_pi.stop()
    }

    private fun startAnimation() {
        animacion_core.start()
        animacion_pd.start()
        animacion_pi.start()
    }

}