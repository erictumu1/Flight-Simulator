package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.vecmath.Vector3d;

import java.util.Iterator;

import static codes.Commons.hundredBS;

public class TurretBehaviour extends Behavior {
    public float speed = -0.03f;

    public static Alpha alpha = new Alpha(1, 500);
    public TransformGroup aimBot = new TransformGroup();
    private TransformGroup gggg = new TransformGroup();
    private WakeupOnElapsedFrames wakeUpCall;
    private Transform3D trans3 = new Transform3D();
    public static Transform3D trans33 = new Transform3D();

    private Alpha bigAlpha;
    public BranchGroup ammoBG;
    public TurretBehaviour(Bounds bounds) {
        ammoBG = new BranchGroup();
        Appearance app = CommonsAR.obj_Appearance(CommonsAR.Green);
        Box laser = new Box(0.03f, 0.01f, 0.03f, app);
        TransformGroup turretTG = new TransformGroup();
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        turretTG.addChild(laser);
        ammoBG.addChild(turretTG);
        Transform3D axis = new Transform3D();
        axis.rotY(Math.PI / 2);
        PositionInterpolator getter = new PositionInterpolator(alpha, turretTG, axis, 0.0f, 50.0f);
        bigAlpha = alpha;
        getter.setSchedulingBounds(hundredBS);
        ammoBG.addChild(getter);
        aimBot.addChild(ammoBG);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.setSchedulingBounds(bounds);
    }
    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        trans3.set(new Vector3d(0.0f, 0.0f, speed));
        trans33.mul(trans3);
        aimBot.setTransform(trans33);
        wakeupOn(wakeUpCall);

    }
}
