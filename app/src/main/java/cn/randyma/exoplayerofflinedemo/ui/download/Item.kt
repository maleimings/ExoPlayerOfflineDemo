package cn.randyma.exoplayerofflinedemo.ui.download

data class Item(val url: String,
                val drmLicenseUrl: String = "",
                val token: String = "",
                val mimetype: String)
