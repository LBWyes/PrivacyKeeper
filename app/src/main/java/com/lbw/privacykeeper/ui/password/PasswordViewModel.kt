package com.lbw.privacykeeper.ui.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lbw.privacykeeper.data.password.PasswordRepository
import com.lbw.privacykeeper.model.Password
import com.lbw.privacykeeper.ui.nav.AppSecondaryScreen
import com.lbw.privacykeeper.utils.BiometricCheck
import com.lbw.privacykeeper.utils.BiometricCheckParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val passwordRepository: PasswordRepository,
    private val biometricCheckParameters: BiometricCheckParameters,
) : ViewModel(){

    fun openBiometricCheck(navController: NavHostController){
        val biometricCheck = BiometricCheck(
            biometricCheckParameters = biometricCheckParameters,
            onSuccess = { navController.navigate(AppSecondaryScreen.Password.route) }
        )
        biometricCheck.launchBiometric()
    }

    var showSavePasswordDialog by mutableStateOf(false)

    fun openDialog(){
        showSavePasswordDialog = true
    }

    fun closeDialog(){
        showSavePasswordDialog = false
    }

    fun savePassword(password : Password){
        viewModelScope.launch(Dispatchers.IO) {
            passwordRepository.save(password = password)
        }
    }


    var passwordList by mutableStateOf<List<Password>>(mutableListOf())

    fun readAllPassword(){
        viewModelScope.launch(Dispatchers.IO) {
            passwordList = passwordRepository.readAll()
        }
    }


    companion object{
        fun provideFactory(
            passwordRepository: PasswordRepository,
            biometricCheckParameters: BiometricCheckParameters,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("main")
            override fun <T : ViewModel> create(modelClass : Class<T>):T{
                return PasswordViewModel(passwordRepository, biometricCheckParameters) as T
            }
        }
    }
}

