package com.telederma.gov.co;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.telederma.gov.co.adapters.SliderAdapter;
import com.telederma.gov.co.tasks.SavePhotoTask;

import java.util.ArrayList;
import java.util.Collections;

public class CamaraDermatoscopia extends BaseActivity implements View.OnClickListener {

    private CameraView camera;
    private ArrayList<byte[]> photos = new ArrayList<>();
    RecyclerView recyclerView;
    SliderAdapter sliderAdapter;
    private SavePhotoTask saveTask = null;
    private SavePhotoTask.Listener saveListener;
    private ArrayList<String> names_photos = new ArrayList<String>();
    public static final String ACTION_CAMARA_SLIDER = "com.telederma.gov.co.CamaraDermatoscopia";
    public static final String ARG_URL_IMAGEN ="url_imagen";
    private String imagenSeleccionada;
    Integer cantidad_imagenes, request_code;
    final Integer maxImage = 6 ;
    Boolean unica = false;
    Button zoom_1, zoom_2;
    public static final int REQUEST_CODE_AGREGAR_IMAGENES = 400;
    public static final int RESULT_CODE_EXITO_AGRGANDO_IMAGENES = 401;
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";
    Boolean isFlashActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        //Glide.get(this).getBitmapPool().clearMemory();
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        setContentView(R.layout.activity_camara);

        // se pasa el porcentaje de brillo requerido
        setBrightness(100);


        // TODO: 4/8/19 mostrar con estilo 
        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo_seleccion), Toast.LENGTH_SHORT).show();

        ((LinearLayout) this.findViewById(R.id.llCamaraDermatoscopia)).setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setTitle(R.string.fotos_patologia);
        camera = findViewById(R.id.camera);
        camera.addCameraListener(new CameraListener() {

            public void onPictureTaken(byte[] jpeg) {
                onPicture(jpeg);
            }

        });
        camera.setFlash(Flash.OFF);
        findViewById(R.id.capturePhoto).setOnClickListener(this);
        zoom_1 = findViewById(R.id.zoom_1);
        zoom_1.setOnClickListener(this);
        zoom_2 = findViewById(R.id.zoom_2);
        zoom_2.setOnClickListener(this);
        findViewById(R.id.zoom_4).setOnClickListener(this);
        findViewById(R.id.zoom_6).setOnClickListener(this);
        findViewById(R.id.zoom_8).setOnClickListener(this);
        findViewById(R.id.action_save_photos).setOnClickListener(this);
        findViewById(R.id.action_disable_flash).setOnClickListener(this);


        saveListener = new SavePhotoTask.Listener() {
            @Override
            public void onPostExecuted(String url) {
                names_photos.add(url);
                if(unica)
                    savePhotos();
            }
        };
        setSupportActionBar(toolbar);
        cantidad_imagenes = getIntent().getIntExtra("cantidad_imagenes", 1);
        request_code = getIntent().getIntExtra("request_code", 200);
        unica = getIntent().getBooleanExtra("unica", false);

        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(Frame frame) {
                frame.release();
            }
        });

        this.imagenSeleccionada =  getIntent().getStringExtra(ARG_URL_IMAGEN);

        validateFlash();

    }

    public void validateFlash() {
        try {
            if (isFlashActive) {
                ((ImageView)findViewById(R.id.img_flash)).setImageDrawable(getResources().getDrawable(R.drawable.ico_flash_on));
            } else {
                ((ImageView)findViewById(R.id.img_flash)).setImageDrawable(getResources().getDrawable(R.drawable.ico_flash_off));
            }


        } catch (Exception e) {

        }
    }

    synchronized private void onPicture(byte[] capture) {
        saveTask = new SavePhotoTask(capture, saveListener);
        saveTask.execute();
        photos.add(capture);

        showImages();

    }

    private void showImages(){
        ArrayList<byte[]> photos_rever = (ArrayList<byte[]>) photos.clone();
        Collections.reverse(photos_rever);
        sliderAdapter = new SliderAdapter(photos_rever, photos.size(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);

                ArrayList<String> clone = (ArrayList<String>) CamaraDermatoscopia.this.names_photos.clone();
                Collections.reverse(clone);
                String item =  clone.get(itemPosition);

            }
        }, getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new CardSliderLayoutManager(50, 200, 12));
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.invalidate();

    }

    private void capturePhoto() {
        /*if (names_photos.size() >= maxImage )
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo_enviar), Toast.LENGTH_SHORT).show();
        camera.capturePicture();*/

        if (names_photos.size() < maxImage )
           camera.capturePicture();
        else
          Toast.makeText(getApplicationContext(), getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capturePhoto:
                capturePhoto();
                break;
            case R.id.zoom_1:
                camera.setZoom(0f);
                zoom_1.setAlpha(1f);
                zoom_2.setAlpha(0.5f);
                break;
            case R.id.zoom_2:
                camera.setZoom(0.2f);
                zoom_1.setAlpha(.5f);
                zoom_2.setAlpha(1f);
                break;
            case R.id.zoom_4:
                camera.setZoom(0.4f);
                break;
            case R.id.zoom_6:
                camera.setZoom(0.6f);
                break;
            case R.id.zoom_8:
                camera.setZoom(0.8f);
                break;
            case R.id.action_save_photos:
                savePhotos();
                break;
            case R.id.action_disable_flash:
                turnFlash();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        camera.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu_camera, menu);
        // TODO: 3/14/19 commented icon save photo from menu header 
//        Drawable drawable = menu.findItem(R.id.action_save_photos).getIcon();
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        }
        return true;
    }

    public void turnFlash() {
        isFlashActive = !isFlashActive;
        if (isFlashActive) {
            camera.setFlash(Flash.TORCH);
        } else {
            camera.setFlash(Flash.OFF);
        }
        validateFlash();
    }


    public void savePhotos() {
        Log.i(getLocalClassName(), "names_photos size ==>"+names_photos.size()+ " cantidad de imagenes ==> "+cantidad_imagenes);
        if (names_photos.size() > cantidad_imagenes - 1) {
            Intent intent = new Intent(ImagenesCamaraDermatoscopia.ACTION_IMAGENES_CAMARA_DERMATOSCOPIA);
            intent.putExtra("imagenes", names_photos);
            intent.putExtra(ARG_URL_IMAGEN, this.imagenSeleccionada);

            if(getIntent().hasExtra(ARG_IMAGEN_NOMBRE_PARTE)){
                intent.putExtra(ARG_IMAGEN_NOMBRE_PARTE, getIntent().getStringExtra(ARG_IMAGEN_NOMBRE_PARTE));
            }

            intent.putExtra("id_parte", getIntent().getStringExtra("id_parte"));
            startActivityForResult(intent, REQUEST_CODE_AGREGAR_IMAGENES);



        } else
            Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.nueva_consulta_registro_imagenes_validacion),cantidad_imagenes.toString()), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AGREGAR_IMAGENES) {
            if (resultCode == RESULT_CODE_EXITO_AGRGANDO_IMAGENES) {

                Intent intent = new Intent();
                ArrayList<String> imagenes_dermatoscopia = data.getStringArrayListExtra("imagenes_dermatoscopia");
                intent.putExtra("imagenes_dermatoscopia", imagenes_dermatoscopia);
                setResult(RESULT_CODE_EXITO_AGRGANDO_IMAGENES, intent);
                finish();


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_save_photos:
//                savePhotos();
//                return true;
            case 16908332:
                //Intent data = new Intent();
                //data.putExtra("id_parte", getIntent().getStringExtra("id_parte"));
                //setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}