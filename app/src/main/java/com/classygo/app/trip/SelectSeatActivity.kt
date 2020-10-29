package com.classygo.app.trip

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.classygo.app.R
import kotlinx.android.synthetic.main.activity_select_seat.*
import kotlinx.android.synthetic.main.activity_select_seat.toolbar
import kotlinx.android.synthetic.main.toolbar.*
import java.util.ArrayList

class SelectSeatActivity : AppCompatActivity(), View.OnClickListener {
    var layout: ViewGroup? = null
    var seats = ("AA_UA_/"
            + "_______________/"
            + "AA_AA_/"
            + "AA_AA_/"
            + "AA_AA_/"
            + "AA_AA_/"
            + "_________________/")
    var seatViewList: MutableList<TextView> = ArrayList()
    var seatSize = 100
    var seatGaping = 5

    var STATUS_AVAILABLE = 1
    var STATUS_BOOKED = 2
    var STATUS_RESERVED = 3
    var selectedIds = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        textViewTitle.text = getString(R.string.select_seat)


        layout = findViewById(R.id.layoutSeat)
        seats = "/$seats"

        val layoutSeat = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(4 * seatGaping, 4 * seatGaping, 4 * seatGaping, 4 * seatGaping)
        layout?.addView(layoutSeat)
        var layout: LinearLayout? = null

        var count = 0
        for (index in 0 until seats.length) {
            if (seats[index] == '/') {
                layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL
                layoutSeat.addView(layout)
            } else if (seats[index] == 'U') {
                count++
                val view = TextView(this)
                val layoutParams =
                    LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_car_seat_booked)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_BOOKED
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == 'A') {
                count++
                val view = TextView(this)
                val layoutParams =
                    LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_car_seat_available)
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = STATUS_AVAILABLE
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == 'R') {
                count++
                val view = TextView(this)
                val layoutParams =
                    LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_car_seat_booked)
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_RESERVED
                layout!!.addView(view)
                seatViewList.toMutableList().add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == '_') {
                val view = TextView(this)
                val layoutParams =
                    LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setBackgroundColor(Color.TRANSPARENT)
                view.text = ""
                layout!!.addView(view)
            }
        }
    }

    override fun onClick(view: View?) {
        if (view?.getTag() as Int == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId().toString() + ",")) {
                selectedIds = selectedIds.replace(view.getId().toString() + ",", "")
                view.setBackgroundResource(R.drawable.ic_car_seat_available)
            } else {
                selectedIds = selectedIds + view.getId() + ","
                view.setBackgroundResource(R.drawable.ic_car_seat_booked)
            }
        } else if (view.getTag() as Int == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show()
        } else if (view.getTag() as Int == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}