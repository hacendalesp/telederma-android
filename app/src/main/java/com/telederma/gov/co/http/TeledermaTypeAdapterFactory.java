package com.telederma.gov.co.http;

import com.telederma.gov.co.modelo.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Daniel Hernández on 26/10/2018.
 */

public class TeledermaTypeAdapterFactory implements TypeAdapterFactory {

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
