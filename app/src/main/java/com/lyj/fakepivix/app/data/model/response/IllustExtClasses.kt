package com.lyj.fakepivix.app.data.model.response

import android.databinding.BaseObservable
import com.lyj.fakepivix.BR
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * @author greensun
 *
 * @date 2019/5/28
 *
 * @desc illust详情
 */

/**
 * 评论
 */
@JsonClass(generateAdapter = true)
data class CommentListResp(
    val comments: List<Comment> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
data class Comment(
    val comment: String = "",
    val date: String = "",
    val has_replies: Boolean = false,
    val id: Int = 0,
    val user: User = User()
)

@JsonClass(generateAdapter = true)
data class BookMarkResp(
    val bookmark_detail: BookmarkDetail = BookmarkDetail()
)

@JsonClass(generateAdapter = true)
data class BookmarkDetail(
    val is_bookmarked: Boolean = false,
    val tags: List<Tag> = listOf(),
    val restrict: String = ""
)

/**
 * 收藏标签
 */
@JsonClass(generateAdapter = true)
data class BookmarkTags(
    val bookmark_tags: List<BookmarkTag> = listOf(),
    val next_url: String = ""
)

data class BookmarkTag(
    val name: String = "",
    val count: Int = 0
): BaseObservable() {
    var selected: Boolean = false
    set(value) {
        field = value
        notifyPropertyChanged(BR.selected)
    }
}

/**
 * 作品系列
 */
@JsonClass(generateAdapter = true)
data class SeriesExt(
    @Json(name = "illust_series_detail")
    val detail: SeriesDetail = SeriesDetail(),
    @Json(name = "illust_series_first_illust")
    val first: Illust = Illust(),
    val illusts: List<Illust> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
data class SeriesDetail(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    @Json(name = "cover_image_urls")
    val imageUrls: ImageUrls = ImageUrls(),
    val series_work_count: Int = 0,
    val create_date: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val user: User = User()
)

/**
 * 当前章节在系列中上下文
 */
data class SeriesContextResp(
    @Json(name = "illust_series_context")
    val context: SeriesContext = SeriesContext()
)

@JsonClass(generateAdapter = true)
data class SeriesContext(
    val content_order: Int = 0,
    val prev: Illust? = null,
    val next: Illust? = null
)
