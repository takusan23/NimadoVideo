package io.github.takusan23.nimadovideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = Bundle()
        bundle.putInt("pos", 0)
        supportFragmentManager.beginTransaction().replace(
            base_fragment.id,
            PlayerCardFragment().apply { arguments = bundle }
        ).commit()

        fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("pos", parent_linearlayout.childCount)
            //PlayerCardFragment追加
            val linearLayout = LinearLayout(this)
            parent_linearlayout.addView(linearLayout)
            linearLayout.id = View.generateViewId()
            supportFragmentManager.beginTransaction().replace(
                linearLayout.id,
                PlayerCardFragment().apply { arguments = bundle }
            ).commit()
        }

    }
}
