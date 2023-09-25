package com.example.blujin

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blujin.screens.FirstScreen
import com.example.blujin.screens.profilesetup
import com.example.blujin.screens.signin

import com.example.blujin.ui.theme.BlujinTheme
import com.example.blujin.ui.theme.bg
import kotlinx.coroutines.delay

class MainActivity :ComponentActivity(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            BlujinTheme {
                Surface( modifier = Modifier.fillMaxSize(), color = Color.White) {
                    val context = this // Use the context of the activity
                    navigation(context)

                }


            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun navigation(context: Context) {
        val navController = rememberNavController()

        // Check if it's the first app launch
        val isFirstAppLaunch = remember {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

            // Update the flag for subsequent launches
            if (isFirstLaunch) {
                sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
            }

            isFirstLaunch
        }

        // Determine the initial destination based on whether it's the first launch or not
        val initialDestination = if (isFirstAppLaunch) {
            "splash" // Set the initial destination to "splash" for the first launch
        } else {
            "signin" // Set the initial destination to "signin" for subsequent launches
        }

        NavHost(navController = navController, startDestination = initialDestination) {
            composable("splash") {
                splashscreen(navController = navController)
            }
            composable("firstScreen") {
                FirstScreen(navController = navController)
            }
            composable("signin") {
                signin(navController = navController)
            }
            composable("profile") {
                profilesetup(navController = navController)
            }
        }
    }


    @Composable
    fun splashscreen(navController: NavController){
        val scale= remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = true ){
            scale.animateTo(targetValue = 1f,
                animationSpec =tween( // spring,keyframes,repeatable(tween),infiniteRepeatable(tween),snap
                    durationMillis = 500,
                    easing = {OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(2000)
            navController.navigate("firstScreen")
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = bg), contentAlignment = Alignment.Center){
            Image(painter = painterResource(id = R.drawable.splash), contentDescription = null, modifier = Modifier.scale(scale.value))

        }
    }

}