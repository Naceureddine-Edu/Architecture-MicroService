package com.sid.securityservice.services;


import com.sid.securityservice.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AccountService
{
    public AppUser addNewUser(AppUser appUser);
    public AppRole addNewRole(AppRole appRole);
    public void addRoleToUser(String userName, String roleName);
    public AppUser loadUserByUserName(String userName);
    public List<AppUser> listUsers();
}
