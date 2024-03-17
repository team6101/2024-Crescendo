package frc.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import java.util.ArrayDeque;
import java.util.List;

public class AutonRoutes {

    public static final ArrayDeque<AutonAction> GO_BACKWARD_OUT_OF_STARTING_ZONE = new ArrayDeque<AutonAction>(
        (List.of(new AutonMoveInches(-84)))
    );

    public static final ArrayDeque<AutonAction> GO_FORWARD_OUT_OF_STARTING_ZONE = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(84))
    );

    public static final ArrayDeque<AutonAction> JUST_SHOOT = new ArrayDeque<AutonAction>(List.of(new AutonShoot()));

    public static ArrayDeque<AutonAction> SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE = new ArrayDeque<>();

    public static ArrayDeque<AutonAction> SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE = new ArrayDeque<>();

    public static final ArrayDeque<AutonAction> SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE_RED = new ArrayDeque<
        AutonAction
    >(List.of(new AutonShoot(), new AutonMoveInches(-84)));

    public static final ArrayDeque<AutonAction> SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE_RED = new ArrayDeque<AutonAction>(
        List.of(
            new AutonShoot(),
            new AutonMoveInches(-40),
            new AutonRotateWithPIDCommand(-45),
            new AutonMoveInches(-65)
        )
    );

    public static final ArrayDeque<AutonAction> SHOOT_ROTATE_BACK_UP_FROM_TERMINAL_SIDE_BLUE = new ArrayDeque<
        AutonAction
    >(List.of(new AutonShoot(), new AutonMoveInches(-150)));

    public static final ArrayDeque<AutonAction> SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE_BLUE = new ArrayDeque<AutonAction>(
        List.of(new AutonShoot(), new AutonMoveInches(-40), new AutonRotateWithPIDCommand(45), new AutonMoveInches(-65))
    );

    public static final ArrayDeque<AutonAction> SHOOT_AND_BACK_UP_SKETCHILY = new ArrayDeque<AutonAction>(
        List.of(new AutonShoot(), new MovePercentOutput())
    );

    public static final ArrayDeque<AutonAction> SHOOT_AND_BACK_UP_FROM_CENTER = new ArrayDeque<AutonAction>(
        List.of(new AutonShoot(), new AutonMoveInches(-84))
    );

    public static ArrayDeque<AutonAction> BACKUP_TURN_BACKUP = new ArrayDeque<>();

    private static final ArrayDeque<AutonAction> BLUE_BACKUP_TURN_BACKUP = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(-24), new AutonRotateWithPIDCommand(-135), new AutonMoveInches(36))
    );

    private static final ArrayDeque<AutonAction> RED_BACKUP_TURN_BACKUP = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(-24), new AutonRotateWithPIDCommand(135), new AutonMoveInches(36))
    );

    public static ArrayDeque<AutonAction> MESS_UP_CENTER_RINGS_FROM_AMP_SIDE = new ArrayDeque<>();

    public static ArrayDeque<AutonAction> MESS_UP_CENTER_RINGS_FROM_TERMINAL_SIDE = new ArrayDeque<>();

    private static final ArrayDeque<AutonAction> MESS_UP_CENTER_RINGS_TURN_LEFT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(312), new AutonRotateWithPIDCommand(-90), new AutonMoveInches(252))
    );

    private static final ArrayDeque<AutonAction> MESS_UP_CENTER_RINGS_TURN_RIGHT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(312), new AutonRotateWithPIDCommand(90), new AutonMoveInches(252))
    );

    /**Paths will slightly differ based on what team we're on, so these will correct them. */
    public static void setupCorrectAutonPaths() {
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            BACKUP_TURN_BACKUP.addAll(RED_BACKUP_TURN_BACKUP);
            MESS_UP_CENTER_RINGS_FROM_AMP_SIDE.addAll(MESS_UP_CENTER_RINGS_TURN_LEFT);
            MESS_UP_CENTER_RINGS_FROM_TERMINAL_SIDE.addAll(MESS_UP_CENTER_RINGS_TURN_RIGHT);
            SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE.addAll(SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE_RED);
            SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE.addAll(SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE_RED);
            System.out.println("Configuring auton routes for RED team");
        } else {
            BACKUP_TURN_BACKUP = BLUE_BACKUP_TURN_BACKUP;
            MESS_UP_CENTER_RINGS_FROM_AMP_SIDE.addAll(MESS_UP_CENTER_RINGS_TURN_RIGHT);
            MESS_UP_CENTER_RINGS_FROM_TERMINAL_SIDE.addAll(MESS_UP_CENTER_RINGS_TURN_LEFT);
            SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE.addAll(SHOOT_ROTATE_BACK_UP_FROM_TERMINAL_SIDE_BLUE);
            SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE.addAll(SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE_BLUE);
            System.out.println("Configuring auton routes for BLUE team");
        }
    }

    public static final ArrayDeque<AutonAction> SHOOT_BACKUP_INTAKE_FORWARD_SHOOT = new ArrayDeque<AutonAction>(
        List.of(
            new AutonShoot(),
            new AutonParallelAction(new AutonFloorIntake(), new AutonMoveInches(-84)),
            new AutonMoveInches(84),
            new AutonShoot()
        )
    );

    public static final ArrayDeque<AutonAction> BOOM = new ArrayDeque<AutonAction>(List.of(new explodeDaBomb()));

    public static ArrayDeque<AutonAction> TEST_ROTATION_WITH_PID_COMMAND = new ArrayDeque<AutonAction>(
        List.of(new AutonRotateWithPIDCommand(90))
    );

    public static ArrayDeque<AutonAction> TEST_ROTATION = new ArrayDeque<AutonAction>(List.of(new AutonRotate(90)));

    public static ArrayDeque<AutonAction> TEST_SMART_MOTION_MOVEMENT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(60))
    );

    public static ArrayDeque<AutonAction> TEST_SMART_MOTION_BACKWARD_MOVEMENT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(-60))
    );

    public static ArrayDeque<AutonAction> TEST_PID_MOVEMENT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInchesWithPIDCommand(60))
    );

    public static ArrayDeque<AutonAction> TEST_PID_BACKWARD_MOVEMENT = new ArrayDeque<AutonAction>(
        List.of(new AutonMoveInches(-60))
    );
}
