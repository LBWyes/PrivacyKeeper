package com.lbw.privacykeeper.data.password.impl

import android.content.Context
import com.lbw.privacykeeper.data.password.PasswordRepository
import com.lbw.privacykeeper.model.Password
import com.lbw.privacykeeper.utils.EncryptPassword
import com.lbw.privacykeeper.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImplPasswordRepository(
    val context : Context,
    mainKeyAlias : String
) : PasswordRepository{

    private val encrypt = EncryptPassword(context = context,mainKeyAlias = mainKeyAlias)

    private val file = File(context.filesDir,"passwords")

    override suspend fun save(password: Password) {
        return withContext(Dispatchers.IO){
            encrypt.encrypt(password = password)
        }
    }

    override suspend fun read(company:String): Password{
        return withContext(Dispatchers.IO) {
            encrypt.decrypt(company = company)
        }
    }

    override suspend fun readAll(): List<Password> {
        return withContext(Dispatchers.IO){
            val list = mutableListOf<Password>()
            Utils.getAllFileNames(file).forEach {
                list.add(read(it))
            }
            list
        }
    }

    override suspend fun delete(filename: String) {
        return withContext(Dispatchers.IO){
            File(file,filename).delete()
        }
    }

}