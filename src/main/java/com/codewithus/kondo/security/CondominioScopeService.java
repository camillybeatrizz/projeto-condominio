package com.codewithus.kondo.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CondominioScopeService {

    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CurrentUserResolver currentUserResolver;
    private final ResourceOwnershipService resourceOwnershipService;

    public CondominioScopeService(AuthenticatedUserFacade authenticatedUserFacade,
                                  CurrentUserResolver currentUserResolver,
                                  ResourceOwnershipService resourceOwnershipService) {
        this.authenticatedUserFacade = authenticatedUserFacade;
        this.currentUserResolver = currentUserResolver;
        this.resourceOwnershipService = resourceOwnershipService;
    }

    public List<UUID> getCondominioIdsPermitidos() {
        if (!authenticatedUserFacade.isAuthenticated() || authenticatedUserFacade.isAdmin()) {
            return List.of();
        }

        return currentUserResolver.findCurrentUsuarioId()
                .map(resourceOwnershipService::findCondominioIdsByUsuarioId)
                .orElse(List.of());
    }

    public void assertCanAccessCondominio(UUID condominioId) {
        if (!authenticatedUserFacade.isAuthenticated() || authenticatedUserFacade.isAdmin()) {
            return;
        }

        if (currentUserResolver.findCurrentUsuarioId()
                .filter(usuarioId -> resourceOwnershipService.canAccessCondominio(usuarioId, condominioId))
                .isEmpty()) {
            throw new AccessDeniedException("Acesso negado");
        }
    }
}
