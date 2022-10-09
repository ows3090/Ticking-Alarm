package ows.kotlinstudy.ticking_alarm.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ows.kotlinstudy.ticking_alarm.R
import ows.kotlinstudy.ticking_alarm.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View<MainPresenter> {
    private lateinit var binding: ActivityMainBinding
    @Inject override lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onCreate(this)
    }

}
