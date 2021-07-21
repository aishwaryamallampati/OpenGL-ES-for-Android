package com.example.airhockey;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * To play a game of air hockey, we need a long rectangular table with two goals (one on each end), a puck, and two mallets to strike the puck with.
 * Each round starts with the puck placed in the middle of the table. Each player then tries to strike the puck into the opponent’s goal while preventing the opponent from doing the same.
 * The first player to reach seven goals wins the game.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2); // get OpenGL3 context
        // GLSurfaceView does rendering only in a background thread
        glSurfaceView.setRenderer(new AirHockeyRenderer(this)); // set custom renderer
        renderSet = true;
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet) {
            glSurfaceView.onResume();
        }
    }
}