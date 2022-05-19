package com.jack.beautygl.utils

import android.content.Context
import com.jack.beautygl.filters.BaseFilter
import com.jack.beautygl.filters.OriginalFilter

class FilterFactory {
    enum class FilterType {
        Original,
        Beauty
    }

    fun createFilter(context: Context, filterType: FilterType): BaseFilter? {
        var baseFilter: BaseFilter? = null

        when(filterType) {
            FilterType.Original -> baseFilter = OriginalFilter(context)
        }

        return baseFilter
    }
}