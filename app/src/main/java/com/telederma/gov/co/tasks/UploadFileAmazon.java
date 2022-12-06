package com.telederma.gov.co.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.database.DBHelper;
import com.telederma.gov.co.modelo.ArchivosSincronizacion;
import com.telederma.gov.co.modelo.Credencial;
import com.telederma.gov.co.modelo.DataSincronizacion;
import com.telederma.gov.co.receivers.SincronizacionBroadcast;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.DBUtil;
import com.telederma.gov.co.utils.Session;
import com.telederma.gov.co.utils.Utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UploadFileAmazon extends AsyncTask<ArchivosSincronizacion, String, String> {
    public static Context contexto;
    public static Credencial credencial;

    /**
     * The shared preferences object for the application.
     */
    private static SharedPreferences appSharedPrefs;

    /**
     * The shared preferences editor.
     */
    private static Editor prefsEditor;

    private static final String APP_SHARED_PREFS = "pref_telederma";

    // User name (make variable public to access from outside)
    public static final String KEY_PREF = "proceso_usando_conexion";


    @Override
    protected String doInBackground(ArchivosSincronizacion... params) {
        ArchivosSincronizacion archivo = params[0];
        try
        {

            String fechaEnvio = getDate();
            setFechaPreferences(fechaEnvio);

            Log.e("USANDO_PROCESO", fechaEnvio);

            String fileName = credencial.getApi_bucket_directory()+ archivo.getTable() + System.currentTimeMillis() + "." + archivo.getExtension();
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(credencial.getApi_access_key(), credencial.getApi_secret_key()));
            s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
            File file = new File(archivo.getPath());
            if (file.exists())
            {
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encodedImage = "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);
                //String aaaa = "data:image/png;base64,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";

                //send(aaaa);
                OkHttpClient client = new OkHttpClient();

                String url = Constantes.API_REST_URL_BASE + "api/v1/injuries/individual_image.json";

                String nameKey = "";
                if (archivo.getTable().equals("imagen_lesion")) {
                    nameKey = "photo";
                } else if (archivo.getTable().equals("imagen_anexo")) {
                    nameKey = "annex_url";
                }

                Log.e("EMPEZANDO ENVIO", "EMPEZANDO ENVIO");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(nameKey, encodedImage)
                        .addFormDataPart("user_token", Session.getInstance(contexto).getCredentials().getToken())
                        .addFormDataPart("user_email", Session.getInstance(contexto).getCredentials().getEmail())
                        .build();

                Request request = new Request.Builder()
                        .addHeader("IMEI", Utils.getInstance(TeledermaApplication.getInstance()).getIMEI())
                        .url(url)
                        .post(requestBody)
                        .build();

                try{
                    okhttp3.Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        Log.e("IMAGEN ENVIADA", "IMAGEN ENVIADA");
                        String res = response.body().string();
                        JSONObject json = new JSONObject(res);

                        String ruta = json.getString("image");

                        Log.e("RUTA_URL", ruta);

                        new DataSincronizacion().set_campo(archivo, ruta);
                        file.delete();


                        setFechaPreferences("");
                        Log.e("LIBERANDO_PROCESO", "LIBERANDO_PROCESO");
                    }
                } catch (Exception e) {
                    setFechaPreferences("");
                    Log.e("ENVIO EXCEPCION1", e.toString());
                    new DataSincronizacion().actualizarArchivo(archivo.getId(), ArchivosSincronizacion.NOMBRE_CAMPO_STATUS, "0");
                }
                //PutObjectRequest por = new PutObjectRequest(credencial.getApi_bucket_name(), fileName,file );//.withCannedAcl(CannedAccessControlList.PublicRead);
                //s3Client.putObject(por);
                //GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(Constantes.API_AMAZON_BUCKET_NAME, fileName);
                //java.util.Date expiration = new java.util.Date();
                //long milliSeconds = expiration.getTime();
                //milliSeconds += 99000 * 60 * 60;
                //expiration.setTime(milliSeconds);
                //urlRequest.setExpiration(expiration);
                //s3Client.setObjectAcl(credencial.getApi_bucket_name(), fileName, CannedAccessControlList.Private);
                //URL url = s3Client.generatePresignedUrl(urlRequest);//URL con expiracion  url.toURI().toString()
                //String url = s3Client.getResourceUrl(credencial.getApi_bucket_name(), fileName);//URL normal
                //new DataSincronizacion().set_campo(archivo, url);
                //file.delete();
            }
            else
                    new DataSincronizacion().eliminarArchivoSincronizacion(archivo.getId());
        } catch (Exception e) {
            setFechaPreferences("");
            Log.e("ENVIO EXCEPCION1", e.toString());
            e.printStackTrace();
            new DataSincronizacion().actualizarArchivo(archivo.getId(), ArchivosSincronizacion.NOMBRE_CAMPO_STATUS, "0");
        }

        setFechaPreferences("");
        return "";
    }

    public static void setFechaPreferences(String value) {
        try {
            appSharedPrefs = TeledermaApplication.getInstance().getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, TeledermaApplication.getInstance().getApplicationContext().MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString(KEY_PREF, value);
            prefsEditor.commit();
        } catch (Exception e) {

        }
    }

    protected void onPostExecute(String feed) {

        Thread asyncThread = new Thread() {
            @Override
            public void run() {
                new DataSincronizacion().sincronizar(contexto);
            }
        };
        asyncThread.start();

    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(c.getTime());

        return currentTime;
    }


}

