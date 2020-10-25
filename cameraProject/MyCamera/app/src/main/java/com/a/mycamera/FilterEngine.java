package com.a.mycamera;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FilterEngine {

    private static final float[] vertexData = {
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
    };

    private Context mContext;
    private FloatBuffer mBuffer;
    private int mOESTextureId;
    private int mShaderProgram;

    private int aPosition = -1;
    private int aTextureCoord = -1;
    private int uTextureMatrix = -1;
    private int uTextureSampler = -1;

    public FilterEngine(int OESTextureId, Context context) {
        mContext = context;
        mOESTextureId = OESTextureId;
        mBuffer = createBuffer(vertexData);
        mShaderProgram = linkProgram();
    }

    public FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    private int loadShader(int type, String shaderSource) {
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Create Shader Failed!" + GLES20.glGetError());
        }
        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private int linkProgram() {
        int vertexShader = loadShader(
            GLES20.GL_VERTEX_SHADER, Utils.readShaderFromResource(mContext, R.raw.base_vertex_shader));
        int fragmentShader = loadShader(
            GLES20.GL_FRAGMENT_SHADER, Utils.readShaderFromResource(mContext, R.raw.base_fragment_shader));

        int program = GLES20.glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Create Program Failed!" + GLES20.glGetError());
        }
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);
        return program;
    }

    public void drawTexture(float[] transformMatrix) {
        aPosition = GLES20.glGetAttribLocation(mShaderProgram, "aPosition");
        aTextureCoord = GLES20.glGetAttribLocation(mShaderProgram, "aTextureCoord");
        uTextureMatrix = GLES20.glGetUniformLocation(mShaderProgram, "uTextureMatrix");
        uTextureSampler = GLES20.glGetUniformLocation(mShaderProgram, "uTextureSampler");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId);
        GLES20.glUniform1i(uTextureSampler, 0);
        GLES20.glUniformMatrix4fv(uTextureMatrix, 1, false, transformMatrix, 0);

        if (mBuffer != null) {
            mBuffer.position(0);
            GLES20.glEnableVertexAttribArray(aPosition);
            GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 16, mBuffer);

            mBuffer.position(2);
            GLES20.glEnableVertexAttribArray(aTextureCoord);
            GLES20.glVertexAttribPointer(aTextureCoord, 2, GLES20.GL_FLOAT, false, 16, mBuffer);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        }
    }
}

