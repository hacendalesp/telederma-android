package com.telederma.gov.co.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.telederma.gov.co.BaseActivity;
import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.Parametro;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.SpeechRecognitionNotAvailable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Clase con métodos utilitarios de uso general
 * <p>
 * Autor: Daniel Hernández
 */
public final class Utils {

    private static final String TAG_ERROR_UTILS = "TAG_ERROR_UTILS";

    private static final String VALID_EMAIL_REGEX = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
    private static final String VALID_CONTRASENA_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
    private static final String DATE_FORMAT_PATTERN_IN = "dd/MM/yyyy";
    private static final String DATE_FORMAT_PATTERN_OUT = "dd/MM/yyyy";

    public static final byte MSJ_INFORMACION = 0;
    public static final byte MSJ_ADVERTENCIA = 1;
    public static final byte MSJ_ERROR = 2;

    public static Snackbar snackbar;

    private static Utils instance;
    private Context contexto;

    private Utils() {
    }

    public static Utils getInstance(Context contexto) {
        if (instance == null)
            instance = new Utils();

        instance.contexto = contexto;

        return instance;
    }

    public boolean validarEmail(String email) {
        final Matcher matcher = Pattern.compile(VALID_EMAIL_REGEX).matcher(email);

        return matcher.matches();
    }

//    public boolean validarContrasena(String contrasena) {
//        final Matcher matcher = Pattern.compile(VALID_CONTRASENA_REGEX).matcher(contrasena);
//
//        return matcher.matches();
//    }

    public boolean validarContrasena(String contrasena) {
        Boolean valid = false;
        if(contrasena != null){
            if(contrasena.length() >= 6) {
             valid = true;
            }
        }
        return valid;
    }

    /**
     * Método que recibe los campos requeridos del formulario y muestra error si no tienen información
     *
     * @param campos
     * @return
     */
    public boolean validarCamposRequeridos(View... campos) {
        final String msjCampoRequerido = contexto.getResources().getString(R.string.campo_requerido);
        View campoFocus = null;
        int index = 0, i=0;
        for (View campo : campos) {
            if (campo instanceof EditText) {
                if (TextUtils.isEmpty(((EditText) campo).getText())) {
                     if(campoFocus == null){ campoFocus = campo; index = i;}
                    mostrarCampoConError(campo, msjCampoRequerido);
                }
            } else if (campo instanceof Spinner) {
                if (((Spinner) campo).getSelectedItem() == null ) {
                     if(campoFocus == null){ campoFocus = campo; index = i;}
                    mostrarCampoConError(campo, msjCampoRequerido);
                }
            } else if (campo instanceof CheckBox) {
                if (!((CheckBox) campo).isChecked()) {
                     if(campoFocus == null){ campoFocus = campo; index = i;}
                    mostrarCampoConError(campo, msjCampoRequerido);
                }
            }

            i +=1;
        }

        if (campoFocus != null) {
            campoFocus.requestFocus();
//            if(index == 0){
//                moveScroll(campoFocus, index);
//            }else if(campos.length > 0){
//                campoFocus = campos[index-1];
//                moveScroll(campoFocus, index);
//            }else{
//                campoFocus.requestFocus();
//            }

            if (contexto instanceof BaseActivity)
                ((BaseActivity) contexto).mostrarMensaje(R.string.msj_campos_requeridos, MSJ_ADVERTENCIA);
        }

        return campoFocus == null;
    }

    public void mostrarCampoConError(View view,  String message) {


        if (view instanceof EditText) {
            ((EditText) view).setError(message);
        } else if (view instanceof Spinner) {
            TextView errorText = (TextView) ((Spinner) view).getSelectedView();
            if(message != null && errorText != null) {
                errorText.setError(message);
                errorText.setTextColor(Color.RED);
            }
        } else if (view instanceof CheckBox) {

            ((CheckBox) view).setError(message);
        }


    }

    public void mostrarCamposConErrores(Map<View, String> camposConErrores) {
        Iterator<Map.Entry<View, String>> i = camposConErrores.entrySet().iterator();
        Map.Entry<View, String> campoError = null;
        View campoFoco = null;

        while (i.hasNext()) {
            campoError = i.next();
            if (campoError.getKey() instanceof EditText) {
                if (campoFoco == null)
                    campoFoco = campoError.getKey();

                ((EditText) campoError.getKey()).setError(campoError.getValue());
            } else if (campoError.getKey() instanceof Spinner) {
                if (campoFoco == null)
                    campoFoco = campoError.getKey();

                TextView errorText = (TextView) ((Spinner) campoError.getKey()).getSelectedView();
                if(campoError.getValue() != null && errorText != null) {
                    errorText.setError(campoError.getValue());
                    errorText.setTextColor(Color.RED);
                }
            } else if (campoError.getKey() instanceof CheckBox) {
                if (campoFoco == null)
                    campoFoco = campoError.getKey();

                ((CheckBox) campoError.getKey()).setError(campoError.getValue());
            }
        }

//        // Pone el foco en el primer campo con error
        if (campoFoco != null)
            campoFoco.requestFocus();
    }

    private void moveScroll(View v, int index){
        Log.i(this.getClass().getSimpleName(), "v==y==>moveScroll==>index==>"+index);
        ScrollView sv_content = null;

        View v_parent = (View) v.getParent();
        while (v_parent != null && !(v_parent instanceof ScrollView)){
            v_parent = (View) v_parent.getParent();
            if(v_parent instanceof ScrollView) {
                sv_content = (ScrollView) v_parent;
                break;
            }
        }

        if(sv_content != null) {

            ViewTreeObserver vto = v.getViewTreeObserver();
            ScrollView finalSv_content = sv_content;
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    Log.i(this.getClass().getSimpleName(), "v==y==>moveScroll==>y==>" + y);
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    Log.i(this.getClass().getSimpleName(), "v==y==>" + y);//3543, 2453, 1908
                    v.getRootView().post(new Runnable() {
                        @Override
                        public void run() {
                            if (index == 0)
                                finalSv_content.scrollTo(0, 0);
                            else
                                finalSv_content.scrollTo(0, y);
                        }
                    });

                }
            });
        }
    }

    public void crearRadios(List<Parametro> parametros, RadioGroup grupo) {
        for (Parametro parametro : parametros)
            agregarRadioButton(
                    parametro.getNombre(), parametro.getValor(), ContextCompat.getColor(contexto, R.color.black),
                    new RadioButton(contexto), grupo
            );

        grupo.check(parametros.get(parametros.size() - 1).getValor());
    }

    public void crearCheckbox(List<Parametro> parametros, ViewGroup view ) {
        for (Parametro parametro : parametros)
            agregarCheckBox(
                    parametro.getNombre(), parametro.getValor(), false, new CheckBox(contexto), view
            );
    }

    public CheckBox agregarCheckBox(String texto, Integer id, boolean checked, CheckBox checkBox, ViewGroup view) {
        checkBox.setText(texto);
        checkBox.setId(id);
        checkBox.setTag("checkbox_"+id);
        checkBox.setChecked(checked);
        view.addView(checkBox);
        return  checkBox;
    }

    public Map<CheckBox,EditText> crearCheckBoxWithEditText(List<Parametro> parametros,List<Integer> withEditText, ViewGroup view ) {
        Map<CheckBox,EditText> data_check_edittext = new HashMap<>();
        for (Parametro parametro : parametros) {
            CheckBox ch =  agregarCheckBox(parametro.getNombre(), parametro.getValor(), false, new CheckBox(contexto), view);
            if(withEditText.contains(parametro.getValor())) {
               EditText editText =  agregarEditTextAndTextView(contexto.getResources().getString(R.string.otro_diagnostiaco_otro_cual), view.getId() * parametro.getValor(), view);
                data_check_edittext.put(ch,editText);
            }
        }
        return data_check_edittext;
    }


    public  EditText agregarEditTextAndTextView(String texto , Integer id , ViewGroup view )
    {
        LinearLayout mainLinearLayout = new LinearLayout(contexto);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLinearLayout.setLayoutParams(mainParams);
        mainLinearLayout.setForegroundGravity(Gravity.CENTER);

        LinearLayout firstChildLinearLayout = new LinearLayout(contexto);
        firstChildLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams firstChildParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        firstChildLinearLayout.setLayoutParams(firstChildParams);
        firstChildLinearLayout.setVisibility(View.GONE);

        TextView textView = new TextView(contexto);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        txtParams.setMarginStart(5);
        txtParams.setMarginEnd(10);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(txtParams);
        textView.setText(texto);

        EditText editText = new EditText(new ContextThemeWrapper(contexto, R.style.telederma_edit_text));
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etParams.weight = 1;
        editText.setBackground(null);
        editText.setLayoutParams(etParams);
        editText.setTag(id);
        editText.setHint(R.string.otro_diagnostiaco_otro_diagnotico);
        firstChildLinearLayout.addView(textView, 0);
        firstChildLinearLayout.addView(editText, 1);
        view.addView(firstChildLinearLayout);
        return editText;
    }

    public void agregarRadioButton(String texto, Integer id, Integer textColor, RadioButton radioButton, ViewGroup view) {
        radioButton.setText(texto);
        radioButton.setId(id);

        if (textColor != null)
            radioButton.setTextColor(textColor);

        radioButton.setPadding(0, 0, 30, 0);
        view.addView(radioButton);
    }

    public void agregarRadioButton(@StringRes Integer texto, Integer id, Integer textColor, RadioButton radioButton, ViewGroup view) {
        agregarRadioButton(contexto.getString(texto), id, textColor, radioButton, view);
    }

    public Date obtenerFecha(@NonNull String fecha) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_PATTERN_IN).parse(fecha);
        } catch (ParseException e) {
            Log.d(TAG_ERROR_UTILS, "Ocurrió un error dando formato a la fecha", e);
        }

        return null;
    }

    public String obtenerFecha(@NonNull Date fecha) {
        return new SimpleDateFormat(DATE_FORMAT_PATTERN_OUT).format(fecha);
    }

    public String obtenerValorBooleano(Boolean valor) {
        return Boolean.TRUE.equals(valor) ?
                contexto.getResources().getString(R.string.valor_booleano_true) :
                contexto.getResources().getString(R.string.valor_booleano_false);
    }

    /*
     * Source:
     * https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
     */
    public String millisecondsToHHMMSS(long milliseconds) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
        );
    }

    /*
     * Source:
     * https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
     */
    public String millisecondsToMMSS(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
        );
    }


    public void cambiarVisibilidad(int visibilidad, View... campos) {
        for (View view : campos) {
            view.setVisibility(visibilidad);
            if (view instanceof EditText)
                ((EditText) view).setText("");

            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    if (innerView instanceof EditText)
                        ((EditText) innerView).setText("");
                }
            }
        }
    }

    public void mostrarMensaje(Activity contexto, String mensaje, int tipoMensaje, int duracion, Snackbar.Callback callback) {
        final InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Activity.INPUT_METHOD_SERVICE);
        final View rootView = ((ContentFrameLayout) contexto.findViewById(android.R.id.content)).getChildAt(0);
        final int idColor;

        if (snackbar != null) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(rootView, mensaje, duracion);

        switch (tipoMensaje) {
            case MSJ_INFORMACION:
                idColor = R.color.green;
                break;

            case MSJ_ADVERTENCIA:
                idColor = R.color.orange;
                break;

            case MSJ_ERROR:
                idColor = R.color.red;
                break;

            default:
                idColor = R.color.green;
        }

        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(contexto, idColor));
        if (callback != null) snackbar.addCallback(callback);
        //if (callback != null) new OnShowTask().execute(callback);

        snackbar.show();
        snackbar.setAction("\u2718", v -> {}).setActionTextColor(Color.WHITE);
    }

    private class OnShowTask extends AsyncTask<MensajeCallBack, Void, Boolean> {

        private Exception e;
        MensajeCallBack callback;

        @Override
        protected Boolean doInBackground(MensajeCallBack... callbacks) {
            callback = callbacks[0];

            return callback.onShown();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            this.callback.update();
            if (Utils.snackbar != null) Utils.snackbar.dismiss();

            if(this.e != null || !result) {
                Log.d(Constantes.TAG_ERROR_MENU_ACTIVITY, "Ha ocurrido un error", e);
                mostrarMensaje((Activity) snackbar.getContext(),
                        contexto.getResources().getString(R.string.msj_error),
                        MSJ_ERROR, Snackbar.LENGTH_LONG, null
                );
            }
        }
    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) contexto.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            return "";
        String imei = "";
        if (Build.VERSION.SDK_INT >= 29)
        {
            imei = Settings.Secure.getString(this.contexto.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            imei = telephonyManager.getDeviceId();
        }

        if (imei != null && !imei.isEmpty())
            return imei;
        return android.os.Build.SERIAL;
    }

    public static boolean verificarEjecucionDelServicio(Class<?> serviceClass, Context contexto) {
        ActivityManager manager = (ActivityManager) contexto.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void showGeneralMessage(Context contexto, String message) {
        if(message != null) {
            View rootView = ((Activity)contexto).getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).setDuration(7000);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(contexto, R.color.green));

            View mySbView = snackbar.getView();
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextSize(10f);


            snackbar.show();
            snackbar.setAction("\u2718", v -> {}).setActionTextColor(Color.WHITE);

        }
    }

    public void speechText(View contentView){
        //contexto = context;
        View btn_microphone = contentView.findViewById(R.id.btn_speech);

        btn_microphone.setOnClickListener(view -> {
            try {
                Speech.getInstance().startListening(contentView, new VoiceText(contexto, contentView));
            } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                speechRecognitionNotAvailable.printStackTrace();
            } catch (GoogleVoiceTypingDisabledException e) {
                e.printStackTrace();
            }

        });

    }
}
