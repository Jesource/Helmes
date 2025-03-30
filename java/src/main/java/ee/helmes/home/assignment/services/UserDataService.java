package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.UserData;

import java.util.Optional;

public interface UserDataService {
    Optional<UserData> getUserDataById(long id);
    Optional<Long> validateAndSaveUserData(UserData userData);
}
