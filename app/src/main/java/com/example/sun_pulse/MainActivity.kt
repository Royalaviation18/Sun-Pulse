package com.example.sun_pulse

import WeatherApp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.sun_pulse.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//4fd9bb5002bd73f12c01588fc61fb2f0

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Vadodara")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.svCityName
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    fetchWeatherData(p0)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        }

        )
    }

    private fun fetchWeatherData(cityName: String) {
        //all other data can change but the baseurl remains same for all the calls
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        //here units is used to convert into string as metric and then perform the search
        val response = retrofit.getWeatherData(cityName,"4fd9bb5002bd73f12c01588fc61fb2f0","metric")
        response.enqueue(object: Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    binding.tvTemperature.text = "$temperature ℃"
                    binding.tvWeatherCondition.text = condition
                    binding.tvDay.text=dayName(System.currentTimeMillis())
                        binding.tvDate.text = date()
                        binding.tvCityName.text = "$cityName"
                    binding.tvMax.text = "$maxTemp ℃"
                    binding.tvMin.text ="$minTemp ℃"
                    binding.tvHumidity.text = "$humidity %"
                    binding.tvWindSpeed.text ="$windSpeed m/s"
                    binding.tvSea.text = "$sealevel"
                    binding.tvSunrise.text ="${time(sunrise)}"
                    binding.tvSunset.text ="${time((sunset))}"

                    changeImageAccWeatherCondition(condition)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeImageAccWeatherCondition(conditions: String) {
        when (conditions){
            "Haze" ->{
                binding.ivIcon.setImageResource(R.drawable.ic_cloudy)
            }
            "Clouds","Partly Clouds","Overcast","Mist","Foggy" ->{
                binding.ivIcon.setImageResource(R.drawable.ic_very_cloudy)
            }
            "Clear Sky","Sunny","Clear" ->{
                binding.ivIcon.setImageResource(R.drawable.ic_sunny)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" ->{
                binding.ivIcon.setImageResource(R.drawable.ic_rainshower)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard" ->{
                binding.ivIcon.setImageResource(R.drawable.ic_snowy)
            }
            else ->{
                binding.ivIcon.setImageResource(R.drawable.ic_sunny)
            }

        }
    }

    private fun date(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format((Date()))
    }

    private fun time(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp: Long): String{
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format((Date()))
    }


}