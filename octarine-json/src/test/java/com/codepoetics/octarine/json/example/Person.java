package com.codepoetics.octarine.json.example;

import com.codepoetics.octarine.api.Key;
import com.codepoetics.octarine.json.deserialisation.RecordDeserialiser;
import com.codepoetics.octarine.json.serialisation.JsonRecordSerialiser;
import com.codepoetics.octarine.keys.KeySet;
import com.codepoetics.octarine.validation.api.Schema;
import com.codepoetics.octarine.validation.api.ValidRecordKey;

import java.awt.*;
import java.util.function.Function;

import static com.codepoetics.octarine.json.serialisation.JsonSerialisers.asInteger;
import static com.codepoetics.octarine.json.serialisation.JsonSerialisers.asString;

public interface Person {

    static final KeySet mandatoryKeys = new KeySet();
    static final Key<String> name = mandatoryKeys.add("name");
    static final Key<Integer> age = mandatoryKeys.add("age");
    public static final Key<Color> favouriteColour = mandatoryKeys.add("favourite colour");

    public static final ValidRecordKey<Address> address =
            mandatoryKeys.addValidRecord("address", Address.schema);

    static final Schema<Person> schema = (record, validationErrors) -> {
        mandatoryKeys.accept(record, validationErrors);
        age.get(record).ifPresent(a -> {
            if (a < 0) validationErrors.accept("Age must be 0 or greater");
        });
    };

    public static final Function<Color, String> colourToString = c -> "0x" + Integer.toHexString(c.getRGB()).toUpperCase().substring(2);

    public static final JsonRecordSerialiser serialiser = p ->
            p.add(Person.name, asString)
                    .add(age, asInteger)
                    .add(favouriteColour, (g, c) -> asString.accept(g, colourToString.apply(c)))
                    .add(address, Address.serialiser);

    public static final RecordDeserialiser deserialiser = RecordDeserialiser.builder()
            .readString(name)
            .readInteger(age)
            .readFromString(favouriteColour, Color::decode)
            .readValidRecord(address, Address.deserialiser.validAgainst(Address.schema))
            .get();
}