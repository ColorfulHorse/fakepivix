package com.lyj.fakepivix.module.illust.ranking

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App.Companion.context
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.ItemTagBinding
import com.lyj.fakepivix.module.common.adapter.IllustAdapter

/**
 * @author greensun
 *
 * @date 2019/9/4
 *
 * @desc 排行榜
 */
class RankingAdapter(data: MutableList<Illust>, likeButton: Boolean = true) : IllustAdapter(data, likeButton) {

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || (type and Illust.TYPE_RANK == Illust.TYPE_RANK)
    }

    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: Illust) {
        super.convert(helper, item)
        if (helper.layoutPosition <= 2) {
            var margin = 6.dp2px()
            var b = 0
            if (helper.layoutPosition == 2) {
                b = margin
            }
            val lp = helper.itemView.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(margin, margin, margin, b)
            helper.itemView.layoutParams = lp
        }
        if(item.itemType == Illust.TYPE_RANK + Illust.TYPE_NOVEL) {
            helper.addOnClickListener(R.id.series_title)
            val data = item.tags.toMutableList()
            val recyclerView = helper.getView<RecyclerView>(R.id.rv_tag)
            var adapter = recyclerView.adapter
            if (adapter == null) {
                adapter = BaseBindingAdapter<Tag, ItemTagBinding>(R.layout.item_detail_tag, data, BR.data)
                recyclerView.layoutManager = ChipsLayoutManager.newBuilder(context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER)
                        .build()
                recyclerView.adapter = adapter
            }else {
                (adapter as BaseQuickAdapter<Tag, *>).setNewData(data)
            }
        }
    }
}