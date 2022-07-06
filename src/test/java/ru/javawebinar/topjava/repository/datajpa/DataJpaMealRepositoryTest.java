package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.DATA_JPA)
public class DataJpaMealRepositoryTest extends MealServiceTest {
}
