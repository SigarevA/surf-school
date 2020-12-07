package ru.samsung.itshool.memandos.model.repo

import io.reactivex.rxjava3.core.Observable
import ru.samsung.itshool.memandos.model.api.AndroidSchoolMemesAPI
import ru.samsung.itshool.memandos.model.response.MemResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurfMemesRepo @Inject constructor() {

    @Inject
    lateinit var memesAPI: AndroidSchoolMemesAPI

    fun loadMemes(): Observable<Collection<MemResponse>> {
        return memesAPI.getMems()
    }
}
