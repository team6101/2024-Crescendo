package frc.robot.robotcomponents;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.auton.AutonFloorIntake;
import frc.robot.auton.AutonMoveHoodToPosition;
import frc.robot.auton.AutonMoveHoodToPosition.HoodPosition;

public class Intake {

    public static void autoIntake() {
        Robot.getTeleopActionRunner().removeActionsOfType(AutonFloorIntake.class);
        // Robot.getTeleopActionRunner().addActionToRun(new AutonIntake());
    }

    public static void startFloorIntake() {
        // if (Shooter.getCurrentHoodPosition() != HoodPosition.INTAKING) {
        //     if (!Robot.getTeleopActionRunner().containsAction(AutonMoveHoodToPosition.class)) {
        //         Robot.getTeleopActionRunner().addActionToRun(new AutonMoveHoodToPosition(HoodPosition.INTAKING));
        //     }
        //     stopFloorIntake();
        //     return;
        // }
        // if (!Robot.getTeleopActionRunner().containsAction(AutonFloorIntake.class)) {
        //     Robot.getTeleopActionRunner().addActionToRun(new AutonFloorIntake());
        // }
        Robot.motors.getFeeder().set(Constants.FLOOR_INTAKE_SPEED);
    }

    public static void startSourceIntake() {
        // Robot.getTeleopActionRunner().addActionToRun(new AutonSourceIntake());
        Robot.motors.getLeftFlywheel().set(Constants.SOURCE_INTAKE_SPEED);
        Robot.motors.getRightFlywheel().set(Constants.SOURCE_INTAKE_SPEED);
    }

    /* This will spin the motor backwards in an attempt to eject a stuck note*/
    public static void backtrack() {}

    public static void stopSourceIntake() {
        // Robot.getTeleopActionRunner().removeActionsOfType(AutonSourceIntake.class);
        Robot.motors.getLeftFlywheel().stopMotor();
        Robot.motors.getRightFlywheel().stopMotor();
    }

    public static void stopFloorIntake() {
        Robot.motors.getFeeder().stopMotor();
        // Robot.getTeleopActionRunner().removeActionsOfType(AutonFloorIntake.class);
    }
}
