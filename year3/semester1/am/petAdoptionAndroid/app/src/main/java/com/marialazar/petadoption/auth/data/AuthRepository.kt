package com.marialazar.petadoption.auth.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.marialazar.petadoption.auth.data.local.TokenDao
import com.marialazar.petadoption.auth.data.remote.RemoteAuthDataSource
import com.marialazar.petadoption.core.Api
import com.marialazar.petadoption.core.Result
import com.marialazar.petadoption.core.TAG

class AuthRepository(private val tokenDao: TokenDao) {
    var token: LiveData<List<TokenHolder>>? = tokenDao.getToken()

    suspend fun logout() {
        token = null
        tokenDao.deleteAll()
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        Log.d(TAG, result.toString());
        if (result is Result.Success<TokenHolder>) {
            tokenDao.insert(result.data)
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
