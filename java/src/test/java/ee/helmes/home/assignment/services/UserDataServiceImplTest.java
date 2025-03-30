package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.repos.UserDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDataServiceImplTest {
    private final UserDataRepository mockUserDataRepository = Mockito.mock(UserDataRepository.class);
    private final UserDataService userDataService = new UserDataServiceImpl(mockUserDataRepository);

    @Test
    void givenUserDataId_whenRequestingUserDataById_thenCorrectUserDataReturned() {
        UserData returnableUserData = new UserData();
        returnableUserData.setId(1L);
        returnableUserData.setName("expected name");
        returnableUserData.setAgreeToTerms(true);
        returnableUserData.setIdsOfInvolvedSectors(List.of(1, 3, 5));

        when(mockUserDataRepository.findById(1L)).thenReturn(Optional.of(returnableUserData));
        Optional<UserData> optionalUserData = userDataService.getUserDataById(1);
        assertTrue(optionalUserData.isPresent());

        UserData actualUserData = optionalUserData.get();
        UserData expected = deepCopy(returnableUserData, UserData.class);
        assertEquals(expected, actualUserData);
    }

    @Test
    void validateAndSaveUserData() {
    }

    private static <T> T deepCopy(T object, Class<T> clazz) {
        try {
            T copy = clazz.getDeclaredConstructor().newInstance(); // Create a new instance

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true); // Access private fields
                field.set(copy, field.get(object)); // Copy field value
            }

            return copy;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy object", e);
        }
    }
}