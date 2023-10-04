package com.ahpp.bebagua

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ahpp.bebagua.databinding.ActivityMainBinding
import com.ahpp.bebagua.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    /*private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resutado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_horas: TextView
    private lateinit var txt_minutos: TextView*/

    private lateinit var calcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoMl = 0f

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendar: Calendar
    var horaAtual = 0
    var minutoAtual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangeSystemBarsTheme(!isSystemInDarkTheme())
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            //iniciarComponentes()
            calcularIngestaoDiaria = CalcularIngestaoDiaria()

            binding.btCalcular.setOnClickListener(){
                val peso_txt = binding.editTextPeso.text.toString()
                val idade_txt = binding.editTextIdade.text.toString()

                if (peso_txt.isEmpty())
                    Toast.makeText(this, R.string.aviso_peso, Toast.LENGTH_SHORT).show()
                else if (idade_txt.toString().isEmpty())
                    Toast.makeText(this, R.string.aviso_idade, Toast.LENGTH_SHORT).show()
                 else {
                    val peso = peso_txt.toFloat()
                    val idade = idade_txt.toInt()
                    resultadoMl = calcularIngestaoDiaria.CalcularTotalMl(peso, idade)
                    val format = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                    format.isGroupingUsed = false
                    binding.txtResultado.text = "${format.format(resultadoMl)} Litros"
                }
            }

            binding.icRedefinir.setOnClickListener(){
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(R.string.dialog_redefinir_dados).setMessage(R.string.dialog_desc)
                alertDialog.setPositiveButton("Ok") {
                        _, _ ->
                    binding.editTextPeso.setText("")
                    binding.editTextIdade.setText("")
                    binding.txtResultado.text = "0,0 Litros"
                    //edit_peso.setText("")
                    //edit_idade.setText("")
                    //txt_resutado_ml.text = "0,0 Litros"
                }
                alertDialog.setNegativeButton("Cancelar") {
                        _, _ ->

                }
                val dialog = alertDialog.create()
                dialog.show()
            }

            binding.btLembrete.setOnClickListener{
                calendar = Calendar.getInstance()
                horaAtual = calendar.get(Calendar.HOUR_OF_DAY)
                minutoAtual = calendar.get(Calendar.MINUTE)
                timePickerDialog = TimePickerDialog(this, {timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                    binding.textHora.text  = String.format("%02d", hourOfDay)
                    binding.textMinutos.text = String.format("%02d", minutes)
                }, horaAtual, minutoAtual, true)
                timePickerDialog.show()
            }

            binding.btAlarme.setOnClickListener{
                val txt_hora = binding.textHora.text.toString()
                val txt_minutos = binding.textMinutos.text.toString()
                if (!txt_hora.isEmpty() && !txt_minutos.isEmpty()){
                    val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                    intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.toInt())
                    intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.toInt())
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.mensagem_alarme))
                    startActivity(intent)

                    if (intent.resolveActivity(packageManager) != null){
                        startActivity(intent)
                    }
                }
            }
        }
    }

    /*private fun iniciarComponentes(){
        edit_peso = findViewById(R.id.editTextPeso)
        edit_idade = findViewById(R.id.editTextIdade)
        bt_calcular = findViewById(R.id.bt_calcular)
        txt_resutado_ml = findViewById(R.id.txt_resultado)
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        bt_lembrete = findViewById(R.id.bt_lembrete)
        bt_alarme = findViewById(R.id.bt_alarme)
        txt_horas = findViewById(R.id.text_hora)
        txt_minutos = findViewById(R.id.text_minutos)
    }*/

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val mycollor = Color.Transparent
        if (lightTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(mycollor.toArgb(), mycollor.toArgb()),
                navigationBarStyle = SystemBarStyle.light(mycollor.toArgb(), mycollor.toArgb()))
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(mycollor.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(Color.Black.toArgb()))
        }
    }

}