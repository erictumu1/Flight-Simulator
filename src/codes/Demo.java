package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import static codes.Commons.*;

public class Demo extends JPanel {
    private Canvas3D[] canvas = null;
    private Matrix4d mtrx = new Matrix4d();
    private static float speed = -0.03f;
    protected PositionInterpolator targetting;

    private static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
    private static BranchGroup ammoBG;
    private static TransformGroup aimBot;
    private static float height = 0.0f;
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private BoundingSphere thousandBS = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
    private static TransformGroup objTG;                              // use 'objTG' to position an object
    public static float n = (float) 1.0;
    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
        TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
        sceneBG.addChild(sceneTG);
        sceneBG.addChild(CommonsAR.add_Lights(CommonsAR.White, 1));
//        Asteroid[] asteroids = new Asteroid[500];
//        for (Asteroid asteroid : asteroids) {
//            asteroid = new Asteroid();
//            sceneTG.addChild(asteroid);
//            sceneTG.addChild(asteroid.objTG);
//        }
//        sceneTG.addChild(new SolarSystem().create_Object());

        Appearance bgAppearance = new Appearance();
        Texture bgTexture = new TextureLoader("Imports/Textures/bg.png", null).getTexture();
        bgTexture.setBoundaryModeS(Texture.WRAP);
        bgTexture.setBoundaryModeT(Texture.WRAP);
        bgAppearance.setTexture(bgTexture);

        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        bgAppearance.setPolygonAttributes(pa);

        Sphere bgSphere = new Sphere(10000f, Sphere.GENERATE_TEXTURE_COORDS, bgAppearance);
        bgSphere.setCapability(Sphere.ALLOW_BOUNDS_WRITE);
        bgSphere.setCapability(Sphere.ALLOW_LOCAL_TO_VWORLD_READ);

        sceneTG.addChild(bgSphere);

        return sceneBG;
    }
    public Demo(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D[2];
        for (int i = 0; i < 2; i++) {
            canvas[i] = new Canvas3D( config );
            canvas[i].setSize( 600, 800 );
            add( canvas[i] );                            // add 2 Canvas3D to Frame
        }
        objTG = new TransformGroup();
        aimBot = new TransformGroup();
        SimpleUniverse su = new SimpleUniverse();    // create a SimpleUniverse
        Locale lcl = su.getLocale();                        // point 2nd/3rd Viewer to c3D[1,2]
        lcl.addBranchGraph( createViewer( canvas[0] ,  10, 0, 0 ) );
        lcl.addBranchGraph( createViewer( canvas[1] ,  -10, 0, 0 ) );
        CommonsAR.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
        su.getViewer().getView().setBackClipDistance(1000);


        Background bg = new Background();
        bg.setImage(new TextureLoader("Imports/Textures/bg.png", null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));

        sceneBG.addChild(bg);


        Jet.TurretBehaviour gunTurret1 = new Jet.TurretBehaviour(hundredBS);
        aimBot.addChild(gunTurret1.aimBot);
        aimBot.addChild(gunTurret1);
        Transform3D trans1 = new Transform3D();
        trans1.setTranslation(new Vector3f(100, 0, 0));
        Jet.MovingPlane plane1 = new Jet.MovingPlane("Imports/Objects/Fighter_01.obj");
        objTG.addChild(plane1.objTG);
        objTG.addChild(plane1);
        canvas[0].addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == 'a') {
                    trans1.rotY(Math.PI/20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }

                if (key == 'd') {
                    trans1.rotY(-Math.PI/20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }


                if (key == 'w') {
                    trans1.rotX(-Math.PI/20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }

                if (key == 's') {
                    trans1.rotX(Math.PI/20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }
                if (key == 'q') {
                    if(plane1.speed <= -1) {
                        plane1.speed += 0.005;
                        System.out.println("You Are Stalling");
                    }
                    else {
                        plane1.speed += 0.001;
                    }
                }
                if (key == 'e') {
                    if(plane1.speed <= 0) {
                        plane1.speed -= 0.001;
                    }
                    else {
                        plane1.speed = 0;

                    }
                }
                if(plane1.speed > 0) {
                    plane1.speed = 0;
                }
                if (key == 'f') {
                    gunTurret1.alpha.setStartTime(System.currentTimeMillis());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Jet.TurretBehaviour gunTurret2 = new Jet.TurretBehaviour(hundredBS);
        aimBot.addChild(gunTurret2.aimBot);
        aimBot.addChild(gunTurret2);
        Transform3D trans2 = new Transform3D();
        trans2.setTranslation(new Vector3f(0, 0, 0));
        Jet.MovingPlane plane2 = new Jet.MovingPlane("Imports/Objects/Fighter_01.obj");
        objTG.addChild(plane2.objTG);
        objTG.addChild(plane2);
        canvas[1].addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == '4') {
                    trans2.rotY(Math.PI/20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == '6') {
                    trans2.rotY(-Math.PI/20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }


                if (key == '5') {
                    trans2.rotX(-Math.PI/20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == '2') {
                    trans2.rotX(Math.PI/20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }
                if (key == '4') {
                    if(plane2.speed <= -1) {
                        plane2.speed += 0.005;
                        System.out.println("You Are Stalling");
                    }
                    else {
                        plane2.speed += 0.001;
                    }
                }
                if (key == '6') {
                    if(plane2.speed <= 0) {
                        plane2.speed -= 0.001;
                    }
                    else {
                        plane2.speed = 0;

                    }
                }
                if(plane2.speed > 0) {
                    plane2.speed = 0;
                }
                if (key == '+') {
                    gunTurret2.alpha.setStartTime(System.currentTimeMillis());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });
        sceneBG.addChild(objTG);
        sceneBG.addChild(aimBot);
        sceneBG.addChild(CommonsAR.key_Navigation(su));     // allow key navigation
        sceneBG.compile();		                           // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas[0],su);


    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame();                   // NOTE: change AR to student's initials
        frame.getContentPane().add(new Demo(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
