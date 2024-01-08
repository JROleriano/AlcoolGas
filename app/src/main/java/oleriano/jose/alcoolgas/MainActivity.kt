package oleriano.jose.alcoolgas

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import oleriano.jose.alcoolgas.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalcular.setOnClickListener {
            calcularVantagem()
        }

        binding.btnLimpar.setOnClickListener {
            limparCampos()
        }

        setupEditText(binding.exPrecoGasolina)
        setupEditText(binding.exPrecoAlcool)
    }

    private fun limparCampos() {
        binding.apply {
            exPrecoGasolina.text.clear()
            exPrecoAlcool.text.clear()
            textView4.text = getString(R.string.txt_resultado)
            gifPosto.visibility = View.INVISIBLE
            btnLimpar.visibility = View.INVISIBLE

            exPrecoGasolina.clearFocus()
            exPrecoAlcool.clearFocus()
        }
    }

    private fun setupEditText(editText: EditText) {
    // Configuração do EditText para aceitar apenas números e ponto decimal
        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            if (source != null) {
                val newText = editText.text.toString() + source.toString()
                try {
                    newText.toDouble()
                    null
                } catch (e: NumberFormatException) {
                    ""
                }
            } else {
                null
            }
        })

        editText.addTextChangedListener(createTextWatcher(editText))
    }

    private fun createTextWatcher(editText: EditText) = object : TextWatcher {
        // Implementação do TextWatcher para formatar o texto ao ser digitado
        override fun afterTextChanged(s: Editable?) {
            val text = s?.toString()?.replace(',', '.') ?: ""
            if (text != s.toString()) {
                editText.setText(text)
                editText.setSelection(text.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private fun exibirResultado(vantagemResultante: Double) {
        binding.apply {
            gifPosto.visibility = View.VISIBLE
            btnLimpar.visibility = View.VISIBLE

            textView4.text = when {
                vantagemResultante < 0.7 -> getString(R.string.resultado_alcool_vantajoso)
                else -> getString(R.string.resultado_gasolina_vantajosa)
            }
        }
    }

    private fun calcularVantagem() {
        val precoGasolina = binding.exPrecoGasolina.text.toString().replace(',', '.').toDoubleOrNull()
        val precoAlcool = binding.exPrecoAlcool.text.toString().replace(',', '.').toDoubleOrNull()

        if (precoGasolina != null && precoAlcool != null) {
            val vantagemResultante = precoAlcool / precoGasolina
            exibirResultado(vantagemResultante)

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } else {
            binding.textView4.text = getString(R.string.resultado_invalido)
        }
    }


}