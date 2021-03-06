package com.codepoetics.octarine.functional.matching;

import com.codepoetics.octarine.records.Key;
import com.codepoetics.octarine.records.Record;
import com.codepoetics.octarine.functional.paths.Path;
import com.codepoetics.octarine.records.example.Address;
import com.codepoetics.octarine.records.example.Person;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.awt.*;

import static com.codepoetics.octarine.Octarine.$;
import static com.codepoetics.octarine.Octarine.$$;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchingExtractorTest {

    private static final Key<String> title = $("title");
    private static final Key<String> director = $("director");
    private static final Key<Integer> rating = $("rating");

    private static final Record person = $$(
            Person.name.of("Richard Rotry"),
            Person.age.of(42),
            Person.favouriteColour.of(Color.CYAN),
            Person.address.of(
                    Address.addressLines.of("13 Newbury Drive", "Sutton", "SU1 4PG")
            )
    );

    private static final MatchingExtractor<Record, String> matching = MatchingExtractor.build(m -> m
            .matching(Person.schema, v -> "A valid person: " + v)
            .matching(Address.schema, a -> "A valid address: " + a)
            .when(rating.is(5))
            .matching(title, director, (t, d) -> String.format("The 5-star movie '%s', directed by %s", t, d))
            .unless(rating.is(0))
            .matching(title, director, (t, d) -> String.format("The movie '%s', directed by %s", t, d))
            .matching(title, director, (t, d) -> String.format("The utter stinker '%s', directed by %s", t, d)));

    @Test
    public void
    dispatches_on_schema() {
        assertThat(matching.extract(person), containsString("A valid person"));
        assertThat(matching.extract(Person.address.extract(person)), containsString("A valid address"));
    }

    @Test
    public void
    dispatches_on_present_keys() {
        Record movie = $$(title.of("Brazil"), director.of("Terry Gilliam"));
        assertThat(matching.extract(movie), containsString("The movie 'Brazil', directed by Terry Gilliam"));
    }

    @Test
    public void
    dispatches_on_predicates_and_present_keys() {
        Record movie1 = $$(title.of("Existenz"), director.of("David Cronenberg"), rating.of(4));
        Record movie2 = $$(title.of("Brazil"), director.of("Terry Gilliam"), rating.of(5));
        Record movie3 = $$(title.of("Howard the Duck"), director.of("Willard Huyck"), rating.of(0));

        assertThat(matching.extract(movie2), containsString("The 5-star movie 'Brazil', directed by Terry Gilliam"));
        assertThat(matching.extract(movie1), containsString("The movie 'Existenz', directed by David Cronenberg"));
        assertThat(matching.extract(movie3), containsString("The utter stinker"));
    }

    @Test
    public void
    dispatches_on_paths() {
        MatchingExtractor<Record, String> matchingExtractor = MatchingExtractor.build(m ->
                m.matching(Person.address.join(Address.addressLines).join(Path.toIndex(1)),
                        s -> "Lives in " + s));

        assertThat(matchingExtractor.extract(person), CoreMatchers.equalTo("Lives in Sutton"));
    }


}
