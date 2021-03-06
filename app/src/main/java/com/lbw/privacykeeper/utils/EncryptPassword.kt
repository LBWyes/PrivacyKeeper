package com.lbw.privacykeeper.utils

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedFile
import com.lbw.privacykeeper.model.Password
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets


//这个类用于加密用户存储的密码
class EncryptPassword(
    val context : Context,
    val mainKeyAlias : String
) {

    fun decrypt(company : String):Password{
        try {
            val file = File(context.filesDir,"passwords")

            val encryptedFile = EncryptedFile.Builder(
                File(file,company),
                context,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val inputStream = encryptedFile.openFileInput()
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte: Int = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }

            val plaintext: ByteArray = byteArrayOutputStream.toByteArray()
            byteArrayOutputStream.close()

            val user =  Utils.StringToUser(plaintext.toString(StandardCharsets.UTF_8))

            return Password(company,user.username,user.password)
        }catch (e : IOException){
            Log.d("error",e.toString())
            return Password("","","")
        }
    }

    fun encrypt(password : Password){
        try {
            val file = File(context.filesDir,"passwords")

            if(!file.exists()){
                file.mkdirs()
            }

            val encryptedFile = EncryptedFile.Builder(
                File(file,password.company),
                context,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            //用空格做区分用户名与密码
            val fileContent = "${password.username} ${password.password}"
                .toByteArray(StandardCharsets.UTF_8)
            encryptedFile.openFileOutput().apply {
                write(fileContent)
                flush()
                close()
            }   
        }catch (e : IOException){
            Log.d("error",e.toString())
        }
        
    }
}