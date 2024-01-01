rootProject.name = "livadoo-api"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

include(
    "app",
    "account-service",
    "auth-service",
    "auth-service-proxy",
    "user-service",
    "user-service-proxy",
    "shared",
    "notification-service",
    "inventory-service",
    "storage-service",
    "customer-service",
    "notification-service-proxy",
    "storage-service-proxy",
    "customer-service-proxy",
    "inventory-service-proxy",
    "utils:exception-utils",
    "utils:security-utils",
    "utils:spring-utils",
    "utils:user-utils",
    "role-service",
    "permission-service",
)
