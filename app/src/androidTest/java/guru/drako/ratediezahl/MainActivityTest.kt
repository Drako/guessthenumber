package guru.drako.ratediezahl

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    enum class Result {
        Correct,
        Less,
        Bigger
    }

    /**
     * Enter [number] as a guess and hit the button.
     *
     * @param number Our guess.
     * @return The result.
     */
    private fun tryNumber(number: Int): Result {
        onView(withId(R.id.numberInput))
            .perform(
                clearText(),
                typeText("$number")
            )

        onView(withId(R.id.tryButton))
            .perform(ViewActions.click())

        lateinit var result: Result
        onView(withId(R.id.resultText))
            .check { view, _ ->
                result = when ((view as TextView).hint.toString().toInt()) {
                    R.string.toast_correct -> Result.Correct
                    R.string.toast_less -> Result.Less
                    R.string.toast_bigger -> Result.Bigger
                    else -> throw IllegalStateException("Unexpected result!")
                }
            }

        return result
    }

    private fun mockSearchedNumber(value: Int) {
        MainActivity::class.java
            .getDeclaredField("searchedNumber")
            .apply {
                isAccessible = true
            }
            .set(activityRule.activity, value)
    }

    @Test
    fun gameShouldWork() {
        repeat(100) {
            mockSearchedNumber(it + 1)
            playTheGame()
        }
    }

    private fun playTheGame() {
        var found = false
        var guesses = 0
        var range = 1..100

        while (!found) {
            ++guesses
            val middle = (range.endInclusive - range.start) / 2 + range.start
            when (tryNumber(middle)) {
                Result.Correct -> found = true
                Result.Less -> range = range.first until middle
                Result.Bigger -> range = (middle + 1)..range.endInclusive
            }
        }

        assertTrue(guesses <= 7)
    }
}
