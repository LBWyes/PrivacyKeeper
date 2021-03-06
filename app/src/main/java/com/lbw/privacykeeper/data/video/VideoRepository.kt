package com.lbw.privacykeeper.data.video

import android.net.Uri

interface VideoRepository {

    suspend fun save(uri:Uri,filename:String)

    suspend fun read(filename: String) : String

    suspend fun readAllFilenames() : List<String>

    suspend fun renameFile(oldFilename: String,newFilename:String)

    suspend fun delete(filename: String)

    suspend fun deleteDecrypted()

}