package com.example.chessmate.ui.utils.graphics

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import androidx.compose.ui.graphics.Color


@SuppressLint("ViewConstructor")
class GLSurfaceOpenGL(context: Context, attrs: AttributeSet, c : Color, cc : Color) : GLSurfaceView(context, attrs) {

    private val renderer: CubeRendererOpenGL

    init {
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8 , 8, 8, 8, 16, 4);

        renderer = CubeRendererOpenGL(context, c)
        setRenderer(renderer)

        renderMode = RENDERMODE_CONTINUOUSLY
    }



}