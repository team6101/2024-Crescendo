package frc.robot;

public class Constants {

    /*DRIVE TRAIN MOTOR IDs*/
    public static final int DRIVE_LEFT_PARENT_ID = 0;
    public static final int DRIVE_LEFT_CHILD_ID = 1;
    public static final int DRIVE_RIGHT_PARENT_ID = 2;
    public static final int DRIVE_RIGHT_CHILD_ID = 3;

    /*CONTROLLER PORT IDs*/
    public static final int DRIVER_CONTROLLER_ID = 0;

    /*START OF VISION CONSTANTS */
    public static final int CAMERA_HEIGHT = 480;
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_BIT_CORRECTION_AMOUNT = 0;
    /**This is the amount of frames in a row that an april tag needs in frame to be to be considered confidently an april tag */
    public static final int APRIL_TAG_CONFIDENCE_FRAMES = 3;
}
