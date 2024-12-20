package stu.cn.ua.lab1androidv2.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import stu.cn.ua.lab1androidv2.R
import android.widget.Toast


class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var secretNumber: String
    private var attempts = 0
    private var difficulty: String = "Easy" // Збереження складності для рестарту гри

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Отримуємо рівень складності з аргументів
        difficulty = arguments?.getString("difficulty") ?: "Easy"
        secretNumber = generateSecretNumber(difficulty)

        val checkButton: Button = view.findViewById(R.id.btnCheck)
        val inputField: EditText = view.findViewById(R.id.inputNumber)
        val attemptsView: TextView = view.findViewById(R.id.attemptsHistory)

        // Обробка кнопки перевірки
        checkButton.setOnClickListener {
            val input = inputField.text.toString()
            if (input.length != secretNumber.length) {
                showToast("Enter ${secretNumber.length}-digit number!")
                return@setOnClickListener
            }

            val result = checkNumber(input, secretNumber)
            attempts++
            attemptsView.append("$attempts: $input -> $result\n")

            if (result.contains("Bulls: ${secretNumber.length}, Cows: 0")) {
                showVictoryDialog()
            }
        }
    }

    // Генерація нового числа
    private fun generateSecretNumber(difficulty: String): String {
        val digits = (0..9).shuffled()
        val length = when (difficulty) {
            "EASY" -> 4
            "MEDIUM" -> 5
            "HARD" -> 6
            else -> 4
        }
        return digits.take(length).joinToString("")
    }

    // Перевірка числа
    private fun checkNumber(input: String, secret: String): String {
        var bulls = 0
        var cows = 0
        val secretUsed = BooleanArray(secret.length)
        val inputUsed = BooleanArray(input.length)

        // Підрахунок Bulls
        for (i in input.indices) {
            if (input[i] == secret[i]) {
                bulls++
                secretUsed[i] = true
                inputUsed[i] = true
            }
        }

        // Підрахунок Cows
        for (i in input.indices) {
            if (!inputUsed[i]) { // Якщо символ ще не використаний
                for (j in secret.indices) {
                    if (!secretUsed[j] && input[i] == secret[j]) {
                        cows++
                        secretUsed[j] = true
                        break // Завершуємо внутрішній цикл, як тільки знаходимо збіг
                    }
                }
            }
        }

        return getString(R.string.bulls_and_cows, bulls, cows)
    }

    // Відображення вікна перемоги
    private fun showVictoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Congratulations!")
        builder.setMessage("You guessed the secret number: $secretNumber\nStart a new game?")
        builder.setCancelable(false)

        builder.setPositiveButton("Restart") { _, _ ->
            restartGame()
        }

        builder.setNegativeButton("Close") { _, _ ->
            requireActivity().finish() // Завершення роботи програми
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Перезапуск гри
    private fun restartGame() {
        secretNumber = generateSecretNumber(difficulty)
        attempts = 0

        // Очищення полів
        val inputField: EditText? = view?.findViewById(R.id.inputNumber)
        val attemptsView: TextView? = view?.findViewById(R.id.attemptsHistory)

        inputField?.text?.clear()
        attemptsView?.text = ""
    }

    // Показ коротких повідомлень
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
