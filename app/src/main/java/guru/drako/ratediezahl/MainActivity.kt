package guru.drako.ratediezahl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private var searchedNumber = generateNewNumber()
    private var guesses = 0

    private fun generateNewNumber() = (1..100).random()

    private fun showToast(@StringRes messageId: Int, vararg values: Any) {
        val message = getString(messageId, *values)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tryButton.setOnClickListener {
            val number = numberInput.text.toString().toInt()
            ++guesses
            when {
                number == searchedNumber -> {
                    showToast(R.string.toast_correct, guesses)
                    searchedNumber = generateNewNumber()
                    guesses = 0
                }
                number < searchedNumber -> showToast(R.string.toast_bigger)
                number > searchedNumber -> showToast(R.string.toast_less)
            }
        }
    }
}
