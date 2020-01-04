package io.github.takusan23.nimadovideo

import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_fragment_size_change.*

class PlayCardSizeChangeBottomFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_fragment_size_change, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 16:9にする
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        bottom_fragment_size_saitekika.setOnClickListener {
            // 16:9 最適化
            val height = (width / 16) * 9
            reSizeFragment(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }

        bottom_fragment_size_four.setOnClickListener {
            //4画面
            val height = height / 5
            reSizeFragment(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }

        bottom_fragment_size_three.setOnClickListener {
            //三画面
            val height = height / 4
            reSizeFragment(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }

    }

    fun reSizeFragment(width: Int, height: Int) {
        //MainActivity
        val mainActivity = activity as MainActivity
        mainActivity.parent_linearlayout.children.forEach {
            //設置したFragment全て取得してサイズ変更
            val fragment =
                mainActivity.supportFragmentManager.findFragmentById(it.id) as PlayerCardFragment
            fragment.reSize(height, width)
        }
    }

}