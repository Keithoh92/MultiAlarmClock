package com.example.multialarmclock

import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Bundle
import com.example.multialarmclock.R
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    var settings: ImageButton? = null
    var avd: AnimatedVectorDrawableCompat? = null
    var avd2: AnimatedVectorDrawable? = null
    var toolbar: Toolbar? = null
//    var imageButtonTopLeft: AppCompatButton? = null
//    var imageButtonTopRight: AppCompatButton? = null
    var cardviewTopLeft: CardView? = null
    var cardviewTopRight: CardView? = null
    var cardviewTopLeftHeader: TextView? = null
    var cardviewTopRightHeader: TextView? = null
    var cardviewSetButton: AppCompatButton? = null
    var cardviewEditButton: AppCompatButton? = null

    private var menu: Menu? = null
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.mytoolbar)
        setSupportActionBar(toolbar)

        cardviewTopLeft = findViewById(R.id.cardview_top_left)
        cardviewTopRight = findViewById(R.id.cardview_top_right)
        cardviewTopLeftHeader = findViewById(R.id.cardview_top_left_tv)
        cardviewTopRightHeader = findViewById(R.id.top_right_cardview_tv)
        cardviewSetButton = findViewById(R.id.set_button)
        cardviewEditButton = findViewById(R.id.edit_button)

//        imageButtonTopLeft = findViewById(R.id.topLeftImageButton)
//        imageButtonTopRight = findViewById(R.id.topRightImageButton)
        //        settings = findViewById(R.id.settingsButton);

//        Drawable drawable = menu.getItem(0).getIcon();
//
//        if(drawable instanceof AnimatedVectorDrawableCompat){
//            avd = (AnimatedVectorDrawableCompat) drawable;
//            avd.start();
//        }else if(drawable instanceof AnimatedVectorDrawable){
//            avd2 = (AnimatedVectorDrawable) drawable;
//            avd2.start();
//        }
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
}