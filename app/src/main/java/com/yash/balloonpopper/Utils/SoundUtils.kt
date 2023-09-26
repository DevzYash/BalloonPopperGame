package com.yash.balloonpopper.Utils

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.view.View
import com.yash.balloonpopper.R


class SoundUtils(activity: Activity) {
    private var mVolume: Float
    private var mLoaded: Boolean = false
    private var mSoundPool: SoundPool? = null
    private var mSoundID = 0

    init {
        val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val actVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
         mVolume = actVolume / maxVolume

        activity.volumeControlStream = AudioManager.STREAM_MUSIC

        val audioAttrib = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mSoundPool = SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build()

        mSoundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status -> mLoaded = true }
        mSoundID = mSoundPool!!.load(activity, R.raw.balloon_pop,1)
    }

    fun playSound() {

        // Is the sound loaded does it already play?
        if (mLoaded) {
            mSoundPool!!.play(mSoundID, mVolume, mVolume, 1, 0, 1f)
        }
    }
}