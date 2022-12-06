package com.telederma.gov.co.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

import static com.telederma.gov.co.utils.Constantes.TAG_DESCARGAR_ARCHIVO;

/**
 * Created by Daniel Hernández on 3/08/2018.
 */

public class FileUtils {

    public static final String RAW_DIR = "raw";
    public static final String UTF_8 = "UTF-8";

    static public boolean existeArchivoLocal(String rutaAbsoluta) {
        return new File(rutaAbsoluta).exists();
    }

    static public void descargarArchivo(ArchivoDescarga archivoDescarga,
                                        Consumer<Integer> actualizarProgreso) throws IOException {
        URL url;
        if(archivoDescarga.url.contains("http")) {
            url = new URL(archivoDescarga.url);
        } else {
            url = new URL("http://190.25.231.102" + archivoDescarga.url);
        }

        URLConnection conexion = url.openConnection();
        conexion.connect();
        int lenghtOfFile = conexion.getContentLength();
        Log.d(TAG_DESCARGAR_ARCHIVO, String.format("Tamaño de archivo: %s bytes", lenghtOfFile));
        InputStream input = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream(archivoDescarga.getRutaAbsoluta());
        byte data[] = new byte[1024];
        long total = 0;
        int count;

        while ((count = input.read(data)) != -1) {
            total += count;
            actualizarProgreso.accept((int) ((total * 100) / lenghtOfFile));
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
    }

    /**
     * Método que recibe una imagen la retorna su valor codificado en base64
     * <p>
     * Autor: Daniel Hernández
     *
     * @param imagen
     * @return
     */
    static public String codificarImagenABase64(Bitmap imagen) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    static public String codificarFileBase64(File file) {
        try {
            InputStream inputStream = null;
            inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output64.close();

            return "data:image/png;base64,"+output.toString();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Método que recibe una imagen codificado en base64, la decodifica y retorna el Bitmap
     * <p>
     * Autor: Daniel Hernández
     *
     * @param imagen
     * @return
     */
    static public Bitmap decodificarImagenDeBase64(String imagen) {
        final byte[] byteArray = Base64.decode(
                imagen.substring(imagen.indexOf(',') + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Método que recibe una imagen y la guarda en la memoria del teléfono con el nombre recibido
     * <p>
     * Autor: Daniel Hernández
     *
     * @param imagen
     * @param nombre
     * @throws IOException
     */
    static public void guardarImagenTemporal(Bitmap imagen, String nombre) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.WEBP, 100, bytes);
        FileOutputStream fo = new FileOutputStream(String.format(
                Constantes.FORMATO_DIRECTORIO_ARCHIVO, Constantes.DIRECTORIO_TEMPORAL, nombre
        ), false);
        fo.write(bytes.toByteArray());
        fo.close();
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * Método que retorna la imagen guardada en la memoria del teléfono si existe con el nombre recibido
     * <p>
     * Autor: Daniel Hernández
     *
     * @param imagen
     * @return
     * @throws FileNotFoundException
     */
    static public Bitmap obtenerImagenTemporal(String imagen) throws FileNotFoundException {
        return BitmapFactory.decodeStream(new FileInputStream(String.format(
                Constantes.FORMATO_DIRECTORIO_ARCHIVO, Constantes.DIRECTORIO_TEMPORAL, imagen
        )));
    }

    public static File decodificarFileDeBase64(final Context context, final String base64_data, String path) {
        File file = new File(path);
        try {
            //Decode Base64 back to Binary format
            byte[] decodedBytes = Base64.decode(base64_data, Base64.DEFAULT);

            //Save Binary file to phone
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(decodedBytes);

            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static void eliminarArchivos(File fileOrDir) {
        if (fileOrDir.isDirectory())
            for (File child : fileOrDir.listFiles())
                eliminarArchivos(child);

        fileOrDir.delete();
    }

    public static String leerArchivoRaw(final Context contexto, String nombre) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                contexto.getResources().openRawResource(
                        contexto.getResources().getIdentifier(nombre, RAW_DIR, contexto.getPackageName())
                ), UTF_8))
        ) {
            reader.lines().forEach(line -> sb.append(line).append(" "));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String leerArchivo(@NonNull String rutaAbsoluta) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaAbsoluta))) {
            reader.lines().forEach(line -> sb.append(line).append(" "));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String saveImage(Bitmap imagen, String path, String name) {
        String respuesta = "";
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            File destination = new File(path + "/" + name);
            FileOutputStream fo;
            respuesta = destination.getPath();
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(outputStream.toByteArray());
            fo.close();
        } catch (IOException e) {
            String l = "";
        }
        return respuesta;

    }

}
