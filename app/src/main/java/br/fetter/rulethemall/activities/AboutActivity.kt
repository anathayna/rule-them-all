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

        email1.setOnClickListener {
            val id = 1
            sendEmail(id)
        }
        email2.setOnClickListener {
            val id = 2
            sendEmail(id)
        }
        email2.setOnClickListener {
            val id = 3
            sendEmail(id)
        }
    }

    fun visitURL() {
        val endereco = "https://www.sp.senac.br"
        Uri.parse(endereco)
        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(endereco))
        startActivity(intent)
    }

    fun sendEmail(id: Int) {
        if (id == 1) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_EMAIL, "anathaynafranca@gmail.com")
                putExtra(Intent.EXTRA_TEXT, "Tenho uma sugestão para o app")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        } else if (id == 2) {

        } else {

        }
    }
}

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse("mailto:" + url));
startActivity(intent);