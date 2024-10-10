package frc.robot.pseudoCode;

import frc.robot.auton.AutonAction;
import frc.robot.auton.AutonMoveInches;
import frc.robot.auton.AutonRotateWithPIDCommand;
import frc.robot.auton.AutonShoot;
import frc.robot.auton.AutonShooterFeed;
import frc.robot.auton.ExtendArms;
import frc.robot.auton.RetractArms;
import java.util.ArrayDeque;

public class PseudoCodeActions {

    public static ArrayDeque<AutonAction> pseudoCodeActionQueue = new ArrayDeque<>();

    public static void addAction(AutonAction action) {
        pseudoCodeActionQueue.add(action);
    }

    public static void moveForwards(int inches) {
        addAction(new AutonMoveInches(inches));
    }

    public static void moveBackwards(int inches) {
        addAction(new AutonMoveInches(-inches));
    }

    public static void rotateRight(double degrees) {
        addAction(new AutonRotateWithPIDCommand(degrees));
    }

    public static void rotateLeft(double degrees) {
        addAction(new AutonRotateWithPIDCommand(-degrees));
    }

    public static void shoot() {
        addAction(new AutonShoot());
    }

    public static void feed() {
        addAction(new AutonShooterFeed());
    }

    public static void raiseArms() {
        addAction(new ExtendArms());
    }

    public static void lowerArms() {
        addAction(new RetractArms());
    }
}
