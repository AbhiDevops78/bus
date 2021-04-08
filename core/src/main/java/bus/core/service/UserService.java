package bus.core.service;

import bus.persistence.dao.IUserDao;
import bus.persistence.domain.User;

import javax.inject.Inject;

public class UserService implements IUserService {
    private final IUserDao userDao;

    @Inject
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUser(Long id) {
        return userDao.findById(id).orElse(null);
    }

    public void persist(User user) {
        userDao.persist(user);
    }
}
