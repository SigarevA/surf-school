package ru.samsung.itshool.memandos.ui.VM

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.samsung.itshool.memandos.model.repo.SurfAuthorizationRepo
import javax.inject.Inject

private const val TAG = "TapeVM"

class TapeVM : ViewModel() {

    @Inject
    lateinit var authRepo: SurfAuthorizationRepo

    fun logout(): LiveData<Result<Unit>> {
        val resp = MutableLiveData<Result<Unit>>()
        authRepo.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resp.value = Result.success(Unit)
            },
                {
                    resp.value = Result.failure(it)
                    Log.d(TAG, "error : ${it}")
                })
        return resp
    }
}