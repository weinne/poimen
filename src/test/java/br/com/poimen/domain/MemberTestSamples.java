package br.com.poimen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Member getMemberSample1() {
        return new Member()
            .id(1L)
            .name("name1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .address("address1")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .cityOfBirth("cityOfBirth1")
            .previousReligion("previousReligion1")
            .spouseName("spouseName1")
            .cpf("cpf1")
            .rg("rg1")
            .churchOfBaptism("churchOfBaptism1")
            .associationMeetingMinutes("associationMeetingMinutes1")
            .exitDestination("exitDestination1")
            .exitMeetingMinutes("exitMeetingMinutes1");
    }

    public static Member getMemberSample2() {
        return new Member()
            .id(2L)
            .name("name2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .address("address2")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .cityOfBirth("cityOfBirth2")
            .previousReligion("previousReligion2")
            .spouseName("spouseName2")
            .cpf("cpf2")
            .rg("rg2")
            .churchOfBaptism("churchOfBaptism2")
            .associationMeetingMinutes("associationMeetingMinutes2")
            .exitDestination("exitDestination2")
            .exitMeetingMinutes("exitMeetingMinutes2");
    }

    public static Member getMemberRandomSampleGenerator() {
        return new Member()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .cityOfBirth(UUID.randomUUID().toString())
            .previousReligion(UUID.randomUUID().toString())
            .spouseName(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .rg(UUID.randomUUID().toString())
            .churchOfBaptism(UUID.randomUUID().toString())
            .associationMeetingMinutes(UUID.randomUUID().toString())
            .exitDestination(UUID.randomUUID().toString())
            .exitMeetingMinutes(UUID.randomUUID().toString());
    }
}
