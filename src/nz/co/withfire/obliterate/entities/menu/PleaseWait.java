/******************************************************\
| The obliterate logo displayed when the app launches. |
|                                                      |
| @author David Saxon                                  |
\******************************************************/
package nz.co.withfire.obliterate.entities.menu;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.entities.Entity;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.QuadTex2d;
import nz.co.withfire.obliterate.utilities.Vector2d;
import nz.co.withfire.obliterate.utilities.Vector4d;

public class PleaseWait extends Entity {
    
    //VARIABLES
    //the gl dimensions
    private Vector2d GLdim;
    
    //the fade in amount of the logo
    private float fade = 1.0f;
    
    //the quad displaying the logo
    private QuadTex2d logo;
    
    //the texture of the logo
    private int tex;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public PleaseWait(Vector2d GLdim, int tex) {
        
        this.GLdim = GLdim;
        this.tex = tex;
        
        float width = Math.abs(GLdim.getX() / 1.2f);
        float height = width / 5.1f;
        
        
        float quadCoord[] = {   -2.0f,  2.0f, 0.0f,
                                -2.0f, -2.0f, 0.0f,
                                 2.0f, -2.0f, 0.0f,
                                 2.0f,  2.0f, 0.0f};
        final float[] texCoords = { 1.0f, 0.0f,
                                    1.0f, 1.0f,
                                    0.0f, 1.0f, 
                                    0.0f, 0.0f};
        logo = new QuadTex2d(quadCoord, texCoords, tex);
    }
    
    //METHODS
    @Override
    public void update() {

    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //shift into visible range
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 0, .10f, -0.01f);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        logo.draw(mvpMatrix);
    }
}
