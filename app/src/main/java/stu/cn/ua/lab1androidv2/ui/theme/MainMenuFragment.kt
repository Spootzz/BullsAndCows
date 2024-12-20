package stu.cn.ua.lab1androidv2.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import stu.cn.ua.lab1androidv2.R

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startGameButton: Button = view.findViewById(R.id.btnStartGame)
        val aboutButton: Button = view.findViewById(R.id.btnAbout)
        val exitButton: Button = view.findViewById(R.id.btnExit)
        val difficultySpinner: Spinner = view.findViewById(R.id.spinnerDifficulty)

        startGameButton.setOnClickListener {
            val difficulty = difficultySpinner.selectedItem.toString()
            val bundle = Bundle().apply { putString("difficulty", difficulty) }
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment, bundle)
        }

        aboutButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_aboutFragment)
        }

        exitButton.setOnClickListener {
            requireActivity().finish()
        }
    }
}
