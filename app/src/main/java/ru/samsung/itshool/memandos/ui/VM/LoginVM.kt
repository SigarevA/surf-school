package ru.samsung.itshool.memandos.ui.VM

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.samsung.itshool.memandos.model.repo.SurfAuthorizationRepo
import ru.samsung.itshool.memandos.model.response.AuthRequest
import ru.samsung.itshool.memandos.storage.UserStorageSharedPref
import javax.inject.Inject


class LoginVM(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var surfAuthRepo: SurfAuthorizationRepo
    private val compositeDisposable = CompositeDisposable()
    private val TAG: String = LoginVM::class.java.name

    fun autheraziton(login: String, password: String): LiveData<Result<Unit>> {
        Log.d(TAG, "autheraziton")

        val authLiveData = MutableLiveData<Result<Unit>>()

        val dispose = surfAuthRepo.authorize(
            AuthRequest(
                login,
                password
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.convert() }
            .subscribe(
                {
                    UserStorageSharedPref.saveUserData(it.userInfo, getApplication())
                    authLiveData.value = Result.success(Unit)
                },
                {
                    authLiveData.value = Result.failure(it)
                }
            )
        compositeDisposable.add(dispose)
        return authLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}