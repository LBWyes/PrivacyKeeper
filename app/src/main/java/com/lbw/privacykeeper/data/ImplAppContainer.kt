package com.lbw.privacykeeper.data

import android.content.Context
import com.lbw.privacykeeper.data.image.ImageRepository
import com.lbw.privacykeeper.data.image.impl.ImplImageRepository
import com.lbw.privacykeeper.data.password.PasswordRepository
import com.lbw.privacykeeper.data.password.impl.ImplPasswordRepository
import com.lbw.privacykeeper.data.preference.PreferenceRepository
import com.lbw.privacykeeper.data.preference.impl.ImplPreferenceRepository
import com.lbw.privacykeeper.data.video.VideoRepository
import com.lbw.privacykeeper.data.video.impl.ImplVideoRepository

//实现依赖注入

interface AppContainer{
    val preferenceRepository : PreferenceRepository
    val passwordRepository : PasswordRepository
    val imageRepository : ImageRepository
    val videoRepository : VideoRepository

}

class ImplAppContainer(
    private val applicationContext: Context,
    private val mainKeyAlias : String
):AppContainer {

    override val preferenceRepository : PreferenceRepository by lazy{
        ImplPreferenceRepository(context = applicationContext)
    }
    override val passwordRepository: PasswordRepository by lazy{
        ImplPasswordRepository(context = applicationContext, mainKeyAlias = mainKeyAlias)
    }

    override val imageRepository:ImageRepository by lazy{
        ImplImageRepository(context = applicationContext, mainKeyAlias = mainKeyAlias)
    }

    override val videoRepository: VideoRepository by lazy{
        ImplVideoRepository(context = applicationContext, mainKeyAlias = mainKeyAlias)
    }

}