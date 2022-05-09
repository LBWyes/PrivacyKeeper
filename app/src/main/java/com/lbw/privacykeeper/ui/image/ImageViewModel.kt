package com.lbw.privacykeeper.ui.image

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lbw.privacykeeper.data.image.ImageRepository
import com.lbw.privacykeeper.ui.nav.AppSecondaryScreen
import com.lbw.privacykeeper.utils.BiometricCheck
import com.lbw.privacykeeper.utils.BiometricCheckParameters
import com.lbw.privacykeeper.utils.Utils.Companion.getUriName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImageViewModel(
    private val imageRepository : ImageRepository,
    private val biometricCheckParameters: BiometricCheckParameters,
):ViewModel() {


    fun openBiometricCheck(navController: NavHostController){
        val biometricCheck = BiometricCheck(
            biometricCheckParameters = biometricCheckParameters,
            onSuccess = { navController.navigate(AppSecondaryScreen.Image.route) }
        )
        biometricCheck.launchBiometric()
    }

    private var uri : Uri = Uri.EMPTY

    fun setNewUri(newUri: Uri){
        uri = newUri
    }

    fun saveImage(){
        viewModelScope.launch(Dispatchers.IO) {
            if (uri!= Uri.EMPTY)
                imageRepository.save(uri,getUriName(uri))
        }
    }


    var filenames : List<String> by mutableStateOf<List<String>>(mutableListOf<String>())

    fun getFilenames(){
        viewModelScope.launch {
            filenames = imageRepository.readAllFilenames()
        }
    }

    private var oldFilename : String = ""

    fun setOldFilename(filename: String){
        oldFilename = filename
    }

    fun rename(newFilename : String){
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.renameFile(oldFilename = oldFilename, newFilename = newFilename)
            filenames = imageRepository.readAllFilenames()
        }
    }

    var showDialog by mutableStateOf<Boolean>(false)

    fun openDialog(){
        showDialog = true
    }

    fun closeDialog(){
        showDialog = false
    }

    fun delete(filename: String){
        viewModelScope.launch {
            imageRepository.delete(filename)
            filenames = imageRepository.readAllFilenames()
        }
    }


    companion object{
        fun provideFactory(
            imageRepository: ImageRepository,
            biometricCheckParameters: BiometricCheckParameters
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("main")
            override fun <T : ViewModel> create(modelClass : Class<T>):T{
                return ImageViewModel(imageRepository, biometricCheckParameters) as T
            }
        }
    }
}

class ThirdImageViewModel(
    private val imageRepository : ImageRepository,
):ViewModel(){

    private var imageBitmap : ImageBitmap = ImageBitmap(1,1)

    fun setImageBitmap(filename : String){
        viewModelScope.launch {
            imageBitmap = imageRepository.getImageBitmap(filename)
        }
    }

    fun getImageBitmap():ImageBitmap{
        return imageBitmap
    }

    companion object{
        fun provideFactory(
            imageRepository: ImageRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("main")
            override fun <T : ViewModel> create(modelClass : Class<T>):T{
                return ThirdImageViewModel(imageRepository) as T
            }
        }
    }
}