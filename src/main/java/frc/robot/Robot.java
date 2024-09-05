// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auton.AutonAction;
import frc.robot.auton.AutonRoutes;
import frc.robot.auton.AutonWait;
import frc.robot.auton.ParallelActionRunner;
import frc.robot.auton.SequentialActionRunner;
import frc.robot.motor.MotorController;
import frc.robot.motor.MotorControllerFactory;
import java.text.DecimalFormat;
import java.util.ArrayDeque;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRo\bot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    //hi
    // hello!!!!!!!

    // AprilTagHighlighter aprilTagHighlighter;

    public static RobotMotors motors;

    MotorController driveLeftParent = MotorControllerFactory.create(
        Constants.DRIVE_LEFT_PARENT_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController driveLeftChild = MotorControllerFactory.create(
        Constants.DRIVE_LEFT_CHILD_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController driveRightParent = MotorControllerFactory.create(
        Constants.DRIVE_RIGHT_PARENT_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController driveRightChild = MotorControllerFactory.create(
        Constants.DRIVE_RIGHT_CHILD_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController leftFlywheel = MotorControllerFactory.create(
        Constants.SHOOTER_LEFT_FLYWHEEL_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController rightFlywheel = MotorControllerFactory.create(
        Constants.SHOOTER_RIGHT_FLYWHEEL_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController feederMotor = MotorControllerFactory.create(
        Constants.SHOOTER_FEEDER_ID,
        MotorController.Type.SparkMaxBrushless
    );
    MotorController leftClimb = MotorControllerFactory.create(
        Constants.LEFT_CLIMB_ID,
        MotorController.Type.SparkMaxBrushed
    );
    MotorController rightClimb = MotorControllerFactory.create(
        Constants.RIGHT_CLIMB_ID,
        MotorController.Type.SparkMaxBrushed
    );
    // MotorController intake = MotorControllerFactory.create(Constants.INTAKE_ID, MotorController.Type.SparkMaxBrushed);
    // MotorController hoodAdjuster = MotorControllerFactory.create(
    //     Constants.SHOOTER_HOOD_ADJUSTERER_ID,
    //     MotorController.Type.SparkMaxBrushed
    // );

    static XboxController driverController = new XboxController(Constants.DRIVER_CONTROLLER_ID);
    static XboxController coDriverController = new XboxController(Constants.CODRIVER_CONTROLLER_ID);

    public static XboxController getDriverController() {
        return driverController;
    }

    public static XboxController getCoDriverController() {
        return coDriverController;
    }

    static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    SequentialActionRunner auton;
    static ParallelActionRunner teleopActionRunner = new ParallelActionRunner();

    public static ParallelActionRunner getTeleopActionRunner() {
        return teleopActionRunner;
    }

    private static final DigitalInput shooterBeamBreakSensor = new DigitalInput(Constants.INTAKE_BEAM_BREAK_ID);

    static int beambreakConfidenceFrames = 0;

    public static boolean getBeambreakSensorConfidentlyTriggered() {
        return beambreakConfidenceFrames > Constants.REQUIRED_BEAM_BREAK_CONFIDENCE_FRAMES;
    }

    private final SendableChooser<ArrayDeque<AutonAction>> autonRouteChooser = new SendableChooser<>();

    public static ADXRS450_Gyro getGyroscope() {
        return gyro;
    }

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        initializeSmartMotion(driveLeftParent, Constants.NORMAL_ROBOT_GAINS);
        initializeSmartMotion(driveRightParent, Constants.NORMAL_ROBOT_GAINS);

        // aprilTagHighlighter = new AprilTagHighlighter();
        autonRouteChooser.setDefaultOption("move forward", AutonRoutes.GO_FORWARD_OUT_OF_STARTING_ZONE);
        setupAutonRoutes();
        //This is an emergency button for switching controllers mid match
        SmartDashboard.putData(
            "SWITCH CONTROLLERS",
            new Command() {
                {
                    setName("Execute");
                }

                @Override
                public void initialize() {
                    System.out.println("We have switched thy controllers");
                    int driverPort = driverController.getPort();
                    int coDriverPort = coDriverController.getPort();
                    driverController = new XboxController(coDriverPort);
                    coDriverController = new XboxController(driverPort);
                }

                @Override
                public boolean isFinished() {
                    return true;
                }
            }
        );

        SmartDashboard.putNumber("Auton Delay (sec)", 0.0);

        SmartDashboard.putNumber("Time Remaining", Timer.getMatchTime());

        SmartDashboard.putData("Auton Routes", autonRouteChooser);

        SmartDashboard.putBoolean("Is AutoShooting?", false);

        driveLeftChild.follow(driveLeftParent);
        driveRightChild.follow(driveRightParent);

        driveLeftParent.setInverted(false);
        driveRightParent.setInverted(true);

        feederMotor.setInverted(true);
        //This false is required
        feederMotor.setBrakeMode(false);

        rightFlywheel.setBrakeMode(false);
        leftFlywheel.setBrakeMode(false);

        rightFlywheel.setInverted(true);
        leftFlywheel.setInverted(false);

        System.out.println("Is drive right parent inverted? " + driveRightParent.getInverted());

        driveLeftChild.setBrakeMode(true);
        driveLeftParent.setBrakeMode(true);
        driveRightChild.setBrakeMode(true);
        driveRightParent.setBrakeMode(true);

        leftClimb.setInverted(true);
        rightClimb.setInverted(true);

        leftClimb.setBrakeMode(true);
        rightClimb.setBrakeMode(true);

        // System.out.println("GYOR ROEADING: " + gyro.getAngle());

        // driveLeftParent.burnFlash();
        // driveRightParent.burnFlash();
        // driveLeftChild.burnFlash();
        // leftClimb.burnFlash();
        // rightClimb.burnFlash();

        motors =
            new RobotMotors()
                .driveLeftParent(driveLeftParent)
                .driveLeftChild(driveLeftChild)
                .driveRightParent(driveRightParent)
                .driveRightChild(driveRightChild)
                .feeder(feederMotor)
                .leftFlywheel(leftFlywheel)
                .rightFlywheel(rightFlywheel)
                .leftClimb(leftClimb)
                .rightClimb(rightClimb);
        // .intake(intake);
        // .hoodAdjuster(hoodAdjuster);
        // getGyroscope().reset();
        System.out.println(Constants.APRIL_TAG_CONFIDENCE_FRAMES);
        SmartDashboard.putNumber("rotationGainsP", Constants.ROTATION_GAINS.P);
        SmartDashboard.putNumber("rotationGainsI", Constants.ROTATION_GAINS.I);
        SmartDashboard.putNumber("rotationGainsD", Constants.ROTATION_GAINS.D);

        SmartDashboard.putNumber("PID TARGET", 90);

        System.out.println("Is drive right parent inverted at end?? " + driveRightParent.getInverted());

        UsbCamera camera = CameraServer.startAutomaticCapture("Front Cam", 0);
        camera.setFPS(15);

        // Set the resolution
        camera.setResolution(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);

        SmartDashboard.putBoolean("Estimated Controller Status", true);

        gyro.calibrate();
        System.out.println("OTHER GYRO: " + gyro.getAngle());
    }

    public void setupAutonRoutes() {
        autonRouteChooser.addOption("move forward", AutonRoutes.GO_FORWARD_OUT_OF_STARTING_ZONE);
        autonRouteChooser.addOption("move backwards", AutonRoutes.GO_BACKWARD_OUT_OF_STARTING_ZONE);
        autonRouteChooser.addOption("just shoot", AutonRoutes.JUST_SHOOT);

        autonRouteChooser.addOption("shoot and back up", AutonRoutes.SHOOT_AND_BACK_UP_FROM_CENTER);
        autonRouteChooser.addOption("shoot and back up forever", AutonRoutes.SHOOT_AND_BACK_UP_SKETCHILY);

        autonRouteChooser.addOption(
            "shoot backup rotate backup from amp side",
            AutonRoutes.SHOOT_BACK_UP_ROTATE_FROM_AMP_SIDE
        );
        autonRouteChooser.addOption(
            "shoot backup rotate backup from source side",
            AutonRoutes.SHOOT_BACK_UP_ROTATE_FROM_TERMINAL_SIDE
        );

        autonRouteChooser.addOption("backup turn backup", AutonRoutes.BACKUP_TURN_BACKUP);
        autonRouteChooser.addOption("explode hidden bomb", AutonRoutes.BOOM);
        autonRouteChooser.addOption("shoot backup intake forward shoot", AutonRoutes.SHOOT_BACKUP_INTAKE_FORWARD_SHOOT);

        autonRouteChooser.addOption("Test old rotation PID", AutonRoutes.TEST_ROTATION);
        autonRouteChooser.addOption("Test new rotation PID", AutonRoutes.TEST_ROTATION_WITH_PID_COMMAND);
        autonRouteChooser.addOption("Test SmartMotion movement", AutonRoutes.TEST_SMART_MOTION_MOVEMENT);
        autonRouteChooser.addOption(
            "Test SmartMotion backward movement",
            AutonRoutes.TEST_SMART_MOTION_BACKWARD_MOVEMENT
        );
        autonRouteChooser.addOption("Test Manual PID movement", AutonRoutes.TEST_PID_MOVEMENT);
        autonRouteChooser.addOption("Test Manual Backward PID movement", AutonRoutes.TEST_PID_BACKWARD_MOVEMENT);
    }

    /*
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */

    private static final DecimalFormat prettyDecimalMaker = new DecimalFormat("#.#");

    @Override
    public void robotPeriodic() {
        SmartDashboard.putString("Time Remaining", prettyDecimalMaker.format(Timer.getMatchTime()));

        SmartDashboard.putNumber("left Shooter velocity", leftFlywheel.getActiveTrajectoryVelocity());
        SmartDashboard.putNumber("right Shooter velocity", rightFlywheel.getActiveTrajectoryVelocity());

        // aprilTagHighlighter.doEveryFrame();
        SmartDashboard.putNumber("Gyro Reading", gyro.getAngle());
        SmartDashboard.putNumber("left Arm encoder", leftClimb.getEncoderPosition());
        SmartDashboard.putNumber("right Arm encoder", rightClimb.getEncoderPosition());
        // SmartDashboard.putNumber("Wheel encoder", driveRightParent.getEncoderPosition());
        SmartDashboard.putNumber("Left motor controller encoder", driveLeftParent.getEncoderPosition());
        SmartDashboard.putNumber("right motor controller encoder", driveRightParent.getEncoderPosition());

        // SmartDashboard.putNumber("Hood controller encoder", hoodAdjuster.getEncoderPosition());

        if (shooterBeamBreakSensor.get()) {
            if (beambreakConfidenceFrames < Constants.REQUIRED_BEAM_BREAK_CONFIDENCE_FRAMES + 10) {
                beambreakConfidenceFrames++;
            }
        } else {
            if (beambreakConfidenceFrames != 0) {
                beambreakConfidenceFrames -= 2;
            }
        }
        // Constants.ROTATION_GAINS.P = SmartDashboard.getNumber("rotationGainsP", kDefaultPeriod);
        // Constants.ROTATION_GAINS.I = SmartDashboard.getNumber("rotationGainsI", kDefaultPeriod);
        // Constants.ROTATION_GAINS.D = SmartDashboard.getNumber("rotationGainsD", kDefaultPeriod);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to the switch structure
     * below with additional strings. If using the SendableChooser make sure to add them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        AutonRoutes.setupCorrectAutonPaths();
        ArrayDeque<AutonAction> route = new ArrayDeque<>(autonRouteChooser.getSelected());
        double delayAmount = SmartDashboard.getNumber("Auton Delay (sec)", 0.0);
        if (delayAmount > 0) {
            route.addFirst(new AutonWait(delayAmount));
        }
        System.out.println("Selected auton route: " + route);
        auton = new SequentialActionRunner(route);
        auton.initiateAuton();
    }

    /**Things Auton needs to do:
     *  - TODO auton wait function
     *  - TODO auton shoot function
     *  - TODO auton rotate function
     *  - TODO make it possible to choose auton paths
     *  - TODO auton adjust height of shooter
     *
     * Auton Paths:
     * TODO auton path: Touching Speaker Next To Amp:
     *  - Wait x amount of time
     *  - Shoot
     *  - Go back
     *  - Turn
     *  - Go back more
     *
     * TODO auton Touching Speaker In Middle:
     *  - Wait x amount of time
     *  - Shoot
     *  - Go back
     * Touching Speaker Next To Terminal:
     *  - Wait x amount of time
     *  - Shoot
     *  - Go back
     */

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        auton.onEveryFrame();
    }

    /** This function is called once when teleop is enabled. */
    @Override
    public void teleopInit() {
        InputtedCoDriverControls.onTeleopInit();
        coDriverController.getAButtonPressed();
        coDriverController.getBButtonPressed();
        coDriverController.getXButtonPressed();
        coDriverController.getYButtonPressed();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        teleopActionRunner.onEveryFrame();
        InputtedDriverControls.onEveryFrame();
        InputtedCoDriverControls.onEveryFrame();
    }

    /** This function is called once when the robot is disabled. */
    @Override
    public void disabledInit() {
        driverController.setRumble(RumbleType.kBothRumble, 0.0);
    }

    /** This function is called periodically when disabled. */
    @Override
    public void disabledPeriodic() {}

    /** This function is called once when test mode is enabled. */
    @Override
    public void testInit() {}

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
        RobotTestControls.testPeriodic();
    }

    /** This function is called once when the robot is first started up. */
    @Override
    public void simulationInit() {}

    /** This function is called periodically whilst in simulation. */
    @Override
    public void simulationPeriodic() {}

    /**
     * WARNING: THIS METHOD WILL RESET MOTOR CONFIG. Do all configuring after this method is called.
     * @param motorController
     * @param gains
     */
    public void initializeSmartMotion(MotorController motorController, Gains gains) {
        /* Factory default hardware to prevent unexpected behavior */
        motorController.reset();

        motorController.asCANSparkMax().setSmartCurrentLimit(40);

        /* Configure Sensor Source for Primary PID */
        motorController.setSensorSource();

        /*
         * set deadband to super small 0.001 (0.1 %). The default deadband is 0.04 (4 %)
         */
        motorController.setNeutralDeadband(0.05);

        /**
         * Configure Talon SRX Output and Sensor direction accordingly Invert Motor to have green LEDs
         * when driving Talon Forward / Requesting Positive Output Phase sensor to have positive
         * increment when driving Talon Forward (Green LED)
         */

        /* Set relevant frame periods to be at least as fast as periodic rate */
        // motorController.setStatusFramePeriod(10);

        /* Set the peak and nominal outputs */
        // motorController.setOutputLimits(0, 0, gains.PEAK_OUTPUT, -gains.PEAK_OUTPUT);

        /* Set Motion Magic gains in slot0 - see documentation */
        motorController.setPID(gains);

        /* Set acceleration and vcruise velocity - see documentation */
        motorController.setMotionSpeed(15000 * Constants.DRIVE_GEARBOX_RATIO, 400 * Constants.DRIVE_GEARBOX_RATIO);
        /* Zero the sensor once on robot boot up */
        motorController.setEncoderPosition(0);
    }
}
