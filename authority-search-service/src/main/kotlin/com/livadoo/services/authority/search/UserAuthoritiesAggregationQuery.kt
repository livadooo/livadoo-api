package com.livadoo.services.authority.search

import org.bson.Document

object UserAuthoritiesAggregationQuery {
    fun getUserAuthoritiesAggregationQuery(userId: String): List<Document> {
        return listOf(
            getUserStage(userId),
            lookupRoles(),
            lookupPermissions(),
            getRolesAndPermissionsDocumentsMatchingUserRoleIdsAndPermissionIds(),
            extractRolesAndPermissionsStringsFromMatchedDocuments(),
            keepRolesAndPermissionsFields(),
        )
    }

    private fun getUserStage(userId: String): Document {
        return Document("\$match", Document("userId", userId))
    }

    private fun lookupRoles(): Document {
        return Document(
            "\$lookup",
            Document("from", "roles")
                .append("as", "roles")
                .append(
                    "pipeline",
                    listOf(
                        Document(
                            "\$project",
                            Document("roleId", 1).append("role", 1),
                        ),
                    ),
                ),
        )
    }

    private fun lookupPermissions(): Document {
        return Document(
            "\$lookup",
            Document("from", "permissions")
                .append("as", "permissions")
                .append(
                    "pipeline",
                    listOf(
                        Document(
                            "\$project",
                            Document("permissionId", 1).append("permission", 1),
                        ),
                    ),
                ),
        )
    }

    private fun getRolesAndPermissionsDocumentsMatchingUserRoleIdsAndPermissionIds(): Document {
        return Document(
            "\$addFields",
            Document(
                "roles",
                Document(
                    "\$filter",
                    Document("input", "\$roles").append("as", "role")
                        .append("cond", Document("\$in", listOf("\$\$role.roleId", "\$roleIds"))),
                ),
            ).append(
                "permissions",
                Document(
                    "\$filter",
                    Document("input", "\$permissions").append("as", "permission")
                        .append("cond", Document("\$in", listOf("\$\$permission.permissionId", "\$permissionIds"))),
                ),
            ),
        )
    }

    private fun extractRolesAndPermissionsStringsFromMatchedDocuments(): Document {
        return Document(
            "\$addFields",
            Document(
                "roles",
                Document(
                    "\$map",
                    Document("input", "\$roles").append("as", "role")
                        .append("in", "\$\$role.role"),
                ),
            ).append(
                "permissions",
                Document(
                    "\$map",
                    Document("input", "\$permissions").append("as", "permission")
                        .append("in", "\$\$permission.permission"),
                ),
            ),
        )
    }

    private fun keepRolesAndPermissionsFields(): Document {
        return Document(
            "\$project",
            Document("roles", 1).append("permissions", 1).append("_id", 0),
        )
    }
}
