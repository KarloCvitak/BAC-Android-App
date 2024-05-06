package hr.tvz.android.kalkulatorcvitak

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import java.math.BigDecimal
import java.math.RoundingMode
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hr.tvz.android.kalkulatorcvitak.databinding.MainActivityBinding
import hr.tvz.android.kalkulatorcvitak.ui.theme.KalkulatorCvitakTheme
import kotlin.math.max
import kotlin.math.round

class MainActivity : ComponentActivity()
{

    private lateinit var binding: MainActivityBinding

    var beerQuantity: Int = 0
    var liquorQuantity: Int = 0
    var vinoQuantity: Int = 0

    var satiVrijednost: Int = 0
    var minuteVrijednost: Int = 0
    var bodyWeight = 0

    var velicinaCasePive = 0
    var velicinaCaseVina = 0
    var velicinaCaseLiquor = 0

    var abcBeerValue = 0f
    var abcWineValue = 0f
    var abcLiquorValue = 0f

    override fun onStart() {
        super.onStart()



        setupSpinners()
        setupSeekBar()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)





        var text: String

        binding.calculateButton.setOnClickListener {
            var ok = true




            if (binding.bodyWeight.text.toString().length == 0) {
                // Toast.makeText(this,"nula je", Toast.LENGTH_LONG).show()
                binding.bodyWeight.error = "Body Weight nije unesen"
                ok = false
            }  else {

                bodyWeight = binding.bodyWeight.text.toString().toInt()

            }

            if (binding.minuteInput.text.toString().length == 0 && binding.satiInput.text.toString().length == 0) {
                binding.errorTime.error= "Nije uneseno vrijeme"
                ok = false
            }



            if (ok) {

                val id: Int = binding.radioGroup.checkedRadioButtonId
                val radio:RadioButton = findViewById(id)
                text = radio.text.toString()

                //SPINNERI PART 1



                if (binding.satiInput.text.toString().length == 0) {
                    satiVrijednost = 0
                } else {
                    satiVrijednost = binding.satiInput.text.toString().toInt()

                }


                if (binding.minuteInput.text.toString().length == 0) {
                    minuteVrijednost = 0
                } else {
                    minuteVrijednost = binding.minuteInput.text.toString().toInt()

                }

                // KOMAD ALKOHOLA

                if (binding.beerKolicina.text.toString().length == 0) {
                    beerQuantity = 0
                } else {
                    beerQuantity = binding.beerKolicina.text.toString().toInt()
                }

                if (binding.vinoKolicina.text.toString().length == 0) {
                    vinoQuantity = 0
                } else {
                    vinoQuantity = binding.vinoKolicina.text.toString().toInt()
                }


                if (binding.liquorKolicina.text.toString().length == 0) {
                    liquorQuantity = 0
                } else {
                    liquorQuantity = binding.liquorKolicina.text.toString().toInt()
                }



                println("$beerQuantity, $liquorQuantity, $satiVrijednost, $bodyWeight, $velicinaCaseLiquor, $abcBeerValue, $text")

                val bac = calculateBAC(beerQuantity, liquorQuantity, vinoQuantity, satiVrijednost, minuteVrijednost, bodyWeight, velicinaCasePive, velicinaCaseVina, velicinaCaseLiquor, abcBeerValue, abcWineValue, abcLiquorValue, text
                )

                val bacRounded = BigDecimal(bac).setScale(4, RoundingMode.HALF_EVEN)

                Toast.makeText(this, "BAC: $bacRounded%", Toast.LENGTH_LONG).show()


            }

        }



    }

    fun setupSeekBar(){

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                updateAppDesign(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })


    }


    fun updateAppDesign(progress: Int) {

        println(progress)
        val color = Color.argb(100 , 204 , 251,254 - progress)
        binding.backgroundMain.setBackgroundColor(color)

        val bold = progress > 50
        binding.textView.setTypeface(null, Typeface.BOLD)

    }

     fun setupSpinners(){


         val litraPiva = resources.getStringArray(R.array.litraPive)
         val litraVina = resources.getStringArray(R.array.litraVina)
         val litraLiquor = resources.getStringArray(R.array.litraLiquor)

         val abcBeer = resources.getStringArray(R.array.abcBeer)
         val abcWine = resources.getStringArray(R.array.abcWine)
         val abcLiquor = resources.getStringArray(R.array.abcLiquor)


         val adapterPive = ArrayAdapter(this, android.R.layout.simple_spinner_item, litraPiva)
         adapterPive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.beerSpinner.adapter = adapterPive





         val adapterVina = ArrayAdapter(this, android.R.layout.simple_spinner_item, litraVina)
         adapterVina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.wineSpinner.adapter = adapterVina




         val adapterLiquor = ArrayAdapter(this, android.R.layout.simple_spinner_item, litraLiquor)
         adapterLiquor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.liquorSpinner.adapter = adapterLiquor




         val adapterAbcLiquor = ArrayAdapter(this, android.R.layout.simple_spinner_item, abcLiquor)
         adapterAbcLiquor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.abcLiquor.adapter = adapterAbcLiquor




         val adapterAbcBeer = ArrayAdapter(this, android.R.layout.simple_spinner_item, abcBeer)
         adapterAbcBeer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.abcBeer.adapter = adapterAbcBeer



         val adapterAbcWine = ArrayAdapter(this, android.R.layout.simple_spinner_item, abcWine)
         adapterAbcWine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.abcWine.adapter = adapterAbcWine






             binding.beerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                   override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                       if (position == 0) {
                           velicinaCasePive = 0
                       } else {
                           velicinaCasePive = litraPiva[position].toInt()
                       }
                   }

                   override fun onNothingSelected(parent: AdapterView<*>?) {
                       velicinaCasePive = 0
                   }
               }


              binding.wineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                 if (position == 0) {
                     velicinaCaseVina = 0
                       } else {
                           velicinaCaseVina = litraVina[position].toInt()
                       }
                   }

                   override fun onNothingSelected(parent: AdapterView<*>?) {
                       velicinaCaseVina = 0
                   }
         }


               binding.liquorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                   override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                       if (position == 0) {
                           velicinaCaseLiquor = 0
                       } else {
                           velicinaCaseLiquor = litraLiquor[position].toInt()
                       }
                   }

                   override fun onNothingSelected(parent: AdapterView<*>?) {
                       velicinaCaseLiquor = 0
                   }
            }


            binding.abcWine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == 0) {
                        abcWineValue = 0f
                    } else {
                        abcWineValue = abcWine[position].toFloat()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    abcWineValue = 0f
                }
            }


            binding.abcBeer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == 0) {
                        abcBeerValue = 0f
                    } else {
                        abcBeerValue = abcBeer[position].toFloat()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    abcBeerValue = 0f
                }
            }

            binding.abcLiquor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == 0) {
                        abcLiquorValue = 0f
                    } else {
                        abcLiquorValue = abcLiquor[position].toFloat()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    abcLiquorValue = 0f
                }
            }



     }



}






private fun calculateBAC(beerQuantity: Int, liquorQuantity: Int, vinoQuantity: Int,
                         satiVrijednost: Int, minuteVrijednost: Int, bodyWeight: Int,
                         velicinaCasePive: Int, velicinaCaseVina: Int, velicinaCaseLiquor: Int,
                         abcBeerValue: Float, abcWineValue: Float, abcLiquorValue: Float, text:String): Double {


    val alcoholConsumed = (beerQuantity * velicinaCasePive * (abcBeerValue/100)* 0.789   +
            vinoQuantity * velicinaCaseVina * (abcWineValue/100)* 0.789 +
            liquorQuantity * velicinaCaseLiquor * (abcLiquorValue/100)* 0.789)

    val totalBodyWater = if (text == "MUŠKO") {
        0.68 * bodyWeight
    } else {
        0.55 * bodyWeight
    }

    val alcoholConcentration = (alcoholConsumed / (totalBodyWater * 1000)) * 100

    val hoursElapsed = satiVrijednost + (minuteVrijednost / 60.0)
    val adjustedConcentration = alcoholConcentration - hoursElapsed*0.015

     return max(0.0, adjustedConcentration)

}