package com.sid.securityservice.services;

import com.sid.securityservice.entities.AppRole;
import com.sid.securityservice.entities.AppUser;
import com.sid.securityservice.repositories.AppRoleRepository;
import com.sid.securityservice.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class AccountServiceImpl implements AccountService
{
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppRoleRepository appRoleRepository,
                              AppUserRepository appUserRepository,
                              PasswordEncoder passwordEncoder)
    {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser)
    {
        String pwd = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pwd));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole)
    {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String userName, String roleName)
    {
        AppUser usr = appUserRepository.findAppUserByUserName(userName);
        AppRole role = appRoleRepository.findAppRoleByRoleName(roleName);
        usr.getAppRoles().add(role);
    }

    @Override
    public AppUser loadUserByUserName(String userName)
    {
        return appUserRepository.findAppUserByUserName(userName);
    }

    @Override
    public List<AppUser> listUsers()
    {
        return appUserRepository.findAll();
    }
}
