package com.example.paging3.Di

import com.example.paging3.Retrofit.QuoteApi
import com.example.paging3.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    // Singleton ->
//    for examples :
//    Functions can be singletons just like classes , It will assure only one instance will be created .
//    Normally multiple calls of same function will be stacked , and performed one by one , while in a
//    singleton function , last call will overwrite the previous one .
//    so if you called it 10 times in a loop , no instances of the calls will be stacked in the memory.

//    For example in your case each time you request a String a new string is going to be created.

    @Singleton
    @Provides  // provides -> give the Object which user want.
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun getQuoteAPI(retrofit: Retrofit): QuoteApi{
        return retrofit.create(QuoteApi::class.java)
    }
}