package ru.samsung.itshool.memandos.ui.VM

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.samsung.itshool.memandos.domain.Mem
import ru.samsung.itshool.memandos.model.repo.MemDatabase
import ru.samsung.itshool.memandos.model.repo.NetworkService
import ru.samsung.itshool.memandos.model.repo.SurfMemesRepo

class ProfileVM : ViewModel() {

    private val surfMemesRepo: SurfMemesRepo = SurfMemesRepo()

    fun getMyMemes(context: Context): LiveData<List<Mem>> {

        val memesLiveData = MutableLiveData<List<Mem>>()

        val mDataBase = MemDatabase.getInstance(context)
        mDataBase.memDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    memesLiveData.value = it
                },
                {
                    Log.d(TAG, it.message)
                }
            )

        return memesLiveData
    }

    companion object {
        private val TAG = RobbionVM::class.java.name
    }
}