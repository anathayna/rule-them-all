package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.fetter.rulethemall.R
import kotlinx.android.synthetic.main.activity_home_list.*

class HomeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_list)

        btnTest.setOnClickListener {
            val i = Intent(this, StoreActivity::class.java)
            startActivity(i)
        }
    }
}