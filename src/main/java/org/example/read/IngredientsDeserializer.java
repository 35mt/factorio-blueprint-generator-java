package org.example.read;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Костыль - recipe-unknown ломает логику пустыми фигурными скобками в ingredients и results
public class IngredientsDeserializer extends JsonDeserializer<List<Item>> {
    @Override
    public List<Item> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Если встретили начало объекта
        if (p.currentToken() == JsonToken.START_OBJECT) {
            JsonNode node = p.readValueAsTree();
            // Если это пустой объект - возвращаем пустой список
            if (node.isObject() && node.isEmpty()) {
                return new ArrayList<>();
            }
            // Если не пустой объект - ошибка
            throw ctxt.mappingException("Expected array or empty object, but got non-empty object");
        }
        // Если встретили массив - десериализуем как обычно
        if (p.currentToken() == JsonToken.START_ARRAY) {
            return p.readValueAs(new TypeReference<List<Item>>() {});
        }
        // Если пришло что-то другое
        return null;
    }
}