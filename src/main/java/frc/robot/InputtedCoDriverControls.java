package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.auton.AutonAction;
import frc.robot.auton.AutonMoveHoodToPosition.HoodPosition;
import frc.robot.robotcomponents.Climber;
import frc.robot.robotcomponents.Intake;
import frc.robot.robotcomponents.Shooter;

public class InputtedCoDriverControls {

    public static void onTeleopInit() {
        hasGoneUp = false;
    }

    /**Prevents the codriver from accidentally moving the arms down */
    static boolean hasGoneUp = false;
    static int lastPOV = -1;

    public static void onEveryFrame() {
        XboxController controller = Robot.getCoDriverController();
        if (controller.getXButtonPressed()) {
            Shooter.startFeedMotors();
        }
        if (controller.getLeftBumperPressed()) {
            Shooter.startShooterMotors();
        } else if (controller.getYButtonPressed()) {
            Shooter.autoShootIntoSpeaker();
        }
        if (controller.getLeftBumperReleased()) {
            Shooter.stopShooterMotors();
        }

        // if (controller.getAButtonPressed()) {
        //     Intake.startSourceIntake();
        // }
        // // TODO add this: && !Shooter.isPositioningRing() when mechanics adds a breambreak sensor (never)
        // if (controller.getAButtonReleased()) {
        //     Intake.stopSourceIntake();
        // }

        if (controller.getBackButton()) {
            Climber.manualExtendArms();
            hasGoneUp = true;
        } else if (controller.getStartButton() && hasGoneUp) {
            Climber.manualRetractArms();
        } else {
            Climber.stopClimbMotors();
        }

        // if (Double.compare(controller.getPOV(), 0.90) == 0) {
        //     Shooter.moveHoodToIntakePosition();
        // } else if (controller.getPOV() >= 0.29 && controller.getPOV() <= 0.71) {
        //     // Shooter.moveHoodToShootingPosition();
        //     Shooter.stopHoodMotors();
        // } else if (Double.compare(controller.getPOV(), 0.10) == 0) {
        //     Shooter.moveHoodToIntakePosition();
        //     // TODO add 3rd position if needed
        // }
        if (controller.getBButton()) {
            Intake.startSourceIntake();
            // TODO add this: && !Shooter.isPositioningRing() when mechanics adds a breambreak sensor (never)
        } else if (controller.getBButtonReleased()) {
            Intake.stopSourceIntake();
        }
        if (controller.getPOV() == 0) {
            Intake.backtrack();
            Shooter.backtrack();
        } else if (controller.getPOV() == -1 && lastPOV != -1) {
            Shooter.stopShooterMotors();
        }
        lastPOV = controller.getPOV();
    }
}
