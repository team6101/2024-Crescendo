package frc.robot.auton;

import frc.robot.QuickActions;
import frc.robot.Robot;

public class AutonMoveInches extends AutonAction {

    double distance;
    double isDoneDebounceTime = 0;

    @Override
    public boolean isDone() {
        Robot.motors.getDriveLeftParent().setDistance(distance);
        Robot.motors.getDriveRightParent().setDistance(distance);
        System.out.println(
            Robot.motors.getDriveLeftParent().get() +
            " veL: " +
            Robot.motors.getDriveLeftParent().getActiveTrajectoryVelocity()
        );
        if (getMaxTrajectoryVelocity() < 0.05) {
            isDoneDebounceTime += 0.02;
        } else {
            isDoneDebounceTime = 0;
        }

        if (isDoneDebounceTime > 3) {
            System.out.println("We moved the correct amount of inches!");
            return true;
        }

        return false;
    }

    @Override
    public void initiate() {
        QuickActions.resetDriveTrainEncoders();
    }

    private double getMaxTrajectoryVelocity() {
        return Math.max(
            Robot.motors.getDriveLeftParent().getActiveTrajectoryVelocity(),
            Robot.motors.getDriveRightParent().getActiveTrajectoryVelocity()
        );
    }

    /**
     *
     * @param distance The distance in inches
     */
    public AutonMoveInches(double distance) {
        this.distance = distance;
    }

    @Override
    public void shutdown() {
        QuickActions.stopAll();
    }
}
