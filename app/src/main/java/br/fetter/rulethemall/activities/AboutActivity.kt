package br.fetter.rulethemall.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.fetter.rulethemall.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        btnSenac.setOnClickListener {
            visitURL()
        }
    }

    fun visitURL() {
        val endereco = "https://www.sp.senac.br"
        Uri.parse(endereco)
        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(endereco))
        startActivity(intent)
    }
}