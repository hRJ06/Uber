package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByUser(User user);
}
