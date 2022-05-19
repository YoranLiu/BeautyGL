package com.jack.beautygl.filters

import android.content.Context
import com.jack.beautygl.R

class OriginalFilter(context: Context): BaseFilter(context) {
    override fun setPath() {
        vertexShaderPath = R.raw.base_vertex_shader
        fragmentShaderPath = R.raw.base_fragment_shader
    }
}