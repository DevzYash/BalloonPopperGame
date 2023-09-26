package com.yash.balloonpopper

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.yash.balloonpopper.Utils.SoundUtils
import com.yash.balloonpopper.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Random


class MainActivity : AppCompatActivity(), Balloon.BalloonListener {
    private lateinit var binding: ActivityMainBinding
    var mScreenHeight: Int? = null
    var mBalloon = ArrayList<Balloon>()
    lateinit var mBalloonColors: List<Int>
    var screenWidth: Int? = null
    lateinit var mContentView: ViewGroup
    var balloonsLaunched: Int = 0
    lateinit var burstRandomList: List<String>
    lateinit var burstRandom: String
    lateinit var soundUtils: SoundUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setBackgroundDrawableResource(R.drawable.modern_background)
        mContentView = binding.root
        screenWidth = mContentView.width
        mScreenHeight = mContentView.height
        mBalloon = ArrayList()
        mBalloonColors = listOf(Color.RED, Color.BLUE)
        burstRandomList = listOf("Red", "Blue")
        soundUtils = SoundUtils(this)
        binding.startGame.setOnClickListener {
            GlobalScope.launch {
                balloonLauncher(2)
            }
        }

        val observer: ViewTreeObserver = mContentView.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
                mContentView.viewTreeObserver.removeOnGlobalLayoutListener { this }
                mScreenHeight = mContentView.height
                screenWidth = mContentView.width
            })
        }


    }

    private suspend fun balloonLauncher(balloonLimit: Int) {
        balloonsLaunched = 0
        while (balloonsLaunched < balloonLimit) {
            val random = Random(Date().time)
            val xPosition = random.nextInt(screenWidth!! - 150)
            withContext(Dispatchers.Main) {
                launchBalloon(xPosition.toFloat())
            }
            balloonsLaunched++
            val delay = random.nextInt(500) + 500
            delay(delay.toLong())
        }
    }

    private fun launchBalloon(x: Float) {
        val balloon = Balloon(this@MainActivity, mBalloonColors.random(), 100)
        balloon.x = x
        balloon.y = mScreenHeight!!.toFloat() + balloon.height
        mContentView.addView(balloon)
        balloon.releaseBalloon(mScreenHeight!!, 3000)

    }


    override fun popBalloon(balloon: Balloon?, touched: Boolean, color: Int) {
        mContentView.removeView(balloon)
        if (touched) {
            soundUtils.playSound()
            binding.tochTV.text = balloon!!.colorName
            GlobalScope.launch {
                balloonLauncher(1)
            }

//            if (balloon!!.colorName.equals("Red")&&burstRandom.equals("Red")){
//                mContentView.removeView(balloon)
//            }
//            else if(balloon!!.colorName.equals("Blue")&&burstRandom.equals("Blue")){
//                mContentView.removeView(balloon)
//            }

        }

    }


}

