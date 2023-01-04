package skyward.pp.util;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = CameraPreview.class.getSimpleName();
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Camera.Size mPreviewSize;
	private Camera.Size mPictureSize;
	private List<Camera.Size> mSupportedPictureSizes;
	private List<Camera.Size> mSupportedPreviewSizes;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSupportedPreviewSizes = mCamera.getParameters()
				.getSupportedPreviewSizes();
		mSupportedPictureSizes = mCamera.getParameters()
				.getSupportedPictureSizes();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		// set preview size and make any resize, rotate or
		// reformatting changes here
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		parameters.setPictureSize(mPictureSize.width, mPictureSize.height);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
		// start preview with new settings

		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalSize(mSupportedPreviewSizes, widthMeasureSpec, heightMeasureSpec);
			mPictureSize = getOptimalSize(mSupportedPictureSizes, widthMeasureSpec, heightMeasureSpec);
		}
	}

	private Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) {
		Camera.Size result = null;
		for (Camera.Size size : sizes) {
			if (size.width <= w && size.height <= h) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return (result);
	}
}