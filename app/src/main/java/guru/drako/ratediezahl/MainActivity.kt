package guru.drako.ratediezahl

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var searchedNumber = generateNewNumber()
    private var guesses = 0

    private fun generateNewNumber() = (1..100).random()

    private fun displayResultMessage(@StringRes messageId: Int, vararg values: Any) {
        resultText.text = getString(messageId, *values)
        resultText.hint = "$messageId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tryButton.setOnClickListener {
            val number = numberInput.text.toString().toInt()
            ++guesses
            when {
                number == searchedNumber -> {
                    displayResultMessage(R.string.toast_correct, guesses)
                    searchedNumber = generateNewNumber()
                    guesses = 0
                }
                number < searchedNumber -> displayResultMessage(R.string.toast_bigger)
                number > searchedNumber -> displayResultMessage(R.string.toast_less)
            }
        }
    }
}
