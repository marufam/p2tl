package com.projek.p2pl.pemeriksaan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.projek.p2pl.Config;
import com.projek.p2pl.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class Pelanggan extends AbstractStep {

    private int i = 1;
    private Button button;
    private final static String CLICK = "click";
    private GoogleMap map;

    MapView mMapView;
    private GoogleMap googleMap;
    double myLat = 0, myLng = 0;
    Location loc;
    LocationManager locationManager;
    String mprovider;
    Circle mapCircle;
    private Marker myMarker;
    //    ApiInterface mApiInterface;
    private LocationListener listener;

    //
    Button btn_foto;
    ImageView img1, img2, img3, img4, img5, img6;
    private Uri fileUri;
    Uri file1, file2, file3, file4, file5, file6;
    int rCode = 100;

    private static final String TAG = "Permission";
    public static final int MEDIA_TYPE_IMAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.pelanggan, container, false);

        img1 = (ImageView) rootView.findViewById(R.id.img1);
        img2 = (ImageView) rootView.findViewById(R.id.img2);
        img3 = (ImageView) rootView.findViewById(R.id.img3);
        img4 = (ImageView) rootView.findViewById(R.id.img4);
        img5 = (ImageView) rootView.findViewById(R.id.img5);
        img6 = (ImageView) rootView.findViewById(R.id.img6);
        btn_foto = (Button) rootView.findViewById(R.id.btn_foto);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        mMapView = (MapView) rootView.findViewById(R.id.posisi);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

//        Toast.makeText(getContext(), "jarak tempuh saat ini : "+spbu.getString("jarak_tempuh",null), Toast.LENGTH_SHORT).show();
        prepareallmap();
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                if (myLat == 0 && myLng == 0) {
//                    Toast.makeText(getContext(), "" + location.getLatitude() + "  " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    myLat = location.getLatitude();
                    myLng = location.getLongitude();
                } else {
                    myLat = location.getLatitude();
                    myLng = location.getLongitude();
                }

                if (mapCircle != null) {
                    mapCircle.remove();
                }

//                googleMap.clear();

//                Toast.makeText(mStepper, ""+myLat+","+myLng, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
//                prepareallmap();
            }

            @Override
            public void onProviderEnabled(String s) {


            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();

        isCameraPermissionGranted();
        isReadPermissionGranted();
        return rootView;
    }


    public void prepareallmap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                CameraPosition cameraPosition;
                // For dropping a marker at a point on the Map
                if (loc != null) {
                    myLat = loc.getLatitude();
                    myLng = loc.getLongitude();
                }
                LatLng myLocation = new LatLng(myLat, myLng);
                cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                }
//                Toast.makeText(getContext(), ""+googleMap.getMyLocation().getLatitude(), Toast.LENGTH_SHORT).show();

                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
//                        Toast.makeText(getContext(), ""+googleMap.getMyLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        if (locationManager != null) {
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            prepareallmap();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        prepareallmap();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(CLICK, i);

//        state.putParcelable("file_uri", fileUri);
    }

    @Override
    public String name() {
        return "Step " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
        Toast.makeText(mStepper, "onNext", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "Form Pelanggan";
    }

    @Override
    public boolean nextIf() {
        return i > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, rCode);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // capture foto meter
//        if (requestCode == CAMERA_CAPTURE_METER_REQUEST_CODE) {
        if (resultCode == RESULT_OK) {

            // successfully captured the image
            Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileUri.getPath()), 300, 300);
//            Bitmap resized = BitmapFactory.decodeFile(fileUri.getPath());

            if (rCode == 100){
                file1 = fileUri;
                img1.setImageBitmap(resized);
            } else if(rCode == 200){
                file1 = fileUri;
                img2.setImageBitmap(resized);
            } else if(rCode == 300){
                file3 = fileUri;
                img3.setImageBitmap(resized);
            } else if(rCode == 400){
                file4 = fileUri;
                img4.setImageBitmap(resized);
            } else if(rCode == 500){
                file5 = fileUri;
                img5.setImageBitmap(resized);
            } else if(rCode == 600){
                file6 = fileUri;
                img6.setImageBitmap(resized);
            }

            rCode += 100;

            if (rCode <= 600)
                captureImage();

        } else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
//            Toast.makeText(getApplicationContext(),
//                    "User cancelled image capture", Toast.LENGTH_SHORT)
//                    .show();
            Toast.makeText(mStepper, "Batal mengambil gambar", Toast.LENGTH_SHORT).show();

        } else {
            // failed to capture image
//            Toast.makeText(getApplicationContext(),
//                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                    .show();
            Toast.makeText(mStepper, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
        }

//        }
        // capture foto kedapatan
//        else if (requestCode == CAMERA_CAPTURE_KEDAPATAN_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                // successfully captured the image
//                fileKedapatan = fileUri;
//
//                Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileUri.getPath()), 300, 300);
//                img_kedapatan.setImageBitmap(resized);
//
//            } else if (resultCode == RESULT_CANCELED) {
//
//                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//
//            } else {
//                // failed to capture image
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }

//        }
    }


    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public  boolean isCameraPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (mStepper.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(mStepper, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public  boolean isReadPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mStepper.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Read Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Read Permission is revoked");
                ActivityCompat.requestPermissions(mStepper, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Read Permission is granted");
            return true;
        }
    }
}