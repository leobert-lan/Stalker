package osp.leobert.android.tracker

import com.google.gson.annotations.SerializedName

data class PointConfig(

	@field:SerializedName("data")
	val data: List<PointDataConfig?>? = null,

	@field:SerializedName("point")
	val point: String? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("group")
	val group: String? = null
)

data class PointDataConfig(

	@field:SerializedName("p")
	val P: String? = null,

	@field:SerializedName("n")
	val N: String? = null
)