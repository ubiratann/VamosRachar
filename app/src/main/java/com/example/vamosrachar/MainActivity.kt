package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener
{

    lateinit var dividend: EditText
    lateinit var divisor: EditText
    lateinit var quotient: TextView

    lateinit var share: FloatingActionButton
    lateinit var speaker: FloatingActionButton

    lateinit var playerTTS: TextToSpeech;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dividend = findViewById(R.id.dividendEditText)
        divisor = findViewById(R.id.divisorEditText)
        quotient = findViewById(R.id.quotientTextView)

        dividend.addTextChangedListener(watcher)
        divisor.addTextChangedListener(watcher)

        share = findViewById(R.id.shareButton)
        speaker = findViewById(R.id.speakerButton)


        share.setOnClickListener{onClickShare()}
        speaker.setOnClickListener{speakOut()}
        playerTTS = TextToSpeech(this, this )

    }

    private val watcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val mean: Double? = calcMean();
            if (mean != null)
                quotient.text = DecimalFormat("00.00").format(mean).toString()
            else
                quotient.text = null
        }
    }

    private fun onClickShare() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT, ("Você e as outras "+divisor.text.toString()+" pessoas têm uma dívida de " + quotient.text.toString() + " R$ a pagar ':)")
        )
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Compartilhar em:"))
    }

    private fun speakOut() {
        val text: String = "Você e as outras "+divisor.text.toString()+" pessoas têm uma dívida de " + quotient.text.toString() + " R$ a pagar ':)"
        playerTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        Log.println(Log.INFO,"","Entrou")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = playerTTS.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                speaker.isEnabled = true
            }
        }
    }

    fun calcMean(): Double?{
        val dividendDouble: Double? = dividend.text.toString().toDoubleOrNull();
        val divisorInt: Int? = divisor.text.toString().toIntOrNull();
        var result: Double? = null

        if (dividendDouble == null || divisorInt == null) return result

        if ( divisorInt > 0){
            result = dividendDouble.div(divisorInt)
        }

        return result
    }





}


