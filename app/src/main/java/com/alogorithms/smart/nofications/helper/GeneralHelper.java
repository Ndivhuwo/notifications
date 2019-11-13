package com.alogorithms.smart.nofications.helper;

import com.alogorithms.smart.nofications.model.enumaration.AlertType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GeneralHelper {

    public static AlertType alertTypeFromString(String type) {
        switch (type) {
            case "Crime":
                return AlertType.CRIME;
            case "Municipal":
                return AlertType.MUNICIPAL;
            case "Accident":
                return AlertType.ACCIDENT;
            case "Fraud/Bribery":
                return AlertType.FRAUD;
            default:
                return AlertType.CRIME;

        }
    }

    public static class MyDateTypeAdapter2 extends TypeAdapter<Date> {
        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));
            if (value == null)
                out.nullValue();
            else
                out.value(df.format(value));
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return df.parse(in.nextString());
            } catch (final java.text.ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
