package frc.robot.robotcomponents;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.auton.AutonMoveHoodToPosition;
import frc.robot.auton.AutonMoveHoodToPosition.HoodPosition;
import frc.robot.auton.AutonShoot;
import frc.robot.auton.AutonShooterFeed;

public class Shooter {

    private static HoodPosition currentHoodPosition;

    public static void setCurrentHoodPosition(HoodPosition currentHoodPosition) {
        Shooter.currentHoodPosition = currentHoodPosition;
    }

    public static HoodPosition getCurrentHoodPosition() {
        return currentHoodPosition;
    }

    private static boolean currentlyPosititionRing = false;

    public static void setCurrentlyPositioningRing(boolean isCurrentlyPosititionRing) {
        Shooter.currentlyPosititionRing = isCurrentlyPosititionRing;
    }

    public static boolean isCurrentlyPosititionRing() {
        return currentlyPosititionRing;
    }

    public static void startShooterMotors() {
        Robot.motors.getLeftFlywheel().set(Constants.SHOOTER_LEFT_FLYWHEEL_SPEED);
        Robot.motors.getRightFlywheel().set(Constants.SHOOTER_RIGHT_FLYWHEEL_SPEED);
    }

    public static void stopShooterMotors() {
        Robot.getTeleopActionRunner().removeActionsOfType(AutonShooterFeed.class, AutonShoot.class);
        Robot.motors.getLeftFlywheel().stopMotor();
        Robot.motors.getRightFlywheel().stopMotor();
        Robot.motors.getFeeder().stopMotor();
    }

    public static void startFeedMotors() {
        Robot.getTeleopActionRunner().removeActionsOfType(AutonShooterFeed.class);
        Robot.getTeleopActionRunner().addActionToRun(new AutonShooterFeed());
    }

    /* This will spin the motor backwards in an attempt to eject a stuck note*/
    public static void backtrack() {
        Robot.getTeleopActionRunner().removeActionsOfType(AutonShooterFeed.class, AutonShoot.class);
        Robot.motors.getLeftFlywheel().set(Constants.MOTOR_BACKTRACK_SPEED_PERCENT);
        Robot.motors.getRightFlywheel().set(Constants.MOTOR_BACKTRACK_SPEED_PERCENT);
        Robot.motors.getFeeder().set(Constants.MOTOR_BACKTRACK_SPEED_PERCENT);
    }

    public static void autoShootIntoSpeaker() {
        Robot.getTeleopActionRunner().removeActionsOfType(AutonShoot.class);
        Robot.getTeleopActionRunner().addActionToRun(new AutonShoot());
    }

    public static void moveHoodToIntakePosition() {
        Robot.motors.getHoodAdjuster().set(Constants.SHOOTER_HOOD_ADJUSTMENT_SPEED);
        // Robot.getTeleopActionRunner().removeActionsOfType(AutonMoveHoodToPosition.class);
        // Robot.getTeleopActionRunner().addActionToRun(new AutonMoveHoodToPosition(HoodPosition.INTAKING));
    }

    public static void moveHoodToShootingPosition() {
        Robot.motors.getHoodAdjuster().set(-Constants.SHOOTER_HOOD_ADJUSTMENT_SPEED);
        // Robot.getTeleopActionRunner().removeActionsOfType(AutonMoveHoodToPosition.class);
        // Robot.getTeleopActionRunner().addActionToRun(new AutonMoveHoodToPosition(HoodPosition.SHOOTING_DEFAULT));
    }

    public static void stopHoodMotors() {
        Robot.motors.getHoodAdjuster().stopMotor();
    }

    public static void stopFeedMotors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopFeedMotors'");
    }
}
