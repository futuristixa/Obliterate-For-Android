package nz.co.withfire.obliterate.entities.main;

import java.util.Random;

import android.opengl.Matrix;
import android.util.Log;
import nz.co.withfire.obliterate.graphics.drawable.shape2d.Quad2d;
import nz.co.withfire.obliterate.physics.CollisionType;
import nz.co.withfire.obliterate.physics.bounding.BoundingRect;
import nz.co.withfire.obliterate.utilities.Vector2d;
import nz.co.withfire.obliterate.utilities.Vector3d;
import nz.co.withfire.obliterate.utilities.Vector4d;

public class Obstacle extends CollisionType {

    //VARIBLES
    //the centre position of the obstacle
    private Vector2d pos = new Vector2d();
    //first corner of the obstacle
    private Vector2d firstCorner = new Vector2d();
    //second corner of the obstacle
    private Vector2d secondCorner = new Vector2d();
    //the dimensions of the obstacle
    private Vector2d dim = new Vector2d();
    
    //is true if been pressed once
    private boolean tapped = false;
    private int tapTimer = 0;
    
    //random number generator
    private Random rand = new Random();
    
    //the image
    private Quad2d image;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the transformation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    public Obstacle(Vector2d pos) {
        
        this.pos.copy(pos);
        firstCorner.copy(pos);
        secondCorner.copy(pos);
        
        //create a random colour for the obstacle
        Vector3d col = new Vector3d((rand.nextFloat() / 2.0f) + 0.5f,
            (rand.nextFloat() / 2.0f) + 0.5f, (rand.nextFloat() / 2.0f) + 0.5f);
        
        float[] coord = {
                -dim.getX(),  dim.getY(), 0.0f,
                -dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(),  dim.getY(), 0.0f,
                        };
        float[] colour = {
              col.getX(), col.getY(), col.getZ(), 1.0f,
              col.getX(), col.getY(), col.getZ(), 1.0f,
              col.getX(), col.getY(), col.getZ(), 1.0f,
              col.getX(), col.getY(), col.getZ(), 1.0f
                        };
            
            image = new Quad2d(coord, colour);
    }
    
    //METHODS
    @Override
    public void update() {
        
        if (tapTimer <= 0) {
            
            tapped = false;
        }
        else {
            
            --tapTimer;
        }
    }
    
    @Override
    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        
        //shift into visible range and move
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), -0.01f);
        
        //Multiply matrix
        Matrix.multiplyMM(mvpMatrix, 0, tMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        image.draw(mvpMatrix);
    }
    
    @Override
    public Vector2d getDim() {
        
        return dim;
    }
    
    @Override
    public Vector2d getPos() {
        
        return pos;
    }
    
    public void setCorner(Vector2d corner) {
        
        secondCorner.copy(corner);
        
        //find the dimensions
        dim.set(Math.abs(firstCorner.getX() - secondCorner.getX()) / 2.0f,
                Math.abs(firstCorner.getY() - secondCorner.getY()) / 2.0f);
                
        //find new pos
        if (firstCorner.getX() < secondCorner.getX()) {
            
            pos.setX(firstCorner.getX() + dim.getX());
        }
        else {
            
            pos.setX(secondCorner.getX() + dim.getX());
        }
        if (firstCorner.getY() < secondCorner.getY()) {
            
            pos.setY(firstCorner.getY() + dim.getY());
        }
        else {
            
            pos.setY(secondCorner.getY() + dim.getY());
        }
        
        //set the quad coords
        float[] coord = {
                -dim.getX(),  dim.getY(), 0.0f,
                -dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(), -dim.getY(), 0.0f,
                 dim.getX(),  dim.getY(), 0.0f,
        };
        
        image.setCoords(coord);
    }
    
    public void place() {
        
        //don't place the obstacle if it is too small
        if (dim.getX() < 0.05f && dim.getY() < 0.05f) {
            
            remove = true;
        }
        
        boundingBox = new BoundingRect(pos,
            new Vector2d(dim.getX() * 2.0f, dim.getY() *2.0f));
    }
    
    public void tap() {
        
        if (!tapped) {
            
            tapped = true;
            tapTimer = 20;
        }
        else {
            
            remove = true;
        }
    }
}
