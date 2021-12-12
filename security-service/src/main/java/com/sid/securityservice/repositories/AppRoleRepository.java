package com.sid.securityservice.repositories;

import com.sid.securityservice.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRole, Long>
{
    AppRole findAppRoleByRoleName(String roleName);
}
