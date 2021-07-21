package com.example.airhockey.util;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        int shaderObjectID = GLES20.glCreateShader(type);
        if (shaderObjectID == 0) {
            Log.i(TAG, "Could not create new shader");
            return 0;
        }

        // uploading shader source code
        GLES20.glShaderSource(shaderObjectID, shaderCode);
        // compiling shader source code
        GLES20.glCompileShader(shaderObjectID);
        // retrieving compilation status - store the GL_COMPILE_STATUS in 0th position of compileStatus array
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectID, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.i(TAG, "Results of compiling source:\n" + shaderCode + "\n" + GLES20.glGetShaderInfoLog(shaderObjectID));
        // checking compilation status
        if (compileStatus[0] == 0) {
            // 0 => compilation failed delete the shader object
            GLES20.glDeleteShader(shaderObjectID);
            Log.i(TAG, "compilation of shader failed");
            return 0;
        }
        // shader object is valide - so return its id
        return shaderObjectID;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programObjectID = GLES20.glCreateProgram();
        if (programObjectID == 0) {
            Log.i(TAG, "Could not create new program");
            return 0;
        }
        // Attack vertex and fragment shaders to the program object
        GLES20.glAttachShader(programObjectID, vertexShaderId);
        GLES20.glAttachShader(programObjectID, fragmentShaderId);
        // linking the program
        GLES20.glLinkProgram(programObjectID);
        // retrieving linking status
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectID, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.i(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectID));
        // checking link status
        if (linkStatus[0] == 0) {
            // linking failed - delete program object
            GLES20.glDeleteProgram(programObjectID);
            Log.i(TAG, "Linking of program failed");
            return 0;
        }
        return programObjectID;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);

        // Retrieving validation status
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.i(TAG, "Results of validating program:" + validateStatus[0] + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}






















