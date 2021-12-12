package com.sid.securityservice.Controllers;

import com.sid.securityservice.entities.AppRole;
import com.sid.securityservice.entities.AppUser;
import com.sid.securityservice.services.AccountServiceImpl;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccountRestController
{
    private final AccountServiceImpl accountService;

    public AccountRestController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/users")
    public AppUser addUser(@RequestBody AppUser appUser)
    {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path ="/roles")
    public AppRole addRole(@RequestBody AppRole appRole)
    {
        return accountService.addNewRole(appRole);
    }

    @GetMapping(path ="/users")
    public List<AppUser> appUserList()
    {
        return accountService.listUsers();
    }

    @GetMapping(path ="/users/{userName}")
    public AppUser appUser(@PathVariable(name = "userName") String userName)
    {
        return accountService.loadUserByUserName(userName);
    }

    @PostMapping(path ="/addRoleToUser")
    public void addRoleToUser(RoleToUser roleToUserClass)
    {
        accountService.addRoleToUser(roleToUserClass.getAppUserName(),roleToUserClass.getAppRoleName());
    }
}

@Data
class RoleToUser
{
    private String appUserName;
    private String appRoleName;
}
