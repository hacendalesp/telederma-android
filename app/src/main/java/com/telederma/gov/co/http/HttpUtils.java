package com.telederma.gov.co.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.telederma.gov.co.BuildConfig;
import com.telederma.gov.co.R;
import com.telederma.gov.co.TeledermaApplication;
import com.telederma.gov.co.modelo.BaseEntity;
import com.telederma.gov.co.utils.Constantes;
import com.telederma.gov.co.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase que contiene métodos utilitarios para el uso de los servicios web
 * <p>
 * Created by Daniel Hernández on 6/14/2018.
 */
public class HttpUtils {

    static class HeaderInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Log.e("IMEI", Utils.getInstance(TeledermaApplication.getInstance()).getIMEI() );
            Request request = original.newBuilder()
                    .addHeader("IMEI", Utils.getInstance(TeledermaApplication.getInstance()).getIMEI())
                    //.addHeader("IMEI", "866218034823287")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        }
    }

    /**
     * Método que crea y retorna un servicio web
     * <p>
     * Autor: Daniel Hernández
     *
     * @param servicio
     * @return
     */
    public static Object crearServicio(Class servicio) {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;



        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                //.sslSocketFactory(sslSocketFactory, trustManager)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constantes.API_REST_URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        builder.addConverterFactory(createGsonConverter());

        return builder.build().create(servicio);
    }

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    public static InputStream trustedCertificatesInputStream() {

        String source = "-----BEGIN CERTIFICATE-----\n" +
                "MIIGRDCCBSygAwIBAgIIC2rOhMiiEj0wDQYJKoZIhvcNAQELBQAwgbQxCzAJBgNV\n" +
                "BAYTAlVTMRAwDgYDVQQIEwdBcml6b25hMRMwEQYDVQQHEwpTY290dHNkYWxlMRow\n" +
                "GAYDVQQKExFHb0RhZGR5LmNvbSwgSW5jLjEtMCsGA1UECxMkaHR0cDovL2NlcnRz\n" +
                "LmdvZGFkZHkuY29tL3JlcG9zaXRvcnkvMTMwMQYDVQQDEypHbyBEYWRkeSBTZWN1\n" +
                "cmUgQ2VydGlmaWNhdGUgQXV0aG9yaXR5IC0gRzIwHhcNMTkwNzA0MjA0NjU3WhcN\n" +
                "MjAwNzA0MjA0NjU3WjA+MSEwHwYDVQQLExhEb21haW4gQ29udHJvbCBWYWxpZGF0\n" +
                "ZWQxGTAXBgNVBAMTEHRlbGVkZXJtYS5nb3YuY28wggEiMA0GCSqGSIb3DQEBAQUA\n" +
                "A4IBDwAwggEKAoIBAQCUe9N5itgsj2xEz3wiwFUmLlIim5P46Tl5HyOss6SI+qj5\n" +
                "eoK+6ZEX6Y5hzHcwoVyM9lCxWByWFu/znLWGRjBjsQ0l+6DxplKAgIsm0O7mhW2e\n" +
                "V+NQEIFqG1Nnlz7yzdj2Tz33EE8TFaRjpA/zUQ/2K0JpzG/x8/kjCa1W4Qu+/7B9\n" +
                "Ks39OvPtBJtq2JEvHBsBuVAeTFezkzCoGnBQ7BCERV0zW96h6SIUCYKF6H1TZn8Q\n" +
                "usPLyKQFf5U6vwEzQO23zdT4DBO//L7Ng3ZE7sqUl26hPbIX0lGxuyd3/yITHlgT\n" +
                "1I1dR6quO/dpBZ6o4P8x+6jnZeVLFuYF6vqFtmr7AgMBAAGjggLNMIICyTAMBgNV\n" +
                "HRMBAf8EAjAAMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjAOBgNVHQ8B\n" +
                "Af8EBAMCBaAwOAYDVR0fBDEwLzAtoCugKYYnaHR0cDovL2NybC5nb2RhZGR5LmNv\n" +
                "bS9nZGlnMnMxLTEyMTYuY3JsMF0GA1UdIARWMFQwSAYLYIZIAYb9bQEHFwEwOTA3\n" +
                "BggrBgEFBQcCARYraHR0cDovL2NlcnRpZmljYXRlcy5nb2RhZGR5LmNvbS9yZXBv\n" +
                "c2l0b3J5LzAIBgZngQwBAgEwdgYIKwYBBQUHAQEEajBoMCQGCCsGAQUFBzABhhho\n" +
                "dHRwOi8vb2NzcC5nb2RhZGR5LmNvbS8wQAYIKwYBBQUHMAKGNGh0dHA6Ly9jZXJ0\n" +
                "aWZpY2F0ZXMuZ29kYWRkeS5jb20vcmVwb3NpdG9yeS9nZGlnMi5jcnQwHwYDVR0j\n" +
                "BBgwFoAUQMK9J47MNIMwojPX+2yz8LQsgM4wMQYDVR0RBCowKIIQdGVsZWRlcm1h\n" +
                "Lmdvdi5jb4IUd3d3LnRlbGVkZXJtYS5nb3YuY28wHQYDVR0OBBYEFHXlwEL1e7lQ\n" +
                "I2k8uLlX1F/cgjSLMIIBBAYKKwYBBAHWeQIEAgSB9QSB8gDwAHYApLkJkLQYWBSH\n" +
                "uxOizGdwCjw1mAT5G9+443fNDsgN3BAAAAFrvr1+igAABAMARzBFAiEA859MkLZT\n" +
                "nxYE2LN6BLaJIA0iCi5HYdsTXQ+X/D9Ee+ECIEkwnAG8goX0yKG90RgBu5lTXQhZ\n" +
                "rx3rCgH2b4x+hQCBAHYAXqdz+d9WwOe1Nkh90EngMnqRmgyEoRIShBh1loFxRVgA\n" +
                "AAFrvr2D0wAABAMARzBFAiEA0m42ej2CeVzUoWUeYNfUUdj1kUYGgednKxoy/ITX\n" +
                "tlcCIBz/7yT/qVifbrlbwlds0RM/pF2stP86mfzc9oCB5UnoMA0GCSqGSIb3DQEB\n" +
                "CwUAA4IBAQCE2XZlBMRWx7WAhOcV5QFnVaFsRQuQAF43bRoM/4ZViPPPhX6LncVP\n" +
                "Zvu33K5gm5zOhp5nqmfRKrLHgZYetJbE4y08gSZSR97GRuc9rZDGNRk5fHgJhrPo\n" +
                "u6GfSgum4k/M1O3iTK0UtfJG3V+zGRedzQe3e9dqbcmcIjPgHHWikkig6ztY/EnK\n" +
                "FPFi0/2qsDVuWhuVkKKg8ianmyYetRr04yrk9YLCBNq8+LPb88bQoDRxEeSAny73\n" +
                "a4IGOcgZqQ0pwXa8St1hOoCb/MD0FTwCbTyOF6nVsvT2DY5cXydXGiKEe33unhYB\n" +
                "j/zeQa5k9KE8PGTUNrhNTOG+n84dCFJ0\n" +
                "-----END CERTIFICATE-----\n" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIIE0DCCA7igAwIBAgIBBzANBgkqhkiG9w0BAQsFADCBgzELMAkGA1UEBhMCVVMx\n" +
                "EDAOBgNVBAgTB0FyaXpvbmExEzARBgNVBAcTClNjb3R0c2RhbGUxGjAYBgNVBAoT\n" +
                "EUdvRGFkZHkuY29tLCBJbmMuMTEwLwYDVQQDEyhHbyBEYWRkeSBSb290IENlcnRp\n" +
                "ZmljYXRlIEF1dGhvcml0eSAtIEcyMB4XDTExMDUwMzA3MDAwMFoXDTMxMDUwMzA3\n" +
                "MDAwMFowgbQxCzAJBgNVBAYTAlVTMRAwDgYDVQQIEwdBcml6b25hMRMwEQYDVQQH\n" +
                "EwpTY290dHNkYWxlMRowGAYDVQQKExFHb0RhZGR5LmNvbSwgSW5jLjEtMCsGA1UE\n" +
                "CxMkaHR0cDovL2NlcnRzLmdvZGFkZHkuY29tL3JlcG9zaXRvcnkvMTMwMQYDVQQD\n" +
                "EypHbyBEYWRkeSBTZWN1cmUgQ2VydGlmaWNhdGUgQXV0aG9yaXR5IC0gRzIwggEi\n" +
                "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC54MsQ1K92vdSTYuswZLiBCGzD\n" +
                "BNliF44v/z5lz4/OYuY8UhzaFkVLVat4a2ODYpDOD2lsmcgaFItMzEUz6ojcnqOv\n" +
                "K/6AYZ15V8TPLvQ/MDxdR/yaFrzDN5ZBUY4RS1T4KL7QjL7wMDge87Am+GZHY23e\n" +
                "cSZHjzhHU9FGHbTj3ADqRay9vHHZqm8A29vNMDp5T19MR/gd71vCxJ1gO7GyQ5HY\n" +
                "pDNO6rPWJ0+tJYqlxvTV0KaudAVkV4i1RFXULSo6Pvi4vekyCgKUZMQWOlDxSq7n\n" +
                "eTOvDCAHf+jfBDnCaQJsY1L6d8EbyHSHyLmTGFBUNUtpTrw700kuH9zB0lL7AgMB\n" +
                "AAGjggEaMIIBFjAPBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBBjAdBgNV\n" +
                "HQ4EFgQUQMK9J47MNIMwojPX+2yz8LQsgM4wHwYDVR0jBBgwFoAUOpqFBxBnKLbv\n" +
                "9r0FQW4gwZTaD94wNAYIKwYBBQUHAQEEKDAmMCQGCCsGAQUFBzABhhhodHRwOi8v\n" +
                "b2NzcC5nb2RhZGR5LmNvbS8wNQYDVR0fBC4wLDAqoCigJoYkaHR0cDovL2NybC5n\n" +
                "b2RhZGR5LmNvbS9nZHJvb3QtZzIuY3JsMEYGA1UdIAQ/MD0wOwYEVR0gADAzMDEG\n" +
                "CCsGAQUFBwIBFiVodHRwczovL2NlcnRzLmdvZGFkZHkuY29tL3JlcG9zaXRvcnkv\n" +
                "MA0GCSqGSIb3DQEBCwUAA4IBAQAIfmyTEMg4uJapkEv/oV9PBO9sPpyIBslQj6Zz\n" +
                "91cxG7685C/b+LrTW+C05+Z5Yg4MotdqY3MxtfWoSKQ7CC2iXZDXtHwlTxFWMMS2\n" +
                "RJ17LJ3lXubvDGGqv+QqG+6EnriDfcFDzkSnE3ANkR/0yBOtg2DZ2HKocyQetawi\n" +
                "DsoXiWJYRBuriSUBAA/NxBti21G00w9RKpv0vHP8ds42pM3Z2Czqrpv1KrKQ0U11\n" +
                "GIo/ikGQI31bS/6kA1ibRrLDYGCD+H1QQc7CoZDDu+8CL9IVVO5EFdkKrqeKM+2x\n" +
                "LXY2JtwE65/3YR8V3Idv7kaWKK2hJn0KCacuBKONvPi8BDAB\n" +
                "-----END CERTIFICATE-----\n" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIIEfTCCA2WgAwIBAgIDG+cVMA0GCSqGSIb3DQEBCwUAMGMxCzAJBgNVBAYTAlVT\n" +
                "MSEwHwYDVQQKExhUaGUgR28gRGFkZHkgR3JvdXAsIEluYy4xMTAvBgNVBAsTKEdv\n" +
                "IERhZGR5IENsYXNzIDIgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTQwMTAx\n" +
                "MDcwMDAwWhcNMzEwNTMwMDcwMDAwWjCBgzELMAkGA1UEBhMCVVMxEDAOBgNVBAgT\n" +
                "B0FyaXpvbmExEzARBgNVBAcTClNjb3R0c2RhbGUxGjAYBgNVBAoTEUdvRGFkZHku\n" +
                "Y29tLCBJbmMuMTEwLwYDVQQDEyhHbyBEYWRkeSBSb290IENlcnRpZmljYXRlIEF1\n" +
                "dGhvcml0eSAtIEcyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv3Fi\n" +
                "CPH6WTT3G8kYo/eASVjpIoMTpsUgQwE7hPHmhUmfJ+r2hBtOoLTbcJjHMgGxBT4H\n" +
                "Tu70+k8vWTAi56sZVmvigAf88xZ1gDlRe+X5NbZ0TqmNghPktj+pA4P6or6KFWp/\n" +
                "3gvDthkUBcrqw6gElDtGfDIN8wBmIsiNaW02jBEYt9OyHGC0OPoCjM7T3UYH3go+\n" +
                "6118yHz7sCtTpJJiaVElBWEaRIGMLKlDliPfrDqBmg4pxRyp6V0etp6eMAo5zvGI\n" +
                "gPtLXcwy7IViQyU0AlYnAZG0O3AqP26x6JyIAX2f1PnbU21gnb8s51iruF9G/M7E\n" +
                "GwM8CetJMVxpRrPgRwIDAQABo4IBFzCCARMwDwYDVR0TAQH/BAUwAwEB/zAOBgNV\n" +
                "HQ8BAf8EBAMCAQYwHQYDVR0OBBYEFDqahQcQZyi27/a9BUFuIMGU2g/eMB8GA1Ud\n" +
                "IwQYMBaAFNLEsNKR1EwRcbNhyz2h/t2oatTjMDQGCCsGAQUFBwEBBCgwJjAkBggr\n" +
                "BgEFBQcwAYYYaHR0cDovL29jc3AuZ29kYWRkeS5jb20vMDIGA1UdHwQrMCkwJ6Al\n" +
                "oCOGIWh0dHA6Ly9jcmwuZ29kYWRkeS5jb20vZ2Ryb290LmNybDBGBgNVHSAEPzA9\n" +
                "MDsGBFUdIAAwMzAxBggrBgEFBQcCARYlaHR0cHM6Ly9jZXJ0cy5nb2RhZGR5LmNv\n" +
                "bS9yZXBvc2l0b3J5LzANBgkqhkiG9w0BAQsFAAOCAQEAWQtTvZKGEacke+1bMc8d\n" +
                "H2xwxbhuvk679r6XUOEwf7ooXGKUwuN+M/f7QnaF25UcjCJYdQkMiGVnOQoWCcWg\n" +
                "OJekxSOTP7QYpgEGRJHjp2kntFolfzq3Ms3dhP8qOCkzpN1nsoX+oYggHFCJyNwq\n" +
                "9kIDN0zmiN/VryTyscPfzLXs4Jlet0lUIDyUGAzHHFIYSaRt4bNYC8nY7NmuHDKO\n" +
                "KHAN4v6mF56ED71XcLNa6R+ghlO773z/aQvgSMO3kwvIClTErF0UZzdsyqUvMQg3\n" +
                "qm5vjLyb4lddJIGvl5echK1srDdMZvNhkREg5L4wn3qkKQmw4TRfZHcYQFHfjDCm\n" +
                "rw==\n" +
                "-----END CERTIFICATE-----\n" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIIEADCCAuigAwIBAgIBADANBgkqhkiG9w0BAQUFADBjMQswCQYDVQQGEwJVUzEh\n" +
                "MB8GA1UEChMYVGhlIEdvIERhZGR5IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBE\n" +
                "YWRkeSBDbGFzcyAyIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA0MDYyOTE3\n" +
                "MDYyMFoXDTM0MDYyOTE3MDYyMFowYzELMAkGA1UEBhMCVVMxITAfBgNVBAoTGFRo\n" +
                "ZSBHbyBEYWRkeSBHcm91cCwgSW5jLjExMC8GA1UECxMoR28gRGFkZHkgQ2xhc3Mg\n" +
                "MiBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASAwDQYJKoZIhvcNAQEBBQADggEN\n" +
                "ADCCAQgCggEBAN6d1+pXGEmhW+vXX0iG6r7d/+TvZxz0ZWizV3GgXne77ZtJ6XCA\n" +
                "PVYYYwhv2vLM0D9/AlQiVBDYsoHUwHU9S3/Hd8M+eKsaA7Ugay9qK7HFiH7Eux6w\n" +
                "wdhFJ2+qN1j3hybX2C32qRe3H3I2TqYXP2WYktsqbl2i/ojgC95/5Y0V4evLOtXi\n" +
                "EqITLdiOr18SPaAIBQi2XKVlOARFmR6jYGB0xUGlcmIbYsUfb18aQr4CUWWoriMY\n" +
                "avx4A6lNf4DD+qta/KFApMoZFv6yyO9ecw3ud72a9nmYvLEHZ6IVDd2gWMZEewo+\n" +
                "YihfukEHU1jPEX44dMX4/7VpkI+EdOqXG68CAQOjgcAwgb0wHQYDVR0OBBYEFNLE\n" +
                "sNKR1EwRcbNhyz2h/t2oatTjMIGNBgNVHSMEgYUwgYKAFNLEsNKR1EwRcbNhyz2h\n" +
                "/t2oatTjoWekZTBjMQswCQYDVQQGEwJVUzEhMB8GA1UEChMYVGhlIEdvIERhZGR5\n" +
                "IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBEYWRkeSBDbGFzcyAyIENlcnRpZmlj\n" +
                "YXRpb24gQXV0aG9yaXR5ggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQAD\n" +
                "ggEBADJL87LKPpH8EsahB4yOd6AzBhRckB4Y9wimPQoZ+YeAEW5p5JYXMP80kWNy\n" +
                "OO7MHAGjHZQopDH2esRU1/blMVgDoszOYtuURXO1v0XJJLXVggKtI3lpjbi2Tc7P\n" +
                "TMozI+gciKqdi0FuFskg5YmezTvacPd+mSYgFFQlq25zheabIZ0KbIIOqPjCDPoQ\n" +
                "HmyW74cNxA9hi63ugyuV+I6ShHI56yDqg+2DzZduCLzrTia2cyvk0/ZM/iZx4mER\n" +
                "dEr/VxqHD3VILs9RaRegAhJhldXRQLIQTO7ErBBDpqWeCtWVYpoNz4iCxTIM5Cuf\n" +
                "ReYNnyicsbkqWletNw+vHX/bvZ8=\n" +
                "-----END CERTIFICATE-----\n";
        InputStream stream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));

        return stream;
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link }, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    public static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        String pass = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQCUe9N5itgsj2xE\n" +
                "z3wiwFUmLlIim5P46Tl5HyOss6SI+qj5eoK+6ZEX6Y5hzHcwoVyM9lCxWByWFu/z\n" +
                "nLWGRjBjsQ0l+6DxplKAgIsm0O7mhW2eV+NQEIFqG1Nnlz7yzdj2Tz33EE8TFaRj\n" +
                "pA/zUQ/2K0JpzG/x8/kjCa1W4Qu+/7B9Ks39OvPtBJtq2JEvHBsBuVAeTFezkzCo\n" +
                "GnBQ7BCERV0zW96h6SIUCYKF6H1TZn8QusPLyKQFf5U6vwEzQO23zdT4DBO//L7N\n" +
                "g3ZE7sqUl26hPbIX0lGxuyd3/yITHlgT1I1dR6quO/dpBZ6o4P8x+6jnZeVLFuYF\n" +
                "6vqFtmr7AgMBAAECgf9rTRCFxNBzYzHaiBYtBtCBxveUCtnkBqaB4P7mzusYEgwK\n" +
                "PX4+eIthvGYoRpnbisBXHakyf3oo/GpqodSMNCzD3i6iRDUKbCcytbHZWyNJcyLE\n" +
                "Sr76HPNoulaLhcD3tcoUNS+SWzbJk9o4J1dffjUKsDhOPG6paqWfrF6XagoS+5nC\n" +
                "Ym3MFJd6+jVadnnFTACXtRRxBXj6VvngdQ/ffhIGczq9nU/jhniyEBkZx+WvBN7o\n" +
                "FyP2TPasI8N2oAAFFfZNI7Q5Lu3Evq1wXhvtIrOumNYT6EkJzeH3QK/pAEWckInr\n" +
                "VHWwUFemKc7R8plBvOLvUUXNr0wD4Di3YwFO1SkCgYEAxL4aqZTCSFPY0IyMXnEt\n" +
                "AzAFpnFC+TJwPnyFDOp4nPfN6B7b19S88tkRCYUDMCFioVpgafoVBM/4A70h16lD\n" +
                "7mUzrUbZYhajhyEtLeRjxQFL32xxqDfXfQiJAkupHgaUjPNZg1LVQEFQewAgkXWq\n" +
                "IjLl0zqacxkrxiLYN1nRzrcCgYEAwTS0jVMCgrFKqpVpfobluH2xWTXkIDw0/7Vc\n" +
                "vOObqMzIv4pWcKgJF2aDIvuHNQc71YM07G11dFldc5jAf9M1weaGxlNBtH7nIUdC\n" +
                "JsJ0Aw9sDZSiBcx7ocdzqcqE0ABPd24nfN56rOX+OgLVQB397g4GU26VVu9B+bSR\n" +
                "Eng5wd0CgYA2tKGXS8zl7aKZAqu5SwD4JO89Q7saraK2TUt1tvDIvMLda0836WiM\n" +
                "ojzxh7HGPVI+iXHLpgUYLHfLGlkBrYg9Dpja8ptZgfAAkAIPA7WWPvEI9A96deSO\n" +
                "WMQ4Bi/FMJE3zCa0tGClmEO3n+Zua+hd0AdFv8LYhEQ+uMukQG5tawKBgHVrqMez\n" +
                "SXVQdsaRo9nH9llX2q8TN5/3t2YpPUF3OyUtwFvYoYw6M7xLCyv5nkS2LsEmysVe\n" +
                "COI7ZbZZgbSuEp/TkNl7xBUEg0gQXL3dtFAf0VW3nXjUu9aotzzb3jfj2GgHRGx5\n" +
                "cikeZpGvIxeioq9JSsLuFEkPhBNUkE6fTsrpAoGAXHxVGiH55xd6gdCDDyP2gvZ1\n" +
                "mGpEAY9VBwNGnU9vStEfVLqmMr1+gS6vrjaJZkPRPauoNuZeZ5CQp/wFxjjoy0Zr\n" +
                "pWNh7Y+tYr1vHgbCPTw73wEI/ATyaJkv1mQSccYCYboC14exkc/rdynxZTPROUxz\n" +
                "AQmQ0ow8+VIeRcoviy0=\n" +
                "-----END PRIVATE KEY-----\n";
        // Put the certificates a key store.
        char[] password = pass.toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    public static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static Object crearServicioImagen(Class servicio) {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_IMGUR_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        builder.addConverterFactory(createGsonConverter());

        return builder.build().create(servicio);
    }

    private static Converter.Factory createGsonConverter() {
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapterFactory(new TeledermaTypeAdapterFactory())
                .create();
//                .setDateFormat("dd/MM/yyyy hh'H':mm'M'")
//                .setDateFormat("dd-MM-yyyy")


        return GsonConverterFactory.create(gson);
    }
    private static final String[] DATE_FORMATS = new String[] {
            "dd/MM/yyyy hh'H':mm'M'",
            "dd-MM-yyyy",
            "dd/MM/yyyy",
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd/MM/yyyy hh:mm a"
    };
    private static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            //for (String format : DATE_FORMATS) {
            try {
                if(jsonElement.getAsString().contains("H")) {
                    return new SimpleDateFormat(DATE_FORMATS[0], Locale.US).parse(jsonElement.getAsString());
                }else if(jsonElement.getAsString().contains("-") && jsonElement.getAsString().contains("T") == false){
                    return new SimpleDateFormat(DATE_FORMATS[1], Locale.US).parse(jsonElement.getAsString());
                }else if(jsonElement.getAsString().contains("/") && jsonElement.getAsString().contains("M") == false){
                    return new SimpleDateFormat(DATE_FORMATS[2], Locale.US).parse(jsonElement.getAsString());
                }else if(jsonElement.getAsString().contains("T")){
                    return new SimpleDateFormat(DATE_FORMATS[3]).parse(jsonElement.getAsString());
                }else if( (jsonElement.getAsString().contains("AM") || jsonElement.getAsString().contains("PM")) &&
                        jsonElement.getAsString().contains("/")){
                    return new SimpleDateFormat(DATE_FORMATS[4], Locale.US).parse(jsonElement.getAsString());
                }else{
                    if(jsonElement.getAsString() != null) {
                        return new SimpleDateFormat("dd/MM/yyyy").parse(jsonElement.getAsString());
                    }
                }
                //return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
            } catch (ParseException e) {
            }
            //}
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }

    // TODO Comentar
    static private class TeledermaTypeAdapterFactory implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                // Serializar
                public void write(JsonWriter out, T value) throws IOException {
                    if (value instanceof BaseEntity.Archivo) {
                        final BaseEntity.Archivo valueArchivo = (BaseEntity.Archivo) value;
                        out.value(valueArchivo.url);
                    } else
                        delegate.write(out, value);
                }

                // Deserializar
                public T read(JsonReader in) throws IOException {
                    return delegate.read(in);
                }
            };
        }
    }

    /**
     * Método que realiza la configuración por defecto para el observable respuesta de un servicio web
     *
     * @param contexto
     * @param servicioObservable
     * @param procesarRespuesta
     * @param procesarExcepcion
     * @param <R>
     */
    public static <R> void configurarObservable(
            Context contexto,
            Observable<Response<R>> servicioObservable,
            Consumer<? super Response<R>> procesarRespuesta,
            Consumer<? super Throwable> procesarExcepcion
    ) {

        servicioObservable.subscribeOn(Schedulers.io())
                .doOnNext(c -> hayConexionApi())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .retry((retryCount, throwable) -> {
                    if (retryCount < 3 && throwable instanceof SocketTimeoutException) {
                        //Toast.makeText(contexto, com.telederma.gov.co.R.string.msj_error_timeout_reintentando, Toast.LENGTH_SHORT).show();
                        Log.d("TIMEOUT", "Retry..." + retryCount, throwable);

                        return true;
                    }

                    return false;
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(procesarRespuesta, procesarExcepcion);
    }

    /**
     * Método que realiza la configuración por defecto para el observable respuesta de un servicio web
     * pero no lo subscribe por si se desea hacer operaciones adicionales con el observable
     *
     * @param contexto
     * @param servicioObservable
     * @param <R>
     */
    public static <R> void configurarObservableForSubscribe(
            Context contexto,
            Observable<Response<R>> servicioObservable
    ) {

        servicioObservable.subscribeOn(Schedulers.io())
                .doOnNext(c -> hayConexionApi())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .retry((retryCount, throwable) -> {
                    //Toast.makeText(contexto, com.telederma.gov.co.R.string.msj_error_timeout_reintentando, Toast.LENGTH_SHORT).show();
                    Log.d("TIMEOUT", "Retry..." + retryCount, throwable);

                    return retryCount < 3 && throwable instanceof SocketTimeoutException;
                })
                .throttleFirst(1, TimeUnit.SECONDS);
    }

    /**
     * Valida si se puede establecer conexión con el servidor de la api
     *
     * Importante: No usar en el hilo principal de la aplicación
     *
     * Autor: Daniel Hernández
     *
     * @throws IOException
     */
    public synchronized static void hayConexionApi() throws IOException {

//        final URLConnection connection = new URL(Constantes.API_REST_URL_BASE).openConnection();
//        connection.setConnectTimeout(10000);
//        connection.connect();

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        try {
                            URLConnection connection = new URL(Constantes.API_REST_URL_BASE).openConnection();
                            if(connection != null) {
                                connection.setConnectTimeout(10000);
                                try {
                                    connection.connect();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                    }
                }).create().start();

    }

    /**
     * Valida si se puede establecer conexión con el servidor de la api
     *
     * Nota: Este método utiliza el hilo principal de la aplicación
     *
     * Autor: Daniel Hernández
     *
     * @return
     */
    public static boolean validarConexionApi() {
        return true;
        //try {
//
        //
        //    String command = "ping -i 10 -c 1 " + Constantes.API_REST_IP_SERVER;
//
        //    return Runtime.getRuntime().exec(command).waitFor() == 0;
        //} catch (InterruptedException e) {
        //    Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error validando la conexión a la api", e);
        //} catch (IOException e) {
        //    Log.d(Constantes.TAG_ERROR_BASE_ACTIVITY, "Error validando la conexión a la api", e);
        //}
//
        //return false;
    }

}
