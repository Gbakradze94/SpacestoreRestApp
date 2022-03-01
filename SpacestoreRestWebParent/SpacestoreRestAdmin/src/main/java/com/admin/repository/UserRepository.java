package com.admin.repository;

import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    List<User> findAll();

    Optional<User> findByEmail(String email);

    UserDto getUserByEmail(String email);
}
