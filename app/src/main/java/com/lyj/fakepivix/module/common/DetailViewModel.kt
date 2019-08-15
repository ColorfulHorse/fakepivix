package com.lyj.fakepivix.module.common

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.ImageUrls
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.module.main.illust.*
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/8/14
 *
 * @desc 小说插画详情
 */
open class DetailViewModel(val key: Int, val position: Int) : BaseViewModel<IModel?>() {

    override val mModel: IModel? = null

    val illust = IllustRepository.instance[key][position]

    open var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var followState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var total = ObservableField(0)
    var current = ObservableField(1)
    var toolbarVisibility = ObservableField(true)
    // 悬浮标题是否显示
    var captionVisibility = ObservableField(false)

    //var star = ObservableField(false)

    val userFooterViewModel = UserFooterViewModel(this)
    val commentFooterViewModel = CommentFooterViewModel(this)
    val relatedIllustViewModel = RelatedIllustDialogViewModel(this)
    val relatedUserViewModel = RelatedUserDialogViewModel(this)

    init {
        this + userFooterViewModel + commentFooterViewModel + relatedIllustViewModel + relatedIllustViewModel
        starState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = starState.get()
            if (state is LoadState.Succeed) {
                val star = illust.is_bookmarked
                if (star) {
                    // 收藏成功加载弹出窗数据
                    relatedIllustViewModel.load()
                }
            }
        })
        followState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = followState.get()
            if (state is LoadState.Succeed) {
                illust.user.let {
                    if (it.is_followed) {
                        // 关注成功加载弹出窗数据
                        relatedUserViewModel.load()
                    }
                }
            }
        })
    }

//    fun showAboutDialog() {
//        val fm = Router.getTopFragmentManager()
//        fm?.let {
//            val dialog = AboutDialogFragment.newInstance().apply {
//                detailViewModel = this@DetailViewModel
//            }
//            dialog.show(it, "BottomDialogFragment")
//        }
//    }


    /**
     * 收藏/取消收藏
     */
    fun star() {
        val disposable = IllustRepository.instance
                .star(illust, starState)
        disposable?.let {
            addDisposable(it)
        }
    }


    /**
     * 关注/取消关注
     */
    fun follow() {
        val disposable = UserRepository.instance
                .follow(illust.user, followState)
        disposable?.let {
            addDisposable(it)
        }
    }

}