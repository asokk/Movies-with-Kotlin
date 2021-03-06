package com.lenz.oliver.movieswithkotlin.ui.recommendations

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.lenz.oliver.movieswithkotlin.repository.Repository
import com.lenz.oliver.movieswithkotlin.repository.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecommendationsViewModel
@Inject constructor(private val repository: Repository) : ViewModel() {

    private var recommendationsLiveData = MutableLiveData<List<Movie>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun getRecommendationsLiveData(movie: Movie?): LiveData<List<Movie>>? {
        if (movie?.id == null) {
            return null
        }

        compositeDisposable.add(
                repository.getRecommendations(movie.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(
                                onNext = {
                                    recommendationsLiveData.value = it.results
                                }
                        )
        )

        return recommendationsLiveData
    }

}