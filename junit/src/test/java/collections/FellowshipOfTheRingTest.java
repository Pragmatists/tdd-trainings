package collections;

import com.google.common.collect.Maps;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class FellowshipOfTheRingTest {

    private Fellowship fellows = readFellowshipMembersFromTheBook();

    // Asercje kontekstowe na obiektach pozwalają sprawdzać przynależność do kolekcji
    // TODO: Popraw skład drużyny aby test przeszedł
    @Ignore
    @Test
    public void frodo_is_in() {
        assertThat(Frodo()).isIn(fellows);
        assertThat(Sauron()).isNotIn(fellows);
    }

    // Fest zawiera asercje specyficzne dla kolekcji i jeszcze bardziej specyficzne dla list
    @Ignore
    @Test
    public void races_in_fellowship() {
        // dla jednego elfa mamy to samo co w poprzednim teście
        assertThat(fellows.elfs()).hasSize(1).contains(Legolas());
        // .contains() nie uwzględnia kolejności
        assertThat(fellows.men()).contains(Boromir(), Aragorn())
                .doesNotContain(Gandalf())
                .doesNotHaveDuplicates(); // TODO: usuń duplikat w składzie drużyny
        assertThat(fellows.hobbits()).hasSize(4).containsSequence(Sam(), /* TODO: uzupełnij sekwencję */ Pippin());
        assertThat(fellows.hobbits()).containsExactly(/* TODO: uzupełnij */ Pippin());
        // TODO: dodaj jeszcze kilka asercji poznając API festa
    }

    // nie zawsze interesuje nas sprawdzanie wszystkich pól obiektu
    @Ignore
    @Test
    public void fellows_have_names() {
        Fellowship fellows = readFellowshipMembersFromTheBook();

        // TODO: Skróć poniższe asercje do jednej linii. Pdpowiedź: użyj assertThat(extractProperty(...).from(...))
        assertThat(fellows.get(0).getName()).isEqualTo("Sam");
        assertThat(fellows.get(1).getName()).isEqualTo("Merry");
        assertThat(fellows.get(2).getName()).isEqualTo("Pippin");
        assertThat(fellows.get(3).getName()).isEqualTo("Gandalf");
        assertThat(fellows.get(4).getName()).isEqualTo("Aragorn");
        assertThat(fellows.get(5).getName()).isEqualTo("Legolas");
        assertThat(fellows.get(6).getName()).isEqualTo("Gimli");
        assertThat(fellows.get(7).getName()).isEqualTo("Boromir");
    }

    // Fest zawiera także asercje specyficzne dla map
    @Test
    public void ring_bearers() {
        Map<Ring, Fellow> ringBearers = createRingBearers();

        assertThat(ringBearers)
                .hasSize(2)
                .doesNotContainValue(Sam())
                .doesNotContain(entry(Ring.oneRing, Aragorn()))
        // TODO: dodaj sprawdzenie elementów, które faktycznie są w mapie
        ;
    }

    private Map<Ring, Fellow> createRingBearers() {
        HashMap<Ring,Fellow> bearers = Maps.newHashMap();
        bearers.put(Ring.oneRing, Frodo());
        bearers.put(Ring.nenya, Galadriel());
        return bearers;
    }

    private Fellow Galadriel() {
        return fellow().named("Galadriel").race("Elf");
    }


    private Fellow Gandalf() {
        return fellow().named("Gandalf").race("Maiar");
    }

    private Fellow Aragorn() {
        return fellow().named("Aragorn").race("Man");
    }

    private Fellow Boromir() {
        return fellow().named("Boromir").race("Man");
    }

    private Fellow Pippin() {
        return fellow().named("Pippin").race("Hobbit");
    }

    private Fellow Sam() {
        return fellow().named("Sam").race("Hobbit");
    }


    private Fellow Legolas() {
        return fellow().named("Legolas").race("Elf");
    }

    private Fellow Frodo() {
        return fellow().named("Frodo").race("Hobbit");
    }


    private Fellow Sauron() {
        return fellow().named("Sauron").race("Ainur");
    }

    private Fellowship readFellowshipMembersFromTheBook() {
        return new Fellowship(
                Sam(),
                fellow().named("Merry").race("Hobbit"),
                Pippin(),
                Gandalf(),
                Aragorn(),
                Legolas(),
                Boromir(),
                fellow().named("Gimli").race("Dwarf"),
                Boromir(),
                Sauron()
        );
    }

    private FellowBuilder fellow() {
        return new FellowBuilder();
    }

}
