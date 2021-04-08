package bus.core.service;

import bus.persistence.domain.User;

public interface IUserService {

    /**
     * Get user by use id
     *
     * @param id user id to earch for
     * @return user object or null if not found
     */
    User getUser(Long id);

    /**
     * Persist user to DB
     *
     * @param user user to save
     */
    void persist(User user);
}
