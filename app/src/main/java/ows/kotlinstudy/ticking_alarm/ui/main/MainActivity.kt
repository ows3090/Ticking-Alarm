package ows.kotlinstudy.ticking_alarm.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ows.kotlinstudy.ticking_alarm.R
import ows.kotlinstudy.ticking_alarm.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}
