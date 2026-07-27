package org.example.encode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.buildings.Size;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.Deflater;

public class Encoder {
    public static String encode(List<Entity> entities, String name) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Blueprint> encodeObject = new HashMap<>();
        encodeObject.put("blueprint", new Blueprint("blueprint", name, "562949958467584", entities));

        // 1. Преобразуем объект в JSON-строку
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(encodeObject);

        // 2. Сжимаем zlib (уровень 9)
        byte[] jsonBytes = json.getBytes("UTF-8");
        Deflater deflater = new Deflater(9);
        deflater.setInput(jsonBytes);
        deflater.finish();
        byte[] compressed = new byte[jsonBytes.length * 2]; // буфер с запасом
        int compressedSize = deflater.deflate(compressed);
        deflater.end();

        // Обрезаем массив до реального размера
        byte[] compressedData = new byte[compressedSize];
        System.arraycopy(compressed, 0, compressedData, 0, compressedSize);

        // 3. Кодируем в Base64
        String base64 = Base64.getEncoder().encodeToString(compressedData);

        // 4. Добавляем версию (0) в начало
        return "0" + base64;
    }
}

