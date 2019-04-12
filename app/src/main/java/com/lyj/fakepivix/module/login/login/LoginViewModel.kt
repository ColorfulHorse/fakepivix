package com.lyj.fakepivix.module.login.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.source.UserRepository
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.reactivex.schedulerTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class LoginViewModel : BaseViewModel<ILoginModel>() {

    var keyboardOpened = ObservableField(false)

    // 是否加载完成
    var loginState : ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loginEnable = ObservableField(false)

    @get:Bindable
    var userName = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }

    @get:Bindable
    var password = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    override var mModel: ILoginModel = LoginModel()

    init {
        addOnPropertyChangedCallback(OnPropertyChangedCallbackImp {
            _, id ->
            when(id) {
                BR.userName, BR.password -> {
                    if (userName.isEmpty() or password.isEmpty()) {
                        loginEnable.set(false)
                    }else {
                        loginEnable.set(true)
                    }
                }
            }
        })
    }


    fun login() {
        val disposable = UserRepository.instance
                .login(userName, password)
                .schedulerTransformer()
                .doOnSubscribe { loginState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loginState.set(LoadState.Succeed)
                }, onError = {
                    loginState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

}