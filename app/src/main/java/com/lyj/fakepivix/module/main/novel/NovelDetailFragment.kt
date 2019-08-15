package com.lyj.fakepivix.module.main.novel

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.databinding.FragmentNovelDetailBinding
import com.lyj.fakepivix.databinding.NovelChapterFooter

/**
 * @author greensun
 *
 * @date 2019/8/12
 *
 * @desc 小说详情
 */
class NovelDetailFragment : BackFragment<FragmentNovelDetailBinding, NovelDetailViewModel?>() {

    override var mViewModel: NovelDetailViewModel? = null

    private lateinit var mAdapter: BaseBindingAdapter<String, ViewDataBinding>

    private var position = -1
    private var key = -1

    private val footerBinding: NovelChapterFooter by lazy {
        val footerBinding: NovelChapterFooter = DataBindingUtil.inflate(mActivity.layoutInflater, R.layout.footer_novel_series, null, false)
        footerBinding.previous.setOnClickListener {
            // 上一章
            start(newInstance(position, key))
        }

        footerBinding.next.setOnClickListener {
            // 下一章
            start(newInstance(position, key))
        }
        addSubBinding(footerBinding)
        footerBinding
    }

    private val headerBinding: ViewDataBinding by lazy {
        val binding: ViewDataBinding = DataBindingUtil.inflate(mActivity.layoutInflater, R.layout.header_novel_cover, null, false)
        addSubBinding(binding)
        binding
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int, key: Int): NovelDetailFragment {
            return NovelDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            key = it.getInt(EXTRA_KEY, -1)
            mViewModel = NovelDetailViewModel(key, position)
            mViewModel?.let { vm ->
                lifecycle.addObserver(vm)
            }
            mBinding.setVariable(bindViewModel(), mViewModel)
        }

        mToolbar?.let {
            it.inflateMenu(R.menu.menu_detail_novel)
            it.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.bookmark -> {}
                    R.id.share -> {}
                    R.id.chapter -> {}
                    R.id.setting -> {}
                    R.id.save -> {}
                    R.id.filter -> {}
                }
                true
            }
        }
        mViewModel?.let { vm ->
            // 是否有上一章或下一章
            vm.loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
                when(vm.loadState.get()) {
                    is LoadState.Succeed -> {
                        vm.novelText?.let {
                            if (it.series_prev != null || it.series_next != null) {
                                footerBinding.data = it
                                mAdapter.addFooterView(footerBinding.root)
                            }
                        }
                    }
                }
            })

            with(mBinding) {
                val layoutManager = LinearLayoutManager(mActivity)
                recyclerView.layoutManager = layoutManager
                mAdapter = BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_novel_chapter, vm.data, BR.data)
                headerBinding.setVariable(BR.data, vm.illust.image_urls.medium)
                mAdapter.addHeaderView(headerBinding.root)
                mAdapter.bindToRecyclerView(recyclerView)
                mAdapter.bindState(vm.loadState)
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .transparentStatusBar()
                .init()
    }

    override fun bindLayout(): Int = R.layout.fragment_novel_detail
}