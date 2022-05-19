package com.jack.beautygl.filters

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

abstract class BaseFilter(private val context: Context) {
    private val vertices = floatArrayOf(
        -1.0f, -1.0f, // bottom left
        1.0f, -1.0f, // bottom right
        -1.0f, 1.0f, // top left
        1.0f, 1.0f // top right
    )

    private val textureVertices = floatArrayOf(
        0.0f, 0.0f, // bottom left
        1.0f, 0.0f, // bottom right
        0.0f, 1.0f, // top left
        1.0f, 1.0f // top right
    )

    private var textureBuffer: FloatBuffer
    private var vertexBuffer: FloatBuffer
    private var program = 0
    protected var vPosition = 0 // vertex coordinate
    protected var vCoord = 0 // texture coordinate
    protected var vMatrix = 0
    protected var vTexture = 0

    var vertexShaderPath = 0 // locate in raw resource path
    var fragmentShaderPath = 0 // locate in raw resource path

    init {
        vertexBuffer = createBuffer(vertices)
        textureBuffer = createBuffer(textureVertices)
        setPath()
    }

    abstract fun setPath()

    private fun createBuffer(vertexData: FloatArray): FloatBuffer {
        val floatBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder()) // big endian -> little endian
            .asFloatBuffer()

        floatBuffer.put(vertexData)
        floatBuffer.position(0)

        return floatBuffer
    }

    fun createProgram() {
        val vertexShaderCode = readRawTextFile(context, vertexShaderPath)
        val fragmentShaderCode = readRawTextFile(context, fragmentShaderPath)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram() // create empty OpenGL ES program
        GLES20.glAttachShader(program, vertexShader) // add vertex shader to program
        GLES20.glAttachShader(program, fragmentShader) // add fragment shader to program
        GLES20.glLinkProgram(program) // create OpenGL ES program executables

        if (program == 0) {
            throw RuntimeException("Unable to create program")
        }
        Log.d(TAG, "OpenGL ES program created")

        vPosition = GLES20.glGetAttribLocation(program, "vPosition")
        vCoord = GLES20.glGetAttribLocation(program, "vCoord")
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix")
        vTexture = GLES20.glGetUniformLocation(program, "vTexture")
    }

    fun draw(textureId: Int, mtx: FloatArray) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        GLES20.glUseProgram(program)

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glEnableVertexAttribArray(vPosition) // data transfered from CPU to GPU

        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        // if textureId != NOTEXTURE
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glUniform1i(vTexture, 0)

        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0)

        // notify to draw
        // onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        // onDrawArraysAfter()
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0) // unbind
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)

        // add shaderCode to shader and compile it
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        return shader
    }

    private fun readRawTextFile(context: Context, rawId: Int): String {
        val code = context.resources.openRawResource(rawId)
            .bufferedReader()
            .use {
                it.readText()
            }
        Log.d(TAG, "readRawTextFile: " + code)

        return code
    }

    open fun bindTexture(): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        return texture[0]
    }


    companion object {
        private val TAG = BaseFilter::class.java.simpleName
    }
}