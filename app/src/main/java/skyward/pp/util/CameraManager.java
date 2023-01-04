package skyward.pp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.Surface;

public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();
    private Context context;

    public CameraManager(Context ctx) {
        context = ctx;

    }

    /**
     * Check if this device has a camera
     */
    public boolean checkCameraHardware() {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Check if this device has a front camera
     */
    public boolean checkFrontCamera() {

        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }

    /**
     * A safe way to get an instance of the Camera object.
     *
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {

            c = Camera.open(CameraInfo.CAMERA_FACING_BACK); // attempt to
            // get a Camera
            // instance

        } catch (Exception e) {
            e.printStackTrace();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * This method id used to set the orientation of Camera to take picture.
     *
     * @param activity - The Activity in which Camera needs to be opened.
     * @param cameraId - Front or back camera ID.
     * @param camera   - Camera object to which the orientation needs to be fixed.
     */
    public void setCameraDisplayOrientation(Activity activity, int cameraId,
                                            Camera camera) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
