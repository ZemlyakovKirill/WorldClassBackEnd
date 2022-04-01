package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findAuthoritiesByAuthority(String authortity);
}
