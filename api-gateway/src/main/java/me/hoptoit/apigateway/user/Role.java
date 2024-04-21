package me.hoptoit.apigateway.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum Role
{
    USER(Collections.EMPTY_SET),
    ADMIN(Set.of(Permission.ADMIN_READ, Permission.ADMIN_CREATE, Permission.ADMIN_UPDATE,Permission.ADMIN_DELETE,
            Permission.MANAGER_READ, Permission.MANAGER_CREATE, Permission.MANAGER_UPDATE,Permission.MANAGER_DELETE
    )),
    MANAGER(Set.of(Permission.MANAGER_READ, Permission.MANAGER_CREATE, Permission.MANAGER_UPDATE,Permission.MANAGER_DELETE
    ))
    ;

    private final Set<Permission> permissions;
}
