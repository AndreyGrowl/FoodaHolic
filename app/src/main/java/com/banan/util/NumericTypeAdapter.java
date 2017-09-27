package com.banan.util;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class NumericTypeAdapter extends TypeAdapter<Number> {

    @Override
    public void write(JsonWriter jsonWriter, Number number) throws IOException {
        if (number == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(number);
    }

    @Override
    public Number read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        try {
            String value = jsonReader.nextString();
            if (!value.matches("\\d+(?:\\.\\d+)?")) {
                value = value.replaceAll("\\D+", "");
            }
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

}