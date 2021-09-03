package com.example.multialarmclock

import android.graphics.drawable.shapes.Shape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.Toolbar

import com.google.android.material.imageview.ShapeableImageView

class BuildNewAlarm : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var edit_name: EditText? = null
//    var edit_name2: EditText? = null
    var shape_layout: ShapeableImageView? = null
    var shape_layout2: ShapeableImageView? = null
//    var relative_layout: View? = null

    var days_dialog_button: Button? = null
    var radio_group: RadioGroup? = null
    var onRadioButton: RadioButton? = null
    var offRadioButton: RadioButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build_new_alarm)

        toolbar = findViewById(R.id.mytoolbar_build_alarm)
        setSupportActionBar(toolbar)

//        relative_layout = findViewById(R.id.text_layout)
        edit_name = findViewById(R.id.edit_name)
//        edit_name2 = findViewById(R.id.edit_name2)
        shape_layout = findViewById(R.id.shape_corner)
//        shape_layout2 = findViewById(R.id.shape_corner2)
        days_dialog_button = findViewById(R.id.days_dialog_button)
        radio_group = findViewById(R.id.toggle_id)
        onRadioButton = findViewById(R.id.toggle_on)
        offRadioButton = findViewById(R.id.toggle_off)

    }
}