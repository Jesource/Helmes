package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.repos.UserDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class UserDataServiceImpl implements UserDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataServiceImpl.class);
    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public Optional<UserData> getUserDataById(long id) {
        return userDataRepository.findById(id);
    }

    @Override
    public Optional<Long> validateAndSaveUserData(UserData userData) {
        try {
            validateUserData(userData);
            return Optional.of(userDataRepository.save(userData).getId());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Failed saving UserData ({}): {}}", userData, e.getMessage());
            return Optional.empty();
        }
    }

    private void validateUserData(UserData userData) throws IllegalArgumentException {
        if (userData.getName() == null || userData.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (userData.getIdsOfInvolvedSectors() == null || userData.getIdsOfInvolvedSectors().isEmpty()) {
            throw new IllegalArgumentException("At least one sector must be chosen");
        }
    }
}
