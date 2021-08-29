package com.example.multialarmclock

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.multialarmclock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var settings: ImageButton? = null
    var avd: AnimatedVectorDrawableCompat? = null
    var avd2: AnimatedVectorDrawable? = null
    var toolbar: Toolbar? = null
//    var imageButtonTopLeft: AppCompatButton? = null
//    var imageButtonTopRight: AppCompatButton? = null
    var cardviewTopLeft: CardView? = null
    var cardviewTopRight: CardView? = null
    var cardviewBottomLeft: CardView? = null
    var cardviewBottomRight: CardView? = null

    var cardviewTopLeftHeader: TextView? = null
    var cardviewTopRightHeader: TextView? = null
    var cardviewBottomLeftHeader: TextView? = null
    var cardviewBottomRightHeader: TextView? = null


    var cardviewSetButton: AppCompatButton? = null
    var cardviewEditButton: AppCompatButton? = null
    var daysChosenTv: TextView? = null
    var divider: View? = null
    var divider2: View? = null
    var alarmRangeTv: TextView? = null
    var setEveryTv: TextView? = null
//    var radius resources

    private var menu: Menu? = null
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.mytoolbar)
        setSupportActionBar(toolbar)


        cardviewTopLeft = findViewById(R.id.cardview_top_left)
        cardviewTopRight = findViewById(R.id.cardview_top_right)
        cardviewBottomLeft = findViewById(R.id.cardview_bottom_left)
        cardviewBottomRight = findViewById(R.id.cardview_bottom_right)



        cardviewTopLeftHeader = findViewById(R.id.cardview_top_left_tv)
        cardviewTopRightHeader = findViewById(R.id.top_right_cardview_tv)
        cardviewBottomLeftHeader = findViewById(R.id.cardview_bottom_left_tv)
        cardviewBottomRightHeader = findViewById(R.id.cardview_bottom_right_tv)

        cardviewSetButton = findViewById(R.id.set_button)
        cardviewEditButton = findViewById(R.id.edit_button)
        daysChosenTv = findViewById(R.id.days_chosen)
        divider = findViewById(R.id.divider)
        divider2 = findViewById(R.id.divider2)
        alarmRangeTv = findViewById(R.id.range)
        setEveryTv = findViewById(R.id.go_off_times)


//        val buttonClick = AlphaAnimation(1f, 0.8f)

//        cardviewEditButton.setOnTouchListener
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawable = item.icon
        if (drawable is AnimatedVectorDrawable) {
            avd = drawable as AnimatedVectorDrawableCompat
            avd!!.start()
        } else if (drawable is AnimatedVectorDrawable) {
            avd2 = drawable
            avd2!!.start()
        }
        return super.onOptionsItemSelected(item)
    }

//    fun buttonEffect(button: View){
//        cardviewEditButton.setOnTouchListener { v, event ->
//            when(event.action){
//                MotionEvent.ACTION_DOWN -> {
//                    v.background.setColorFilter()
//                }
//            }
//        }
//    }
}