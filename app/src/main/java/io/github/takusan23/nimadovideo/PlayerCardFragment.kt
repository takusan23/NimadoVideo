package io.github.takusan23.nimadovideo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_player_video.*
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.exoplayer2.*
import kotlinx.android.synthetic.main.activity_main.*


class PlayerCardFragment : Fragment() {

    val READ_REQUEST_CODE = 810

    lateinit var exoPlayer: SimpleExoPlayer

    // 再生速度
    var speed = 1f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 16:9にする
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (width / 16) * 9)
        exoplayerview.layoutParams = layoutParams

        openSAF()
        fragment_player_open_file.setOnClickListener {
            openSAF()
            // 初期化済みなら再生止める
            if (::exoPlayer.isInitialized) {
                exoPlayer.playWhenReady = false
                exoPlayer.release()
            }
        }

        //削除
        fragment_player_close.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.parent_linearlayout.removeView(fragment_player_parent)
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            // 初期化済みなら再生止める
            if (::exoPlayer.isInitialized) {
                exoPlayer.playWhenReady = false
                exoPlayer.release()
            }
        }

    }

    //動画を取得する
    fun openSAF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE) {
            val uri = data?.data
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context)
            val dataSourceFactory =
                DefaultDataSourceFactory(context, Util.getUserAgent(context, "NimadoVideo"))
            val mediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            //再生
            exoPlayer.prepare(mediaSource)
            exoplayerview.player = exoPlayer
            //ミュートできるように
            fragment_player_volume.setOnClickListener {
                exoPlayer.apply {
                    if (volume == 0f) {
                        fragment_player_volume.setImageDrawable(context?.getDrawable(R.drawable.ic_volume_up_24px))
                        volume = 10f
                    } else {
                        fragment_player_volume.setImageDrawable(context?.getDrawable(R.drawable.ic_volume_off_24px))
                        volume = 0f
                    }
                }
            }
            //リピート再生
            fragment_player_repeat.setOnClickListener {
                exoPlayer.apply {
                    if (repeatMode == Player.REPEAT_MODE_OFF) {
                        fragment_player_repeat.setImageDrawable(context?.getDrawable(R.drawable.ic_repeat_one_24px))
                        repeatMode = Player.REPEAT_MODE_ONE
                    } else {
                        fragment_player_repeat.setImageDrawable(context?.getDrawable(R.drawable.ic_repeat_24px))
                        repeatMode = Player.REPEAT_MODE_OFF
                    }
                }
            }
            fragment_player_speed.setOnClickListener {
                exoPlayer.apply {
                    if (speed == 1f) {
                        // 2倍にする
                        speed = 2f
                        val playbackParameters = PlaybackParameters(speed)
                        exoPlayer.playbackParameters = playbackParameters
                        Toast.makeText(context, "x2", Toast.LENGTH_SHORT).show()
                    } else if (speed == 2f) {
                        // 5倍に
                        speed = 5f
                        val playbackParameters = PlaybackParameters(speed)
                        exoPlayer.playbackParameters = playbackParameters
                        Toast.makeText(context, "x5", Toast.LENGTH_SHORT).show()
                    } else {
                        // 初期
                        speed = 1f
                        val playbackParameters = PlaybackParameters(speed)
                        exoPlayer.playbackParameters = playbackParameters
                        Toast.makeText(context, "x1", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun reSize(height: Int, Width: Int) {
        val layoutParams = LinearLayout.LayoutParams(Width, height)
        exoplayerview.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

}