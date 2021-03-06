package com.example.bolovanje.ui.employers.allEmployers

import com.example.bolovanje.model.Employer
import com.example.bolovanje.model.FirebaseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AllEmployersPresenter : AllEmployersContract.Presenter {

    private lateinit var view: AllEmployersContract.View
    private var compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var employers: Employer

    override fun loadData() {

        compositeDisposable.add(
            FirebaseRepository.readAllData()
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.first.isNotEmpty()) {
                        Pair(it.first as ArrayList<Employer>, it.second as ArrayList<String>)
                    } else {
                        Pair(arrayListOf(), arrayListOf())
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //sort by num of days first
                        if (it.first.isNotEmpty()) {
                            it.first.sortBy {
                                it.lastName
                            }
                            view.showProgressBar(false)
                            view.showData(it.first)
                        } else {
                            view.showProgressBar(false)
                            view.showData(it.first)
                        }

                    }, { error ->
                        view.showErrorMessage(error.toString())
                        view.showProgressBar(true)
                    })
        )
    }

    override fun resetDatesForNewMonth() {
        FirebaseRepository.resetDatesForNewMonth()
    }

    override fun attach(view: AllEmployersContract.View) {
        this.view = view
    }

    override fun destroy() {
        compositeDisposable.dispose()
    }
}