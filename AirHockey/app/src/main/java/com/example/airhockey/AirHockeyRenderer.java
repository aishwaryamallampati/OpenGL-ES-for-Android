package com.example.airhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.airhockey.util.ShaderHelper;
import com.example.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRenderer";
    private static final int POSITION_COMPONENT_COUNT = 2; // Two floating point numbers per vertex: (x_position, y_position)
    private static final int BYTES_PER_FLOAT = 4; // In JAVA, float has 32 bits of precision
    /**
     * Java code runs on Dalvik virtual machine which uses automatic garbage collection.
     * OpenGL runs directly on top of the hardware as native system library which has no garbage collection
     * So, data needs to be copied from Dalvik env to native env
     */
    private FloatBuffer vertexData; // floatbuffer will be used to to store data in native memory
    private final Context context;
    private int program;

    // Getting the position of uniforms
    private static final String U_COLOR = "u_Color";
    private int uColorLocation; // A uniform location is unique to a program object
    // Getting the position of attribute
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    public AirHockeyRenderer(Context context) {
        this.context = context;
        // creating a rectangular table
        float[] tableVerticesWithTriagles = {
                // we can only draw points, lines and triangles in OpenGL
                // here the vertices are ordered in counterclockwise order also known as winding order
                // If we use winding order everywhere, we can tell if a triangle belongs to front or back of an object
                // and discard triangles that are back of a given object - culling
                // a small f is added after every number to inform compiler to interpret the value as float instead of double

                // In OpenGL, x and y are in the range [-1, 1]
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                // Line 1 - middle line dividing the board into half horizontally
                -0.5f, 0f,
                0.5f, 0f,
                // Mallets
                0f, -0.25f,
                0f, 0.25f,
        };

        // Memory allocation for vertexData
        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriagles.length * BYTES_PER_FLOAT) // this memory will not be managed by garbage collector
                .order(ByteOrder.nativeOrder()) // use the same order as the platform
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriagles);// copy from Dalvik's memory to native memory
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // reading shader source from files
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        // uploading and compiling shaders
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        // linking shader to a program
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        // validating program
        ShaderHelper.validateProgram(program);
        // Indicating OpenGL to use the program we created while drawing anything onto the screen
        GLES20.glUseProgram(program);
        // Retrieving u_Color and a_Position location
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        // Associate vertex data with attribute
        vertexData.position(0); // tell OpenGL to start reading vertexData from beginning
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation); // enabling vertex attribute array
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height); // set OpenGL viewport to full screen
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clears the rendering surface  and fills it with the color previously defined by glClearColor
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Drawing the table
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f); // White table
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);//draw triangles - 6 vertices are given so 2 triangles will be drawn
        // Drawing the dividing line
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f); // red line
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
        // Drawing the mallets as points
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f); // blue mallet
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f); // red mallet
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

    }
}
