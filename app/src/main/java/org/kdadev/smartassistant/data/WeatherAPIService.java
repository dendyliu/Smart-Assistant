package org.kdadev.smartassistant.data;

import org.kdadev.smartassistant.model.WeatherForecast;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Albert on 25/2/2017.
 */

public interface WeatherAPIService {
    String baseURL = "http://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Call<WeatherForecast> getWeather(@Query("lat") double lat, @Query("lon") double lon,
                                     @Query("units") String units, @Query("appid") String apikey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    WeatherAPIService service = retrofit.create(WeatherAPIService.class);

}
