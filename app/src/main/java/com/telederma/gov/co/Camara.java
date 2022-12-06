package com.telederma.gov.co;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.WorkerThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Size;
import com.otaliastudios.cameraview.SizeSelector;
import com.telederma.gov.co.adapters.CheckedImageArrayAdapter;
import com.telederma.gov.co.dialogs.ShowImagesDialog;
import com.telederma.gov.co.services.LogoutService;
import com.telederma.gov.co.tasks.SavePhotoTask;
import com.telederma.gov.co.adapters.SliderAdapter;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.telederma.gov.co.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Camara extends BaseActivity implements View.OnClickListener {

    private CameraView camera;
    private ArrayList<byte[]> photos = new ArrayList<>();
    RecyclerView recyclerView;
    SliderAdapter sliderAdapter;
    private SavePhotoTask saveTask = null;
    private SavePhotoTask.Listener saveListener;
    private ArrayList<String> names_photos = new ArrayList<String>();
    public static final String ACTION_CAMARA_SLIDER = "com.telederma.gov.co.Camara";
    Integer cantidad_imagenes, request_code;
    final Integer maxImage = 10 ;
    Boolean unica = false;
    Button zoom_1, zoom_2;
    private JSONObject imagenes_dermatoscopia;
    private ArrayList<Boolean> sonImagenesDermatoscopicas = new ArrayList<Boolean>();
    public static final String ARG_URL_IMAGEN ="url_imagen";
    public static final String ARG_POS_IMAGEN ="pos_imagen";
    public static final int REQUEST_CODE_IMAGEN = 200;
    public static final int RESULT_CODE_ELIMINAR_IMAGEN = 201;
    public static final int RESULT_CODE_EXITO_AGRGANDO_IMAGENES = 401;
    private Boolean verBotonesDermatoscopia;
    private Boolean anadirImagen;
    public static final String ARG_NAME_PARTE ="name_parte";
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";
    public static int RESULT_LOAD_IMAGE = 101;

    Boolean isFlashActive = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

       // Glide.get(this).getBitmapPool().clearMemory();

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        setContentView(R.layout.activity_camara);

        // se pasa el porcentaje de brillo requerido
        setBrightness(100);


        // TODO: 4/8/19 mostrar con estilo 
        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo_seleccion), Toast.LENGTH_SHORT).show();


        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setTitle(R.string.fotos_patologia);
        camera = findViewById(R.id.camera);
        camera.addCameraListener(new CameraListener() {

            public void onPictureTaken(byte[] jpeg) {
                onPicture(jpeg);
            }

        });
        findViewById(R.id.capturePhoto).setOnClickListener(this);
        findViewById(R.id.fileChooser).setOnClickListener(this);
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
                marcarImagenesdermatoscopicas();
                showImages();
                if(unica)
                    savePhotos();
            }
        };
        setSupportActionBar(toolbar);
        cantidad_imagenes = getIntent().getIntExtra("cantidad_imagenes", 1);
        request_code = getIntent().getIntExtra("request_code", 200);
        unica = getIntent().getBooleanExtra("unica", false);
        verBotonesDermatoscopia = getIntent().getBooleanExtra("verBotonesDermatoscopia", false);

        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(Frame frame) {
                frame.release();
            }
        });

        // This will be the size of pictures taken with takePicture().
        camera.setPictureSize(new SizeSelector() {
            @Override
            public List<Size> select(List<Size> source) {

                try {
                    int height = 3648;
                    int width = 2736;

                    //int height = 3264;
                    //int width = 2448;

                    //int height = 3264;
                    //int width = 1840;
                    //1024

                    List<Size> newList = new ArrayList<Size>();

                    for (int i = 0; i < source.size(); i++) {
                        Size size = source.get(i);

                        if (size.getHeight() == height && size.getWidth() == width) {
                            newList.add(size);
                            return newList;
                        }

                    }

                    newList.add(source.get(10));
                    return newList;

                } catch (Exception e) {
                    List<Size> newList = new ArrayList<Size>();
                    newList.add(source.get(10));
                    return newList;
                }




            }
        });

        //camera.setPictureSize(1736, 2312);

        this.imagenes_dermatoscopia = new JSONObject();

        this.anadirImagen = getIntent().getBooleanExtra("anadirImagen", false);

        if(this.anadirImagen){
            cargarImagenes();

        }

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

    public void cargarImagenes(){
        try{
            this.imagenes_dermatoscopia = new JSONObject(getIntent().getStringExtra("imagenes_dermatoscopia"));
            this.names_photos = getIntent().getStringArrayListExtra("imageUrls");

            for (int i = 0; i < names_photos.size(); i++){

                photos.add(pathToByte(names_photos.get(i)));
            }

            marcarImagenesdermatoscopicas();
            showImages();
        }catch (Exception e){

        }
    }

    public byte[] pathToByte(String path){
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }



    synchronized private void onPicture(byte[] capture) {
        saveTask = new SavePhotoTask(capture, saveListener);
        saveTask.execute();
        photos.add(capture);
        //showImages();

    }

    private void showImages(){
        ArrayList<byte[]> photos_rever = (ArrayList<byte[]>) photos.clone();

        Collections.reverse(photos_rever);
        sliderAdapter = new SliderAdapter(photos_rever, photos.size(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verBotonesDermatoscopia){
                    int itemPosition = recyclerView.getChildLayoutPosition(view);

                    ArrayList<String> clone = (ArrayList<String>)Camara.this.names_photos.clone();
                    Collections.reverse(clone);
                    openShowImageDermatoscopia(clone, itemPosition, true);
                }

            }
        }, getApplicationContext(), this.sonImagenesDermatoscopicas);
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

    private void fileChooser() {


        if (names_photos.size() < maxImage ) {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nueva_consulta_cam_mensaje_maximo), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capturePhoto:
                capturePhoto();
                break;
            case R.id.fileChooser:
                fileChooser();
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
            Intent data = new Intent();
            data.putExtra("imagenes", names_photos);
            data.putExtra("id_parte", getIntent().getStringExtra("id_parte"));

            if(!this.imagenes_dermatoscopia.toString().equals("{}")){
                data.putExtra("imagenes_dermatoscopia", this.imagenes_dermatoscopia.toString());
            }
            setResult(request_code, data);
            finish();
        } else
            Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.nueva_consulta_registro_imagenes_validacion),cantidad_imagenes.toString()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_save_photos:
//                savePhotos();
//                return true;
            case 16908332:
                Intent data = new Intent();
                data.putExtra("id_parte", getIntent().getStringExtra("id_parte"));
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openShowImageDermatoscopia(ArrayList<String> imagenes, int posicion, Boolean showButtonsDermoscopy){

        try{




            Intent intent = new Intent(ImagenDermatoscopia.ACTION_IMAGEN_DERMATOSCOPIA);
            intent.putExtra(ARG_URL_IMAGEN, imagenes);
            intent.putExtra(ARG_POS_IMAGEN, posicion);


            String nombreCuerpo = "";
            if(getIntent().hasExtra(ARG_NAME_PARTE)){
                intent.putExtra(ARG_IMAGEN_NOMBRE_PARTE, obtenerImagenesCuerpo(imagenes).toString());
            }
            intent.putExtra("nombreCuerpo", nombreCuerpo);
            intent.putExtra("showButtonsDermoscopy", showButtonsDermoscopy);
            intent.putExtra("imagenes_dermatoscopia", this.imagenes_dermatoscopia.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, REQUEST_CODE_IMAGEN);


        }catch (Exception e){

        }
    }

    public JSONObject obtenerImagenesCuerpo(ArrayList<String> imagenes){
        JSONObject Jimagenes = new JSONObject();
        try{

            for (String imagen : imagenes) {
                Jimagenes.put(imagen, getIntent().getStringExtra(ARG_NAME_PARTE));
            }
        }catch (Exception e){

        }

        return Jimagenes;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGEN) {
            if (resultCode == RESULT_CODE_ELIMINAR_IMAGEN) {
                String urlImagen = data.getStringExtra("url_imagen");

                eliminarImagen(urlImagen);
            } if (resultCode == RESULT_CODE_EXITO_AGRGANDO_IMAGENES) {

                try{
                    String urlImagen = data.getStringExtra("url_imagen");
                    ArrayList<String> imagenes_dermatoscopia = data.getStringArrayListExtra("imagenes_dermatoscopia");

                    JSONArray imagenes = new JSONArray();
                    for(String imagen: imagenes_dermatoscopia){
                        imagenes.put(imagen);
                    }
                    this.imagenes_dermatoscopia.put(urlImagen, imagenes);
                    marcarImagenesdermatoscopicas();
                    showImages();
                } catch (Exception e){

                }
            }
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {

            try {
                Uri selectedImage = data.getData();

                InputStream iStream =   getContentResolver().openInputStream(selectedImage);
                byte[] inputData = getBytes(iStream);

                saveTask = new SavePhotoTask(inputData, saveListener);
                saveTask.execute();
                photos.add(inputData);
            } catch (Exception e) {

            }

        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void eliminarImagen(String urlImagen) {

        int pos = -1;

        for(int i = 0; i < names_photos.size(); i++){
            if(names_photos.get(i).equals(urlImagen)){
                pos = i;
            }
        }

        if(pos != -1){

            ArrayList<String> cloneNames_photos = (ArrayList<String>) Camara.this.names_photos.clone();
            ArrayList<byte[]> clonephotos = (ArrayList<byte[]>) Camara.this.photos.clone();

            cloneNames_photos.remove(pos);
            clonephotos.remove(pos);

            names_photos = cloneNames_photos;
            photos = clonephotos;

            this.imagenes_dermatoscopia.remove(urlImagen);

            marcarImagenesdermatoscopicas();
            showImages();
        }

    }

    public void marcarImagenesdermatoscopicas(){
        ArrayList<String> cloneNames_photos = (ArrayList<String>) Camara.this.names_photos.clone();
        Collections.reverse(cloneNames_photos);
        this.sonImagenesDermatoscopicas = new ArrayList<Boolean>();

        for (int i = 0; i < cloneNames_photos.size(); i++){
            String name = cloneNames_photos.get(i);

            if (this.imagenes_dermatoscopia.has(name)){
                this.sonImagenesDermatoscopicas.add(true);
            } else {
                this.sonImagenesDermatoscopicas.add(false);
            }
        }
    }




}