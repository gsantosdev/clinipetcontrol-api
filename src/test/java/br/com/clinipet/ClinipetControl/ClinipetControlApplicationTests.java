package br.com.clinipet.ClinipetControl;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ClinipetControlApplicationTests {

    @Test
    void test() {

        Boolean valido = compareTime();

        Assertions.assertThat(valido).isTrue();

    }


    public static boolean compareTime() {
        final LocalTime horarioEstabInicio = new LocalTime(12, 30);
        final LocalTime horarioEstabFim = new LocalTime(20, 40);

        return horarioEstabInicio.isBefore(horarioEstabFim);

    }

}


