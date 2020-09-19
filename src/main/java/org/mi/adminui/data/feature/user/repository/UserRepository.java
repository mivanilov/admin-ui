package org.mi.adminui.data.feature.user.repository;

import org.mi.adminui.data.core.repository.CrudJpaRepository;
import org.mi.adminui.data.feature.user.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudJpaRepository<User, String> {
}
